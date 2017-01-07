package com.github.bigexcalibur.herovideo.mvp.common.view;

/**
 * Created by Xie.Zhou on 2017/1/6.
 */

public interface RxBaseView {

    void loadData();

    void showProgressBar() ;

    void hideProgressBar() ;

    void reloadData();

    void noData();

    void noNetConnnect();

}
