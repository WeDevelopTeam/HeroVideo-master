package com.github.bigexcalibur.herovideo.mvp.detail.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.adapter.VideoDetaisSelectionAdapter;
import com.github.bigexcalibur.herovideo.mediaplayer.MediaPlayer;
import com.github.bigexcalibur.herovideo.mediaplayer.MediaPlayerActivity;
import com.github.bigexcalibur.herovideo.mvp.common.ui.RxLazyFragment;
import com.github.bigexcalibur.herovideo.mvp.detail.model.VideoDetailsInfo;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.util.ConstantUtil;
import com.github.bigexcalibur.herovideo.util.LogUtil;
import com.github.bigexcalibur.herovideo.util.Md5;
import com.github.bigexcalibur.herovideo.util.NumberUtil;
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
 * 视频简介界面
 */
public class VideoIntroductionFragment extends RxLazyFragment {

    @BindView(R.id.tv_title)
    TextView mTitleText;

    @BindView(R.id.tv_play_time)
    TextView mPlayTimeText;

    @BindView(R.id.tv_review_count)
    TextView mReviewCountText;

    @BindView(R.id.tv_description)
    TextView mDescText;

    @BindView(R.id.share_num)
    TextView mShareNum;

    @BindView(R.id.coin_num)
    TextView mCoinNum;

    @BindView(R.id.fav_num)
    TextView mFavNum;

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    @BindView(R.id.bangumi_selection_recycler)
    RecyclerView mBangumiSelectionRecycler;

    @BindView(R.id.tv_update_index)
    TextView mUpdateIndexText;
    private String av;

    private VideoDetailsInfo.DataBean mVideoDetailsInfo;
    private VideoDetailsActivity mVideoDetailsActivity;


    public static VideoIntroductionFragment newInstance(String aid) {

        VideoIntroductionFragment fragment = new VideoIntroductionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtil.EXTRA_AV, aid);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_video_introduction;
    }


    @Override
    public void finishCreateView(Bundle state) {

        av = getArguments().getString(ConstantUtil.EXTRA_AV);
        mVideoDetailsActivity = (VideoDetailsActivity) getActivity();
        loadData();
    }


    @Override
    public void loadData() {
        LogUtil.test("loadData");
        RetrofitHelper.getBiliAppAPI()
                .getVideoDetails(av)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoDetails -> {
                    mVideoDetailsInfo = videoDetails.getData();
                    LogUtil.test(" videoDetails title = " + mVideoDetailsInfo.getTitle());
                    finishTask();
                }, throwable -> {
                    LogUtil.test("error");
                });
    }


    public void finishTask() {

        //设置视频标题
        mTitleText.setText(mVideoDetailsInfo.getTitle());
        //设置视频播放数量
        mPlayTimeText.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getView()));
        //设置视频弹幕数量
        mReviewCountText.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getDanmaku()));
        //设置Up主信息
        mDescText.setText(mVideoDetailsInfo.getDesc());
        //设置分享 收藏 投币数量
        mShareNum.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getShare()));
        mFavNum.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getFavorite()));
        mCoinNum.setText(NumberUtil.converString(mVideoDetailsInfo.getStat().getCoin()));
        //设置集数
        parseVideoUrl(av);
    }

    /**
     * 初始化选集recyclerView
     */
    public void initSelectionRecycler(List<String> urls) {
        this.urls = urls;
        mUpdateIndexText.setText("共"+urls.size()+"分P");

        mBangumiSelectionRecycler.setHasFixedSize(false);
        mBangumiSelectionRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        mLinearLayoutManager.setReverseLayout(true);
        mBangumiSelectionRecycler.setLayoutManager(mLinearLayoutManager);
        VideoDetaisSelectionAdapter mVideoDetaisSelectionAdapter = new VideoDetaisSelectionAdapter(mBangumiSelectionRecycler, urls, "");
        mBangumiSelectionRecycler.setAdapter(mVideoDetaisSelectionAdapter);
        mVideoDetaisSelectionAdapter.notifyItemForeground(0);
        mBangumiSelectionRecycler.scrollToPosition(0);
        mVideoDetaisSelectionAdapter.setOnItemClickListener((position, holder) -> {
            mVideoDetaisSelectionAdapter.notifyItemForeground(holder.getLayoutPosition());
            selectIndex = position;
            playVideo();
        });
    }

    private List<String> urls;
    private int selectIndex = 0;

    public void playVideo(){
        if (urls!=null){
            MediaPlayerActivity.configPlayer(mVideoDetailsActivity)
                    .setTitle(av)
                    .setFullScreenOnly(true)
                    .setScaleType(MediaPlayer.SCALETYPE_FILLPARENT)
                    .play(urls.get(selectIndex));
        }
    }


    private void parseVideoUrl(String av) {
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
                        responseBodyObservable.map(VideoIntroductionFragment.this::parseVideoUrl)
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
                                        initSelectionRecycler(urls);
                                    }
                                });
                    }
                });
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

        LogUtil.test("cid = " + cid + "  aid = " + aid);
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

    @OnClick(R.id.btn_share)
    void share() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自「哔哩哔哩」的分享:" + mVideoDetailsInfo.getDesc());
        startActivity(Intent.createChooser(intent, mVideoDetailsInfo.getTitle()));
    }
}
