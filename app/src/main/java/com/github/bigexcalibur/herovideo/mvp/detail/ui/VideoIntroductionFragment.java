package com.github.bigexcalibur.herovideo.mvp.detail.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.mvp.detail.model.VideoDetailsInfo;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.util.ConstantUtil;
import com.github.bigexcalibur.herovideo.util.LogUtil;
import com.github.bigexcalibur.herovideo.util.NumberUtil;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 视频简介界面
 */
public class VideoIntroductionFragment extends RxLazyFragment {

  @BindView(R.id.tv_title)
  TextView mTitleText;

  @BindView(R.id.tv_play_time)
  TextView mPlayTimeText;

  @BindView(R.id.tv_review_count)
  TextView mReviewCountText;

  @BindView(R.id.tv_description)
  TextView mDescText;

  @BindView(R.id.share_num)
  TextView mShareNum;

  @BindView(R.id.coin_num)
  TextView mCoinNum;

  @BindView(R.id.fav_num)
  TextView mFavNum;

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;

  private String av;

  private VideoDetailsInfo.DataBean mVideoDetailsInfo;


  public static VideoIntroductionFragment newInstance(String aid) {

    VideoIntroductionFragment fragment = new VideoIntroductionFragment();
    Bundle bundle = new Bundle();
    bundle.putString(ConstantUtil.EXTRA_AV, aid);
    fragment.setArguments(bundle);
    return fragment;
  }


  @Override
  public int getLayoutResId() {

    return R.layout.fragment_video_introduction;
  }


  @Override
  public void finishCreateView(Bundle state) {

    av = getArguments().getString(ConstantUtil.EXTRA_AV);
    loadData();
  }


  @Override
  public void loadData() {
    LogUtil.test("loadData");
    RetrofitHelper.getBiliAppAPI()
        .getVideoDetails(av)
        .compose(this.bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(videoDetails -> {
          mVideoDetailsInfo = videoDetails.getData();
          LogUtil.test(" videoDetails title = " +mVideoDetailsInfo.getTitle());
          finishTask();
        }, throwable -> {
          LogUtil.test("error");
        });
  }


  public void finishTask() {

    //设置视频标题
    mTitleText.setText(mVideoDetailsInfo.getTitle());
    //设置视频播放数量
    mPlayTimeText.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getView()));
    //设置视频弹幕数量
    mReviewCountText.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getDanmaku()));
    //设置Up主信息
    mDescText.setText(mVideoDetailsInfo.getDesc());
    //设置分享 收藏 投币数量
    mShareNum.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getShare()));
    mFavNum.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getFavorite()));
    mCoinNum.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getCoin()));

  }



  @OnClick(R.id.btn_share)
  void share() {

    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
    intent.putExtra(Intent.EXTRA_TEXT, "来自「哔哩哔哩」的分享:" + mVideoDetailsInfo.getDesc());
    startActivity(Intent.createChooser(intent, mVideoDetailsInfo.getTitle()));
  }
}
