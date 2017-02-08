package com.github.bigexcalibur.herovideo.adapter.section;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.mvp.bangumi.model.BangumiAppIndexInfo;
import com.github.bigexcalibur.herovideo.ui.widget.sectioned.StatelessSection;
import com.github.bigexcalibur.herovideo.util.NumberUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 2016/10/14 19:29
 * 100332338@qq.com
 * <p>
 * 首页番剧新番连载Section
 */

public class HomeBangumiNewSerialSection extends StatelessSection
{

    private Context mContext;

    private List<BangumiAppIndexInfo.ResultBean.SerializingBean> newBangumiSerials;


    public HomeBangumiNewSerialSection(Context context, List<BangumiAppIndexInfo.ResultBean.SerializingBean> newBangumiSerials)
    {

        super(R.layout.layout_home_bangumi_new_serial_head, R.layout.layout_home_bangumi_new_serial_body);
        this.mContext = context;
        this.newBangumiSerials = newBangumiSerials;
    }

    @Override
    public int getContentItemsTotal()
    {

        return newBangumiSerials.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view)
    {

        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        BangumiAppIndexInfo.ResultBean.SerializingBean serializingBean = newBangumiSerials.get(position);

        Glide.with(mContext)
                .load(serializingBean.getCover())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bili_default_image_tv)
                .dontAnimate()
                .into(itemViewHolder.mImage);

        itemViewHolder.mTitle.setText(serializingBean.getTitle());
        itemViewHolder.mPlay.setText(NumberUtil.converString(serializingBean.getWatching_count()) + "人在看");
        itemViewHolder.mUpdate.setText("更新至第" + serializingBean.getNewest_ep_index() + "话");

//        itemViewHolder.mCardView.setOnClickListener(v -> BangumiDetailsActivity.launch(
//                (Activity) mContext, serializingBean.getSeason_id()));
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)
    {

        return new HomeBangumiNewSerialSection.HeaderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
    {

        HomeBangumiNewSerialSection.HeaderViewHolder headerViewHolder = (HomeBangumiNewSerialSection.HeaderViewHolder) holder;
//        headerViewHolder.mAllSerial.setOnClickListener(v -> mContext.startActivity(
//                new Intent(mContext, NewBangumiSerialActivity.class)));
    }


    static class HeaderViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tv_all_serial)
        TextView mAllSerial;


        HeaderViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.card_view)
        LinearLayout mCardView;

        @BindView(R.id.item_img)
        ImageView mImage;

        @BindView(R.id.item_title)
        TextView mTitle;

        @BindView(R.id.item_play)
        TextView mPlay;

        @BindView(R.id.item_update)
        TextView mUpdate;


        public ItemViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
