package com.github.bigexcalibur.herovideo.mvp.recommend.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.adapter.section.HomeRecommendBannerSection;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.mvp.recommend.model.RecommendBannerInfo;
import com.github.bigexcalibur.herovideo.mvp.recommend.model.RecommendInfo;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.ui.widget.CustomEmptyView;
import com.github.bigexcalibur.herovideo.ui.widget.banner.BannerEntity;
import com.github.bigexcalibur.herovideo.ui.widget.sectioned.SectionedRecyclerViewAdapter;
import com.github.bigexcalibur.herovideo.util.LogUtil;
import com.github.bigexcalibur.herovideo.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/4 11:58
 * 100332338@qq.com
 * <p/>
 * 主页推荐界面
 */
public class HomeRecommendFragment extends RxLazyFragment
{

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    private List<RecommendInfo.ResultBean> results = new ArrayList<>();

    private List<BannerEntity> banners = new ArrayList<>();

    private List<RecommendBannerInfo.DataBean> recommendBanners = new ArrayList<>();

    private boolean mIsRefreshing = false;

    private SectionedRecyclerViewAdapter mSectionedAdapter;

    public static HomeRecommendFragment newInstance()
    {

        return new HomeRecommendFragment();
    }

    @Override
    public int getLayoutResId()
    {

        return R.layout.fragment_home_recommend;
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

        mSectionedAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {

            @Override
            public int getSpanSize(int position)
            {

                switch (mSectionedAdapter.getSectionItemViewType(position))
                {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;

                    case SectionedRecyclerViewAdapter.VIEW_TYPE_FOOTER:
                        return 2;

                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mSectionedAdapter);
        setRecycleNoScroll();
    }

    protected void initRefreshLayout()
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

    @Override
    public void loadData()
    {

        RetrofitHelper.getBiliAppAPI()
                .getRecommendedBannerInfo()
                .compose(bindToLifecycle())
                .map(RecommendBannerInfo::getData)
                .flatMap(new Func1<List<RecommendBannerInfo.DataBean>,Observable<RecommendInfo>>()
                {

                    @Override
                    public Observable<RecommendInfo> call(List<RecommendBannerInfo.DataBean> dataBeans)
                    {

                        recommendBanners.addAll(dataBeans);
                        return RetrofitHelper.getBiliAppAPI().getRecommendedInfo();
                    }
                })
                .compose(bindToLifecycle())
                .map(RecommendInfo::getResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {
                    results.addAll(resultBeans);
                    finishTask();
                }, throwable -> {
                    initEmptyView();
                });
    }

    /**
     * 设置轮播banners
     */
    private void convertBanner()
    {

        Observable.from(recommendBanners)
                .compose(bindToLifecycle())
                .forEach(dataBean -> banners.add(new BannerEntity(dataBean.getValue(),
                        dataBean.getTitle(), dataBean.getImage())));
    }

    protected void finishTask()
    {

        mSwipeRefreshLayout.setRefreshing(false);
        mIsRefreshing = false;
        hideEmptyView();
        convertBanner();
        mSectionedAdapter.addSection(new HomeRecommendBannerSection(banners));

        int size = results.size();
        for (int i = 0; i < size; i++)
        {
            String type = results.get(i).getType();
            if (!TextUtils.isEmpty(type))
                switch (type)
                {
//                    case ConstantUtil.TYPE_TOPIC:
//                        //话题
//                        mSectionedAdapter.addSection(new HomeRecommendTopicSection(getActivity(),
//                                results.get(i).getBody().get(0).getCover(),
//                                results.get(i).getBody().get(0).getTitle(),
//                                results.get(i).getBody().get(0).getParam()));
//                        break;
//                    case ConstantUtil.TYPE_ACTIVITY_CENTER:
//                        //活动中心
//                        mSectionedAdapter.addSection(new HomeRecommendActivityCenterSection(
//                                getActivity(),
//                                results.get(i).getBody()));
//                        break;
                    default:
//                        mSectionedAdapter.addSection(new HomeRecommendedSection(
//                                getActivity(),
//                                results.get(i).getHead().getTitle(),
//                                results.get(i).getType(),
//                                results.get(1).getHead().getCount(),
//                                results.get(i).getBody()));
                        break;
                }

            String style = results.get(i).getHead().getStyle();
//            if (style.equals(ConstantUtil.STYLE_PIC))
//            {
//                mSectionedAdapter.addSection(new HomeRecommendPicSection(getActivity(),
//                        results.get(i).getBody().get(0).getCover(),
//                        results.get(i).getBody().get(0).getParam()));
//            }
        }
        mSectionedAdapter.notifyDataSetChanged();
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


    private void clearData()
    {

        banners.clear();
        recommendBanners.clear();
        results.clear();
        mIsRefreshing = true;
        mSectionedAdapter.removeAllSections();
    }


    private void setRecycleNoScroll()
    {

        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }

    @Override
    public void onGlobalThemeChange() {
        super.onGlobalThemeChange();
        mSwipeRefreshLayout.setColorSchemeColors(ThemeUtils.getColorById(getActivity(),R.color.theme_color_primary));
    }
}
