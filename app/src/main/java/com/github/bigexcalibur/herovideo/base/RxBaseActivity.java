package com.github.bigexcalibur.herovideo.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.ui.widget.ThemePickDialog;
import com.github.bigexcalibur.herovideo.util.ThemeHelper;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Xie.Zhou on 2017/1/3.
 */

public abstract class RxBaseActivity extends RxAppCompatActivity implements ThemePickDialog.ClickListener {

    private Unbinder bind;

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
    }

    public abstract int getLayoutId();

    public abstract void initViews(Bundle savedInstanceState);

    public abstract void initToolbar();

    public void loadData() {
    }

    public void showProgressBar() {
    }

    public void hideProgressBar() {
    }

    public void initRecyclerView() {
    }

    public void initRefreshLayout() {
    }

    public void finishTask() {
    }

    //MagicSakura 主题切换
    @Override
    public void onConfirm(int currentTheme) {

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
                        }

                        @Override
                        public void refreshSpecificView(View view) {
                            //TODO: will do this for each traversal
                        }
                    }
            );
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
