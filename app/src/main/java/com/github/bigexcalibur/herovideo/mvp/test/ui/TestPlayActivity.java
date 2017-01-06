package com.github.bigexcalibur.herovideo.mvp.test.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mediaplayer.VideoPlayerView;

import static android.provider.CalendarContract.CalendarCache.URI;

/**
 * Created by Xie.Zhou on 2017/1/4.
 */

public class TestPlayActivity extends AppCompatActivity {

    private VideoPlayerView mVideoPlayerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    private void init() {
        mVideoPlayerView = (VideoPlayerView) findViewById(R.id.playerView);
        mVideoPlayerView.setVideoURI(URI.parse("http://cdn.hotcast.cn/import%2F20161226%2Fuhd%2Fyzc20161223.mp4"));
        mVideoPlayerView.start();
    }
}
