package com.github.bigexcalibur.herovideo.mvp.common.presenter;

import com.github.bigexcalibur.herovideo.mvp.common.view.IRxBaseFragmentView;

/**
 * Created by Xie.Zhou on 2017/1/18.
 */

public class RxBaseFragmentViewPresenter extends RxBaseViewPresenter {

    private IRxBaseFragmentView mIRxBaseFragmentView;

    protected RxBaseFragmentViewPresenter(){}

    public RxBaseFragmentViewPresenter(IRxBaseFragmentView iRxBaseFragmentView){
        super(iRxBaseFragmentView);
        this.mIRxBaseFragmentView = iRxBaseFragmentView;
    }

}
