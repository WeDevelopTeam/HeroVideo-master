package com.github.bigexcalibur.herovideo.mvp.common.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.bigexcalibur.herovideo.mvp.common.presenter.RxBaseFragmentViewPresenter;
import com.github.bigexcalibur.herovideo.mvp.common.view.IRxBaseFragmentView;
import com.github.bigexcalibur.herovideo.rxbus.event.ThemeChangeEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 */
public abstract class RxLazyFragment extends RxFragment implements IRxBaseFragmentView
{

    private View parentView;

    private FragmentActivity activity;

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;

    private Unbinder bind;
    private RxBaseFragmentViewPresenter rxBaseFragmentViewPresenter;

    public abstract @LayoutRes int getLayoutResId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getSupportActivity();
        return parentView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        finishCreateView(savedInstanceState);
        rxBaseFragmentViewPresenter = new RxBaseFragmentViewPresenter(this);
        initThemeChangeObserver();
    }

    private void initThemeChangeObserver(){
        rxBaseFragmentViewPresenter.initThemeChangeSubscription();
        rxBaseFragmentViewPresenter.onGlobalThemeChange();
    }

    public abstract void finishCreateView(Bundle state);

    public void onThemeChange(ThemeChangeEvent themeChangeEvent){

    }

    @Override
    public void onResume()
    {

        super.onResume();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        bind.unbind();
        rxBaseFragmentViewPresenter.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity)
    {

        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    @Override
    public void onDetach()
    {

        super.onDetach();
        this.activity = null;
    }

    public FragmentActivity getSupportActivity()
    {

        return super.getActivity();
    }

    public android.app.ActionBar getSupportActionBar()
    {

        return getSupportActivity().getActionBar();
    }

    public Context getApplicationContext()
    {

        return this.activity == null ? (getActivity() == null ? null :
                getActivity().getApplicationContext()) : this.activity.getApplicationContext();
    }


    /**
     * Fragment数据的懒加载
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {

        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint())
        {
            isVisible = true;
            onVisible();
        } else
        {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible()
    {
        onLazyLoad();
    }


    @SuppressWarnings("unchecked")
    public <T extends View> T $(int id)
    {

        return (T) parentView.findViewById(id);
    }

    @Override
    public void onSpecificThemeChange(View view) {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void FinishLoadData() {

    }

    @Override
    public void onNodata() {

    }

    @Override
    public void onNetDisConnected() {

    }

    @Override
    public void onGlobalThemeChange() {

    }

    @Override
    public void onLazyLoad() {

    }

    @Override
    public void onInvisible() {

    }
}
