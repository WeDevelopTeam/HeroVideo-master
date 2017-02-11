package com.github.bigexcalibur.herovideo.mvp.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.mvp.detail.ui.VideoDetailsActivity;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.util.ConstantUtil;
import com.github.bigexcalibur.herovideo.util.LogUtil;
import com.github.bigexcalibur.herovideo.util.Md5;
import com.github.bigexcalibur.herovideo.util.ToastUtil;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.github.bigexcalibur.herovideo.network.auxiliary.ApiConstants.SECRETKEY_MINILOADER;

/**
 * Created by Xie.Zhou on 2017/1/18.
 */

public class Test2Fragment extends RxLazyFragment {
    @BindView(R.id.et_test)
    EditText mEtTest;
    @BindView(R.id.btn_test2)
    Button mBtnTest2;
    @BindView(R.id.listview)
    ListView mListview;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test2;
    }

    @Override
    public void finishCreateView(Bundle state) {

    }
    //    852476
    //    1064641

    @OnClick(R.id.btn_test2)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test2:
                String av = mEtTest.getText().toString();
                RetrofitHelper.getBiliAVSearchAPI()
                        .getBiliAVSearchHtml(av)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map(this::parseSearchUrl)
                        .map(map -> RetrofitHelper.getBiliAVVideoAPI().getBiliAVVideoHtml(map))
                        .subscribe(new Observer<Observable<ResponseBody>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.ShortToast("Error");
                            }

                            @Override
                            public void onNext(Observable<ResponseBody> responseBodyObservable) {
                                responseBodyObservable.map(Test2Fragment.this::parseVideoUrl)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<ArrayList<String>>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                ToastUtil.ShortToast("Error");
                                            }

                                            @Override
                                            public void onNext(ArrayList<String> urls) {
                                                mListview.setAdapter(new BaseAdapter() {
                                                    @Override
                                                    public int getCount() {
                                                        return urls.size();
                                                    }

                                                    @Override
                                                    public Object getItem(int position) {
                                                        return null;
                                                    }

                                                    @Override
                                                    public long getItemId(int position) {
                                                        return 0;
                                                    }

                                                    @Override
                                                    public View getView(int position, View convertView, ViewGroup parent) {
                                                        convertView = getActivity().getLayoutInflater().inflate(R.layout.item_testlist, null);
                                                        TextView tv_item_test = (TextView) convertView.findViewById(R.id.tv_item_test);
                                                        tv_item_test.setText("" + position);
                                                        return convertView;
                                                    }
                                                });
//                                                mListview.setOnItemClickListener((parent, view1, position, id) -> MediaPlayerActivity.configPlayer(getActivity()).setTitle(urls.get(position)).play(urls.get(position)));
                                                mListview.setOnItemClickListener((parent, view1, position, id) ->{
                                                    Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
                                                    intent.putExtra(ConstantUtil.EXTRA_URL,urls.get(position));
                                                    intent.putExtra(ConstantUtil.EXTRA_AV,aid);
                                                    startActivity(intent);
                                                });
                                            }
                                        });
                            }
                        });

                break;

        }
    }

    String aid;

    public Map<String, String> parseSearchUrl(ResponseBody responseBody) {
        String body;
        try {
            body = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // 创建 Pattern 对象
        Pattern r = Pattern.compile("cid=([^&]+)");
        // 现在创建 matcher 对象
        Matcher m = r.matcher(body);
        String cid;
        if (m.find()) {
            cid = m.group(1);
        } else {
            ToastUtil.ShortToast("未找到cid");
            return null;
        }

        // 创建 Pattern 对象
        Pattern r2 = Pattern.compile("aid=([^\"&]+)");
        // 现在创建 matcher 对象
        Matcher m2 = r2.matcher(body);
        if (m2.find()) {
            aid = m2.group(1);
        } else {
            ToastUtil.ShortToast("未找到aid");
            return null;
        }

        LogUtil.test("cid = " + cid +"  aid = " +aid);
        String sign = Md5.strToMd5Low32("cid=" + cid + "&from=miniplay&player=1" + SECRETKEY_MINILOADER);
        Map<String, String> map = new HashMap<>();
        map.put("cid", cid);
        map.put("from", "miniplay");
        map.put("player", "1");
        map.put("sign", sign);

        // 真实的视频源地址请求url
        LogUtil.test("video request url = " + "http://interface.bilibili.com/playurl?&cid=" + cid + "&from=miniplay&player=1" + "&sign=" + sign);
        return map;
    }

    public ArrayList<String> parseVideoUrl(ResponseBody responseBody) {
        String xml = null;
        try {
            xml = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("xml", xml);
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        assert document != null;
        Element rootElement = document.getRootElement();
        List<Element> elementList = rootElement.elements("durl");

        ArrayList<String> urlList = new ArrayList<>();

        for (Element element : elementList) {
            Element urlElement = element.element("url");
            String url = urlElement.getText();
            LogUtil.test("video play url = " + url);
            urlList.add(url);
        }

        return urlList;
    }
}
