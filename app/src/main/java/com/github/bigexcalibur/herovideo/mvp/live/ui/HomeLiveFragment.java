package com.github.bigexcalibur.herovideo.mvp.live.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.bigexcalibur.herovideo.LiveAppIndexAdapter;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.ui.widget.CustomEmptyView;
import com.github.bigexcalibur.herovideo.util.SnackbarUtil;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页直播界面
 */
public class HomeLiveFragment extends RxLazyFragment
{

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    private LiveAppIndexAdapter mLiveAppIndexAdapter;


    public static HomeLiveFragment newIntance()
    {

        return new HomeLiveFragment();
    }


    @Override
    public int getLayoutResId()
    {

        return R.layout.fragment_home_live;
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

        mLiveAppIndexAdapter = new LiveAppIndexAdapter(getActivity());
        mRecyclerView.setAdapter(mLiveAppIndexAdapter);
        GridLayoutManager layout = new GridLayoutManager(getActivity(), 12);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {

            @Override
            public int getSpanSize(int position)
            {

                return mLiveAppIndexAdapter.getSpanSize(position);
            }
        });

        mRecyclerView.setLayoutManager(layout);
    }

    protected void initRefreshLayout()
    {

        mSwipeRefreshLayout.setColorSchemeResources(R.color.theme_color_primary);
        mSwipeRefreshLayout.setOnRefreshListener(this::loadData);
        mSwipeRefreshLayout.post(() -> {

            mSwipeRefreshLayout.setRefreshing(true);
            loadData();
        });
    }

    @Override
    public void loadData()
    {

        RetrofitHelper.getLiveAPI()
                .getLiveAppIndex()
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(liveAppIndexInfo -> {

                    mLiveAppIndexAdapter.setLiveInfo(liveAppIndexInfo);
                    finishTask();
                }, throwable -> {
                    initEmptyView();
                });
    }

    private void initEmptyView()
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


    @Override
    public void finishTask()
    {

        hideEmptyView();
        mSwipeRefreshLayout.setRefreshing(false);
        mLiveAppIndexAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }
}
