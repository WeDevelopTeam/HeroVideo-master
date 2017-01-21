package com.github.bigexcalibur.herovideo.mvp.home.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.mvp.home.ui.adapter.HomePagerAdapter;
import com.github.bigexcalibur.herovideo.ui.widget.circle.CircleImageView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页模块主界面
 */
public class HomePageFragment extends RxLazyFragment
{

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTab;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.toolbar_user_avatar)
    CircleImageView mCircleImageView;

    public static HomePageFragment newInstance()
    {
        return new HomePageFragment();
    }


    @Override
    public int getLayoutResId()
    {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void finishCreateView(Bundle state)
    {
        setHasOptionsMenu(true);
        initToolBar();
        initSearchView();
        initViewPager();
    }

    private void initToolBar()
    {
        mToolbar.setTitle("");
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        mCircleImageView.setImageResource(R.drawable.ic_launcher);
    }

    private void initSearchView()
    {

        //初始化SearchBar
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query)
            {
//                TotalStationSearchActivity.launch(getActivity(), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {

                return false;
            }
        });
    }


    private void initViewPager()
    {

        HomePagerAdapter mHomeAdapter = new HomePagerAdapter(getChildFragmentManager(),
                getApplicationContext());
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mHomeAdapter);
        mSlidingTab.setViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

        // 设置SearchViewItemMenu
        MenuItem item = menu.findItem(R.id.id_action_search);
        mSearchView.setMenuItem(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        switch (id)
        {

            case R.id.id_action_download:
                //离线缓存
//                startActivity(new Intent(getActivity(), OffLineDownloadActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.navigation_layout)
    void toggleDrawer()
    {

        Activity activity = getActivity();
        if (activity instanceof MainActivity)
            ((MainActivity) activity).toggleDrawer();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == Activity.RESULT_OK)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0)
            {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd))
                {
                    mSearchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean isOpenSearchView()
    {

        return mSearchView.isSearchOpen();
    }

    public void closeSearchView()
    {

        mSearchView.closeSearch();
    }


    @Override
    public void onLazyLoad() {

    }

    @Override
    public void onInvisible() {

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
        mSlidingTab.setBackgroundColor(ThemeUtils.getColorById(getActivity(),R.color.theme_color_primary));
    }

    @Override
    public void onSpecificThemeChange(View view) {

    }
}
