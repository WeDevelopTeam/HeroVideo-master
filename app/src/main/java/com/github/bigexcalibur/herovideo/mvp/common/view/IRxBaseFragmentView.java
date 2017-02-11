package com.github.bigexcalibur.herovideo.mvp.common.view;

/**
 * Created by Xie.Zhou on 2017/1/18.
 */

public interface IRxBaseFragmentView extends IRxBaseView {
    void onLazyLoad();

    void onInvisible();
}
