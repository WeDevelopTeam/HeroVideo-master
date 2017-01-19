package com.github.bigexcalibur.herovideo.mvp.common.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.common.presenter.RxBaseViewPresenter;
import com.github.bigexcalibur.herovideo.mvp.common.view.IRxBaseView;
import com.github.bigexcalibur.herovideo.ui.widget.ThemePickDialog;
import com.github.bigexcalibur.herovideo.util.LogUtil;
import com.github.bigexcalibur.herovideo.util.ThemeHelper;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Xie.Zhou on 2017/1/3.
 */

public abstract class RxBaseActivity extends RxAppCompatActivity implements ThemePickDialog.ClickListener, IRxBaseView {

    private Unbinder bind;
    private RxBaseViewPresenter mRxBaseViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局内容
        setContentView(getLayoutId());
        //初始化黄油刀控件绑定框架
        bind = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
        initToolbar();
        //初始化Presenter
        initPresenter();
        // 初始化主题
        initTheme();
    }

    protected void initTheme(){
        //初始化RxBus事件监听
        mRxBaseViewPresenter.initThemeChangeSubscription();
        mRxBaseViewPresenter.onInitThemeChange();
    }

    protected void initPresenter(){
        mRxBaseViewPresenter = new RxBaseViewPresenter(this);
    }

    public abstract int getLayoutId();

    public abstract void initViews(Bundle savedInstanceState);

    public abstract void initToolbar();

    @Override
    public void loadData() {

    }

    @Override
    public void FinishLoadData(){

    }

    @Override
    public void onNodata() {

    }

    @Override
    public void onNetDisConnected() {

    }

    @Override
    public void onInitThemeChange() {

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ThemeUtils.getColorById(this, R.color.theme_color_primary_dark));
            ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(null, null, ThemeUtils.getThemeAttrColor(this, android.R.attr.colorPrimary));
            setTaskDescription(description);
        }
    }

    // MagicSakura 主题切换

    @Override
    public void onConfirm(int currentTheme) {
        LogUtil.d("onConfirm = " + currentTheme);

        if (ThemeHelper.getTheme(RxBaseActivity.this) != currentTheme) {
            ThemeHelper.setTheme(RxBaseActivity.this, currentTheme);
            ThemeUtils.refreshUI(RxBaseActivity.this, new ThemeUtils.ExtraRefreshable() {
                        @Override
                        public void refreshGlobal(Activity activity) {
                            //for global setting, just do once
                            if (Build.VERSION.SDK_INT >= 21) {
                                final RxBaseActivity context = RxBaseActivity.this;
                                ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null, null, ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                                setTaskDescription(taskDescription);
                                getWindow().setStatusBarColor(ThemeUtils.getColorById(context, R.color.theme_color_primary_dark));
                            }
                            // post主题切换的消息,方便Fragment等完成主题切换
                            mRxBaseViewPresenter.onGlobalThemeChange();
                        }

                        @Override
                        public void refreshSpecificView(View view) {
                            //TODO: will do this for each traversal
                            // post主题切换的消息
                            mRxBaseViewPresenter.onSpecificThemeChange(view);
                        }
                    }
            );
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        mRxBaseViewPresenter.onDestroyView();
    }

    @Override
    public void onGlobalThemeChange() {

    }

    @Override
    public void onSpecificThemeChange(View view) {

    }
}
