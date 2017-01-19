package com.github.bigexcalibur.herovideo.mvp.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mediaplayer.VideoPlayerActivity;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.network.auxiliary.ApiConstants;
import com.github.bigexcalibur.herovideo.rxbus.event.ThemeChangeEvent;
import com.github.bigexcalibur.herovideo.util.LogUtil;
import com.github.bigexcalibur.herovideo.util.Md5;
import com.github.bigexcalibur.herovideo.util.ToastUtil;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.github.bigexcalibur.herovideo.network.auxiliary.ApiConstants.SECRETKEY_MINILOADER;

/**
 * Created by Xie.Zhou on 2017/1/4.
 */

public class TestFragment extends RxLazyFragment {

    @BindView(R.id.tv_test)
    TextView mTvTest;
    @BindView(R.id.btn_test1)
    Button mBtnTest1;
    @BindView(R.id.btn_test2)
    Button mBtnTest2;
    @BindView(R.id.et_test)
    EditText mEtTest;

    private TextView mTv_test;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mTvTest.setOnClickListener(v -> {

        });

        mBtnTest1.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VideoPlayerActivity.class));
        });

        mBtnTest2.setOnClickListener(v -> {
            Editable av_num = mEtTest.getText();
            if (TextUtils.isEmpty(av_num)){
                ToastUtil.showLong(getActivity(),"输入为空");
            }else {
                startBilibiliVideo(av_num);
            }
        });


    }

    public void setText(String text) {
        mTv_test.setText(text);
    }

    @Override
    public void onThemeChange(ThemeChangeEvent themeChangeEvent) {
        super.onThemeChange(themeChangeEvent);
        switch (themeChangeEvent.eventType) {
            case ThemeChangeEvent.GLOBLE_CHANGE:
            case ThemeChangeEvent.INIT_CHANGE:
                mBtnTest2.setBackgroundColor(ThemeUtils.getColorById(getActivity(), R.color.theme_color_primary));
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    private void startBilibiliVideo(Editable av_num) {
        String url = ApiConstants.VIDEO_SEARCH_HEAD +"av" +av_num;
        Request request = new Request.Builder().url(url).build();
        RetrofitHelper.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                LogUtil.d("test", response.body().string());
                String content = response.body().string();

                // 创建 Pattern 对象
                Pattern r = Pattern.compile("cid=([^&]+)");

                // 现在创建 matcher 对象
                Matcher m = r.matcher(content);
                String cid = "";
                if (m.find( )) {
                    cid = m.group(1);
                } else {
                    ToastUtil.ShortToast("未找到资源");
                    return;
                }

                String sign = Md5.strToMd5Low32("cid="+cid+"&from=miniplay&player=1"+SECRETKEY_MINILOADER);
                LogUtil.test(sign);

                String url2 = "http://interface.bilibili.com/playurl?cid=" + cid + "&from=miniplay&player=1" + "&sign=" + sign;
                LogUtil.test(url2);
                Request request2 = new Request.Builder().url(url2).build();
                RetrofitHelper.getOkHttpClient().newCall(request2).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String content = response.body().string();
                        LogUtil.d("html",content);
                        // 创建 Pattern 对象
                        Pattern r = Pattern.compile("http([^><]+)");
                        // 现在创建 matcher 对象
                        Matcher m = r.matcher(content);
                        String str = "";
                        if (m.find( )) {
                            LogUtil.d("test"," m.group(0) = " +m.group(0));
                            LogUtil.d("test"," m.group(1) = " +m.group(1));
                            str = m.group(0);
                        } else {
                            ToastUtil.ShortToast("未找到资源");
                            return;
                        }
                        String[] split = str.split("]]");
                        Intent intent = new Intent(getActivity(),VideoPlayerActivity.class);
                        intent.putExtra("url",split[0]);
                        getActivity().startActivity(intent);
                    }
                });
            }
        });

    }

}
