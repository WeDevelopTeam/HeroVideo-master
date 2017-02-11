package com.github.bigexcalibur.herovideo.mvp.common.presenter;

import android.view.View;

import com.github.bigexcalibur.herovideo.mvp.common.view.IRxBaseView;
import com.github.bigexcalibur.herovideo.rxbus.RxBus;
import com.github.bigexcalibur.herovideo.rxbus.event.ThemeChangeEvent;

import rx.Subscription;

/**
 * Created by Xie.Zhou on 2017/1/18.
 */

public class RxBaseViewPresenter {
    private IRxBaseView mIRxBaseView;
    private Subscription mThemeChangeSubscription;

    protected RxBaseViewPresenter(){
    }

    public RxBaseViewPresenter(IRxBaseView IRxBaseView) {
        this.mIRxBaseView = IRxBaseView;
    }

    public void onInitThemeChange() {
        // 发送MagicaSacura初始化的事件
        RxBus.getInstance().post(new ThemeChangeEvent(ThemeChangeEvent.INIT_CHANGE));
    }

    public void onGlobalThemeChange() {
        RxBus.getInstance().post(new ThemeChangeEvent(ThemeChangeEvent.GLOBLE_CHANGE));
    }

    public void onSpecificThemeChange(View view) {
        RxBus.getInstance().post(new ThemeChangeEvent(ThemeChangeEvent.SPECIFIC_CHANGE, view));
    }

    public void initThemeChangeSubscription() {
        mThemeChangeSubscription = RxBus.getInstance().toObserverable(ThemeChangeEvent.class).subscribe(this::onThemeChange);
    }

    public void onThemeChange(ThemeChangeEvent themeChangeEvent) {
        switch (themeChangeEvent.eventType) {
            case ThemeChangeEvent.INIT_CHANGE:
            case ThemeChangeEvent.GLOBLE_CHANGE:
                mIRxBaseView.onGlobalThemeChange();
                break;
            case ThemeChangeEvent.SPECIFIC_CHANGE:
                mIRxBaseView.onSpecificThemeChange(themeChangeEvent.view);
                break;
        }
    }

    public void onDestroyView() {
        if (mThemeChangeSubscription != null && !mThemeChangeSubscription.isUnsubscribed()) {
            mThemeChangeSubscription.unsubscribe();
        }
    }
}
