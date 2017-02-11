package com.github.bigexcalibur.herovideo.mvp.home.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.bangumi.ui.HomeBangumiFragment;
import com.github.bigexcalibur.herovideo.mvp.live.ui.HomeLiveFragment;
import com.github.bigexcalibur.herovideo.mvp.recommend.ui.HomeRecommendFragment;
import com.github.bigexcalibur.herovideo.mvp.region.ui.HomeRegionFragment;


/**
 * 主界面Fragment模块Adapter
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES;

    private Fragment[] fragments;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLES = context.getResources().getStringArray(R.array.sections);
        fragments = new Fragment[TITLES.length];
    }

    @Override
    public Fragment getItem(int position) {

        if (fragments[position] == null) {
            switch (position) {

                case 0:
                    fragments[position] = HomeLiveFragment.newIntance();
                    break;
                case 1:
                    fragments[position] = HomeRecommendFragment.newInstance();
                    break;
                case 2:
                    fragments[position] = HomeBangumiFragment.newInstance();
                    break;
                case 3:
                    fragments[position] = HomeRegionFragment.newInstance();
                    break;

                default:
                    break;
            }
        }


        return fragments[position];
    }

    @Override
    public int getCount() {

        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return TITLES[position];
    }
}
