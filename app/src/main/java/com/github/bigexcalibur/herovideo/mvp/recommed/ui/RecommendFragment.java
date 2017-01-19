package com.github.bigexcalibur.herovideo.mvp.recommed.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;

import butterknife.BindView;

/**
 * Created by Xie.Zhou on 2017/1/18.
 */

public class RecommendFragment extends RxLazyFragment {
    @BindView(R.id.test_text1)
    TextView mTestText1;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void finishCreateView(Bundle state) {
        loadData();
    }

    @Override
    public void loadData() {
        super.loadData();
    }
}
