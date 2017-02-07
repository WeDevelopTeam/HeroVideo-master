package com.github.bigexcalibur.herovideo.mvp.common.view;

import android.view.View;

/**
 * Created by Xie.Zhou on 2017/1/6.
 */

public interface IRxBaseView {

    void loadData();

    void FinishLoadData();

    void onNodata();

    void onNetDisConnected();

    void onGlobalThemeChange();

    void onSpecificThemeChange(View view);

    void finishTask();
}
