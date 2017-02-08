package com.github.bigexcalibur.herovideo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.bigexcalibur.herovideo.mediaplayer.MediaPlayerActivity;
import com.github.bigexcalibur.herovideo.mvp.live.model.LiveAppIndexInfo;
import com.github.bigexcalibur.herovideo.network.RetrofitHelper;
import com.github.bigexcalibur.herovideo.ui.widget.banner.BannerEntity;
import com.github.bigexcalibur.herovideo.ui.widget.banner.BannerView;
import com.github.bigexcalibur.herovideo.ui.widget.circle.CircleImageView;
import com.github.bigexcalibur.herovideo.util.LogUtil;
import com.github.bigexcalibur.herovideo.util.Md5;
import com.github.bigexcalibur.herovideo.util.ToastUtil;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/4 14:12
 * 100332338@qq.com
 * <p/>
 * 首页直播adapter
 */
public class LiveAppIndexAdapter extends RecyclerView.Adapter
{

    private Context context;

    private LiveAppIndexInfo mLiveAppIndexInfo;

    private int entranceSize;

    //直播分类入口
    private static final int TYPE_ENTRANCE = 0;

    //直播Item
    private static final int TYPE_LIVE_ITEM = 1;

    //直播分类Title
    private static final int TYPE_PARTITION = 2;

    //直播页Banner
    private static final int TYPE_BANNER = 3;

    private List<BannerEntity> bannerEntitys = new ArrayList<>();

    private List<Integer> liveSizes = new ArrayList<>();

    private int[] entranceIconRes = new int[]{
            R.drawable.live_home_follow_anchor,
            R.drawable.live_home_live_center,
            R.drawable.live_home_search_room,
            R.drawable.live_home_all_category
    };

    private String[] entranceTitles = new String[]{
            "关注主播", "直播中心",
            "搜索直播", "全部分类"
    };

    public LiveAppIndexAdapter(Context context)
    {

        this.context = context;
    }

    public void setLiveInfo(LiveAppIndexInfo liveAppIndexInfo)
    {

        this.mLiveAppIndexInfo = liveAppIndexInfo;
        entranceSize = 4;
        liveSizes.clear();
        bannerEntitys.clear();
        int tempSize = 0;
        int partitionSize = mLiveAppIndexInfo.getData().getPartitions().size();

        List<LiveAppIndexInfo.DataBean.BannerBean> banner = mLiveAppIndexInfo.getData().getBanner();
        Observable.from(banner)
                .forEach(bannerBean -> bannerEntitys.add(new BannerEntity(
                        bannerBean.getLink(), bannerBean.getTitle(), bannerBean.getImg())));

        for (int i = 0; i < partitionSize; i++)
        {
            liveSizes.add(tempSize);
            tempSize += mLiveAppIndexInfo.getData().getPartitions().get(i).getLives().size();
        }
    }

    public int getSpanSize(int pos)
    {

        int viewType = getItemViewType(pos);
        switch (viewType)
        {
            case TYPE_ENTRANCE:
                return 3;
            case TYPE_LIVE_ITEM:
                return 6;
            case TYPE_PARTITION:
                return 12;
            case TYPE_BANNER:
                return 12;
        }
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        View view;
        switch (viewType)
        {
            case TYPE_ENTRANCE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_live_entrance, null);
                return new LiveEntranceViewHolder(view);
            case TYPE_LIVE_ITEM:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_live_partition, null);
                return new LiveItemViewHolder(view);
            case TYPE_PARTITION:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_live_partition_title, null);
                return new LivePartitionViewHolder(view);
            case TYPE_BANNER:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_live_banner, null);
                return new LiveBannerViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        position -= 1;
        final LiveAppIndexInfo.DataBean.PartitionsBean.LivesBean livesBean;
        if (holder instanceof LiveEntranceViewHolder)
        {

            LiveEntranceViewHolder liveEntranceViewHolder = (LiveEntranceViewHolder) holder;
            liveEntranceViewHolder.title.setText(entranceTitles[position]);
            Glide.with(context)
                    .load(entranceIconRes[position])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((LiveEntranceViewHolder) holder).image);
        } else if (holder instanceof LiveItemViewHolder)
        {

            LiveItemViewHolder liveItemViewHolder = (LiveItemViewHolder) holder;

            livesBean = mLiveAppIndexInfo.getData().getPartitions().get(getItemPosition(position))
                    .getLives().get(position - 1 - entranceSize - getItemPosition(position) * 5);

            Glide.with(context)
                    .load(livesBean.getCover().getSrc())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bili_default_image_tv)
                    .dontAnimate()
                    .into(liveItemViewHolder.itemLiveCover);

            Glide.with(context)
                    .load(livesBean.getCover().getSrc())
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ico_user_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((LiveItemViewHolder) holder).itemLiveUserCover);

            liveItemViewHolder.itemLiveTitle.setText(livesBean.getTitle());
            liveItemViewHolder.itemLiveUser.setText(livesBean.getOwner().getName());
            liveItemViewHolder.itemLiveCount.setText(String.valueOf(livesBean.getOnline()));
