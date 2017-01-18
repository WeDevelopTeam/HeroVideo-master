package com.github.bigexcalibur.herovideo.mvp.test.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Xie.Zhou on 2017/1/18.
 */

public class Test2Fragment extends RxLazyFragment {
    @BindView(R.id.et_test)
    EditText mEtTest;
    @BindView(R.id.btn_test2)
    Button mBtnTest2;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test2;
    }

    @Override
    public void finishCreateView(Bundle state) {

    }

    @OnClick(R.id.btn_test2)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_test2:
                String av = mEtTest.getText().toString();
                RetrofitHelper.
                        getBilibiliVideo().
                        getBiliAVSearchHtml(av);
//                        .compose(bindToLifecycle()).map;

                break;

        }
    }
}
