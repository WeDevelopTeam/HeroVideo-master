package com.github.bigexcalibur.herovideo.mvp.detail.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxBaseActivity;
import com.github.bigexcalibur.herovideo.mvp.detail.model.VideoDetailsInfo;
import com.github.bigexcalibur.herovideo.mvp.test.ui.Test2Fragment;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.network.auxiliary.UrlHelper;
import com.github.bigexcalibur.herovideo.util.ConstantUtil;
import com.github.bigexcalibur.herovideo.util.DisplayUtil;
import com.github.bigexcalibur.herovideo.util.LogUtil;
import com.github.bigexcalibur.herovideo.util.helper.SystemBarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 视频详情界面
 */
public class VideoDetailsActivity extends RxBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.video_preview)
    ImageView mVideoPreview;

    @BindView(R.id.tab_layout)
    SlidingTabLayout mSlidingTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.fab)
    FloatingActionButton mFAB;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.tv_player)
    TextView mTvPlayer;

    @BindView(R.id.tv_av)
    TextView mAvText;

    private List<Fragment> fragments = new ArrayList<>();

    private List<String> titles = new ArrayList<>();

    private String av;

    private String imgUrl;

    private VideoDetailsInfo.DataBean mVideoDetailsInfo;
    private VideoIntroductionFragment mMVideoIntroductionFragment;


    @Override
    public int getLayoutId() {

        return R.layout.activity_video_details;
    }


    @Override
    public void initViews(Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (intent != null) {
            av = intent.getStringExtra(ConstantUtil.EXTRA_AV);
            imgUrl = intent.getStringExtra(ConstantUtil.EXTRA_IMG_URL);
        }

    Glide.with(VideoDetailsActivity.this)
        .load(UrlHelper.getClearVideoPreviewUrl(imgUrl))
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.bili_default_image_tv)
        .dontAnimate()
        .into(mVideoPreview);

        loadData();

        mFAB.setClickable(false);
        mFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        mFAB.setTranslationY(-getResources().getDimension(R.dimen.floating_action_button_size_half));
        mFAB.setOnClickListener(v -> {
            mMVideoIntroductionFragment.playVideo();
        });

        mAppBarLayout.addOnOffsetChangedListener(
                (appBarLayout, verticalOffset) -> setViewsTranslation(verticalOffset));
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeEvent() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {

                if (state == State.EXPANDED) {
                    //展开状态
                    mTvPlayer.setVisibility(View.GONE);
                    mAvText.setVisibility(View.VISIBLE);
                    mToolbar.setContentInsetsRelative(DisplayUtil.dp2px(VideoDetailsActivity.this, 15), 0);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mTvPlayer.setVisibility(View.VISIBLE);
                    mAvText.setVisibility(View.GONE);
                    mToolbar.setContentInsetsRelative(DisplayUtil.dp2px(VideoDetailsActivity.this, 150), 0);
                } else {
                    mTvPlayer.setVisibility(View.GONE);
                    mAvText.setVisibility(View.VISIBLE);
                    mToolbar.setContentInsetsRelative(DisplayUtil.dp2px(VideoDetailsActivity.this, 15), 0);
                }
            }
        });
    }

    @Override
    protected void initStatusBar() {
        //设置StatusBar透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            SystemBarHelper.immersiveStatusBar(this);
            SystemBarHelper.setHeightAndPadding(this, mToolbar);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initToolbar() {

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        //设置收缩后Toolbar上字体的颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        mAvText.setText("av" + av);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//    getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewsTranslation(int target) {

        mFAB.setTranslationY(target);
        if (target == 0) {
            showFAB();
        } else if (target < 0) {
            hideFAB();
        }
    }


    public static void launch(Activity activity, String aid, String imgUrl) {

        Intent intent = new Intent(activity, VideoDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ConstantUtil.EXTRA_AV, aid);
        intent.putExtra(ConstantUtil.EXTRA_IMG_URL, imgUrl);
        activity.startActivity(intent);
    }


    private void showFAB() {

        mFAB.animate().scaleX(1f).scaleY(1f)
                .setInterpolator(new OvershootInterpolator())
                .start();

        mFAB.setClickable(true);
    }


    private void hideFAB() {

        mFAB.animate().scaleX(0f).scaleY(0f)
                .setInterpolator(new AccelerateInterpolator())
                .start();

        mFAB.setClickable(false);
    }


    @Override
    public void loadData() {

        RetrofitHelper.getBiliAppAPI()
                .getVideoDetails(av)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoDetails -> {

                    mVideoDetailsInfo = videoDetails.getData();
                    LogUtil.test(" VideoDetails finishTask" + mVideoDetailsInfo.getTitle());
                    finishTask();
                }, throwable -> {

                    mFAB.setClickable(false);
                    mFAB.setBackgroundTintList(ColorStateList.valueOf(
                            getResources().getColor(R.color.gray_20)));
                });
    }


    public void finishTask() {

        mFAB.setClickable(true);
        mFAB.setBackgroundTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.theme_color_primary)));
        mCollapsingToolbarLayout.setTitle("");
        mMVideoIntroductionFragment = VideoIntroductionFragment.newInstance(av);
//    VideoCommentFragment mVieoCommentFragment = VideoCommentFragment.newInstance(av);
        Test2Fragment mVideoCommentFragment = new Test2Fragment();
        fragments.add(mMVideoIntroductionFragment);
        fragments.add(mVideoCommentFragment);

        setPagerTitle(String.valueOf(mVideoDetailsInfo.getStat().getReply()));

    }

    private void setPagerTitle(String num) {

        titles.add("简介");
        titles.add("评论" + "(" + num + ")");
        LogUtil.test(" fragments = " + fragments.size());
        VideoDetailsPagerAdapter mAdapter = new VideoDetailsPagerAdapter(getSupportFragmentManager(),
                fragments, titles);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mSlidingTabLayout.setViewPager(mViewPager);
        measureTabLayoutTextWidth(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {

                measureTabLayoutTextWidth(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void measureTabLayoutTextWidth(int position) {

        String title = titles.get(position);
        TextView titleView = mSlidingTabLayout.getTitleView(position);
        TextPaint paint = titleView.getPaint();
        float textWidth = paint.measureText(title);
        mSlidingTabLayout.setIndicatorWidth(textWidth / 3);
    }


    public static class VideoDetailsPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        private List<String> titles;


        VideoDetailsPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {

            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }


        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }


        @Override
        public int getCount() {

            return fragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return titles.get(position);
        }
    }
}
