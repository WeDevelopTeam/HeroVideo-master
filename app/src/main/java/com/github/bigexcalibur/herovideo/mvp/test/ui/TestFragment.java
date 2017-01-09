package com.github.bigexcalibur.herovideo.mvp.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mediaplayer.VideoPlayerActivity;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.rxbus.event.ThemeChangeEvent;

import butterknife.BindView;

/**
 * Created by Xie.Zhou on 2017/1/4.
 */

public class TestFragment extends RxLazyFragment {

    @BindView(R.id.tv_test)
    TextView mTvTest;
    @BindView(R.id.btn_test1)
    Button mBtnTest1;
    @BindView(R.id.btn_test2)
    Button mBtnTest2;
    private TextView mTv_test;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Override
    public void finishCreateView(Bundle state) {

        mTvTest.setOnClickListener(v -> {

        });

        mBtnTest1.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VideoPlayerActivity.class));
        });
    }

    public void setText(String text) {
        mTv_test.setText(text);
    }

    @Override
    public void onThemeChange(ThemeChangeEvent themeChangeEvent) {
        super.onThemeChange(themeChangeEvent);
        if (themeChangeEvent.eventType == ThemeChangeEvent.GLOBLE_CHANGE||themeChangeEvent.eventType == ThemeChangeEvent.INIT_CHANGE){
            mBtnTest2.setBackgroundColor(ThemeUtils.getColorById(getActivity(),R.color.theme_color_primary));
        }
    }
}
