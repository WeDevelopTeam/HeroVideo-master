package com.github.bigexcalibur.herovideo.model.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mediaplayer.VideoPlayerActivity;

/**
 * Created by Xie.Zhou on 2017/1/4.
 */

public class TestFragment extends Fragment {

    private TextView mTv_test;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mTv_test = (TextView) view.findViewById(R.id.tv_test);
        mTv_test.setOnClickListener(v -> {

        });
        view.findViewById(R.id.btn_test1).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VideoPlayerActivity.class));
        });
    }

    public void setText(String text){
        mTv_test.setText(text);
    }
}