//            liveItemViewHolder.itemLiveLayout.setOnClickListener(v -> LivePlayerActivity.
//                    launch((Activity) context, livesBean.getRoom_id(),
//                            livesBean.getTitle(), livesBean.getOnline(), livesBean.getOwner().getFace(),
//                            livesBean.getOwner().getName(), livesBean.getOwner().getMid()));
            liveItemViewHolder.itemLiveLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPlay(livesBean);
                }
            });
        } else if (holder instanceof LivePartitionViewHolder)
        {

            LivePartitionViewHolder livePartitionViewHolder = (LivePartitionViewHolder) holder;

            LiveAppIndexInfo.DataBean.PartitionsBean.PartitionBean partition = mLiveAppIndexInfo.
                    getData().getPartitions().get(getItemPosition(position)).getPartition();

            Glide.with(context)
                    .load(partition.getSub_icon().getSrc())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(livePartitionViewHolder.itemIcon);

            livePartitionViewHolder.itemTitle.setText(partition.getName());
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder("当前" + partition.getCount() + "个直播");
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(
                    context.getResources().getColor(R.color.text_primary_color));
            stringBuilder.setSpan(foregroundColorSpan, 2,
                    stringBuilder.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            livePartitionViewHolder.itemCount.setText(stringBuilder);
        } else if (holder instanceof LiveBannerViewHolder)
        {
            LiveBannerViewHolder liveBannerViewHolder = (LiveBannerViewHolder) holder;

            liveBannerViewHolder.banner
                    .delayTime(5)
                    .build(bannerEntitys);
        }
    }

    @Override
    public int getItemCount()
    {

        if (mLiveAppIndexInfo != null)
        {
            return 1 + entranceIconRes.length
                    + mLiveAppIndexInfo.getData().getPartitions().size() * 5;
        } else
        {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position)
    {

        if (position == 0)
        {
            return TYPE_BANNER;
        }
        position -= 1;
        if (position < entranceSize)
        {
            return TYPE_ENTRANCE;
        } else if (isPartitionTitle(position))
        {
            return TYPE_PARTITION;
        } else
        {
            return TYPE_LIVE_ITEM;
        }
    }

    /**
     * 获取当前Item在第几组中
     */
    private int getItemPosition(int pos)
    {

        pos -= entranceSize;
        return pos / 5;
    }

    private boolean isPartitionTitle(int pos)
    {

        pos -= entranceSize;
        return (pos % 5 == 0);
    }


    /**
     * 直播界面Banner ViewHolder
     */
    static class LiveBannerViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.item_live_banner)
        public BannerView banner;

        LiveBannerViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 直播界面Item分类 ViewHolder
     */
    static class LiveEntranceViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.live_entrance_title)
        public TextView title;

        @BindView(R.id.live_entrance_image)
        public ImageView image;

        LiveEntranceViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 直播界面Grid Item ViewHolder
     */
    static class LiveItemViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.item_live_cover)
        ImageView itemLiveCover;

        @BindView(R.id.item_live_user)
        TextView itemLiveUser;

        @BindView(R.id.item_live_title)
        TextView itemLiveTitle;

        @BindView(R.id.item_live_user_cover)
        CircleImageView itemLiveUserCover;

        @BindView(R.id.item_live_count)
        TextView itemLiveCount;

        @BindView(R.id.item_live_layout)
        CardView itemLiveLayout;

        LiveItemViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 直播界面分区类型 ViewHolder
     */
    static class LivePartitionViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.item_live_partition_icon)
        ImageView itemIcon;

        @BindView(R.id.item_live_partition_title)
        TextView itemTitle;

        @BindView(R.id.item_live_partition_count)
        TextView itemCount;

        LivePartitionViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void startPlay(LiveAppIndexInfo.DataBean.PartitionsBean.LivesBean livesBean){
        String appKey = "<Bilibili App Key Here>";
        String secretKey =  "<Bilibili App Secret Key Here>";
        String cid = "" +livesBean.getRoom_id();
        String ts ="" + System.currentTimeMillis();
        String apiParams = "appkey="+appKey+"&"+"cid="+cid+"&"+"player=1&quality=0&ts="+ts;

        String sign = Md5.strToMd5Low32(apiParams + secretKey);

        LogUtil.test("http://live.bilibili.com/api/playurl?"+apiParams+"&sign=" + sign);

        RetrofitHelper.getLiveAPI()
                .getLiveUrl(cid,appKey,ts,sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(responseBody -> parseLiveUrl(responseBody))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.ShortToast("error");
                    }
                    @Override
                    public void onNext(String url) {
                        LogUtil.test(url);
                        MediaPlayerActivity.configPlayer((Activity)context).setTitle(livesBean.getTitle()).setFullScreenOnly(true).playLive(url);
                    }
                });
    }

    private String parseLiveUrl(ResponseBody responseBody) {
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
        Element durlElement = rootElement.element("durl");
        Element urlElement = durlElement.element("url");
        String url = urlElement.getText();
        return url;
    }
}
