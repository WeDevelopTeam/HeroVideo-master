package com.github.bigexcalibur.herovideo.mediaplayer;

import android.content.Intent;
import android.os.Bundle;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mediaplayer.danmuku.PlayerManager;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxBaseActivity;

/**
 * Created by Xie.Zhou on 2017/1/19.
 */

public class AndroidPlayerActivity extends RxBaseActivity implements PlayerManager.PlayerStateListener {

    private PlayerManager player;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_player;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initPlayer();
    }

    private void initPlayer() {
        player = new PlayerManager(this);
        player.setFullScreenOnly(true);
        player.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        player.playInFullScreen(true);
        player.setPlayerStateListener(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        player.play(url);
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onPlay() {

    }
}
