package com.github.bigexcalibur.herovideo.mvp.bangumi.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.adapter.section.HomeBangumiBannerSection;
import com.github.bigexcalibur.herovideo.adapter.section.HomeBangumiItemSection;
import com.github.bigexcalibur.herovideo.adapter.section.HomeBangumiNewSerialSection;
import com.github.bigexcalibur.herovideo.adapter.section.HomeBangumiRecommendSection;
import com.github.bigexcalibur.herovideo.adapter.section.HomeBangumiSeasonNewSection;
import com.github.bigexcalibur.herovideo.mvp.bangumi.model.BangumiAppIndexInfo;
import com.github.bigexcalibur.herovideo.mvp.bangumi.model.BangumiRecommendInfo;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.ui.widget.CustomEmptyView;
import com.github.bigexcalibur.herovideo.ui.widget.banner.BannerEntity;
import com.github.bigexcalibur.herovideo.ui.widget.sectioned.SectionedRecyclerViewAdapter;
import com.github.bigexcalibur.herovideo.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 首页番剧界面
 */
public class HomeBangumiFragment extends RxLazyFragment
{

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    private boolean mIsRefreshing = false;

    private List<BannerEntity> bannerList = new ArrayList<>();

    private List<BangumiRecommendInfo.ResultBean> bangumiRecommends = new ArrayList<>();

    private List<BangumiAppIndexInfo.ResultBean.AdBean.HeadBean> banners = new ArrayList<>();

    private List<BangumiAppIndexInfo.ResultBean.AdBean.BodyBean> bangumibobys = new ArrayList<>();

    private List<BangumiAppIndexInfo.ResultBean.PreviousBean.ListBean> seasonNewBangumis = new ArrayList<>();

    private List<BangumiAppIndexInfo.ResultBean.SerializingBean> newBangumiSerials = new ArrayList<>();

    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;

    private int season;

    public static HomeBangumiFragment newInstance()
    {

        return new HomeBangumiFragment();
    }

    @Override
    public int getLayoutResId()
    {

        return R.layout.fragment_home_bangumi;
    }

    @Override
    public void finishCreateView(Bundle state)
    {

        isPrepared = true;
        onLazyLoad();
    }

    @Override
    public void onLazyLoad()
    {

        if (!isPrepared || !isVisible)
            return;

        initRefreshLayout();
        initRecyclerView();
        isPrepared = false;
    }

    protected void initRecyclerView()
    {

        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {

            @Override
            public int getSpanSize(int position)
            {

                switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position))
                {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 3;
                    default:
                        return 1;
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);
        setRecycleNoScroll();
    }

    public void initRefreshLayout()
    {

        mSwipeRefreshLayout.setColorSchemeResources(R.color.theme_color_primary);
        mSwipeRefreshLayout.post(() -> {

            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            loadData();
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            clearData();
            loadData();
        });
    }


    private void clearData()
    {

        mIsRefreshing = true;
        banners.clear();
        bannerList.clear();
        bangumibobys.clear();
        bangumiRecommends.clear();
        newBangumiSerials.clear();
        seasonNewBangumis.clear();
        mSectionedRecyclerViewAdapter.removeAllSections();
    }


    @Override
    public void loadData()
    {

        RetrofitHelper.getBangumiAPI()
                .getBangumiAppIndex()
                .compose(bindToLifecycle())
                .flatMap(new Func1<BangumiAppIndexInfo,Observable<BangumiRecommendInfo>>()
                {

                    @Override
                    public Observable<BangumiRecommendInfo> call(BangumiAppIndexInfo bangumiAppIndexInfo)
                    {

                        banners.addAll(bangumiAppIndexInfo.getResult().getAd().getHead());
                        bangumibobys.addAll(bangumiAppIndexInfo.getResult().getAd().getBody());
                        seasonNewBangumis.addAll(bangumiAppIndexInfo.getResult().getPrevious().getList());
                        season = bangumiAppIndexInfo.getResult().getPrevious().getSeason();
                        newBangumiSerials.addAll(bangumiAppIndexInfo.getResult().getSerializing());
                        return RetrofitHelper.getBangumiAPI().getBangumiRecommended();
                    }
                })
                .compose(bindToLifecycle())
                .map(BangumiRecommendInfo::getResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {

                    bangumiRecommends.addAll(resultBeans);
                    finishTask();
                }, throwable -> {
                    initEmptyView();
                });
    }

    @Override
    public void finishTask()
    {

        mSwipeRefreshLayout.setRefreshing(false);
        mIsRefreshing = false;
        hideEmptyView();

        Observable.from(banners)
                .compose(bindToLifecycle())
                .forEach(bannersBean -> bannerList.add(new BannerEntity(
                        bannersBean.getLink(), bannersBean.getTitle(), bannersBean.getImg())));

        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiBannerSection(bannerList));
        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiItemSection(getActivity()));
        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiNewSerialSection(getActivity(), newBangumiSerials));
//        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiBobySection(getActivity(), bangumibobys));
        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiSeasonNewSection(getActivity(), season, seasonNewBangumis));
        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiRecommendSection(getActivity(), bangumiRecommends));
        mSectionedRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void initEmptyView()
    {
        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.drawable.img_tips_error_load_error);
        mCustomEmptyView.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
        SnackbarUtil.showMessage(mRecyclerView, "数据加载失败,请重新加载或者检查网络是否链接");
    }

    public void hideEmptyView()
    {

        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setRecycleNoScroll()
    {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }
}
