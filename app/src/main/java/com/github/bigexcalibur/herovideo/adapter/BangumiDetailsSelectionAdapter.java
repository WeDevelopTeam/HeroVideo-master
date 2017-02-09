package com.github.bigexcalibur.herovideo.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.adapter.helper.AbsRecyclerViewAdapter;
import com.github.bigexcalibur.herovideo.mvp.bangumi.model.BangumiDetailsInfo;

import java.util.List;

/**
 * 番剧选集adapter
 */

public class BangumiDetailsSelectionAdapter extends AbsRecyclerViewAdapter
{

    private int layoutPosition = 0;

    private List<BangumiDetailsInfo.ResultBean.EpisodesBean> episodes;

    public BangumiDetailsSelectionAdapter(RecyclerView recyclerView, List<BangumiDetailsInfo.ResultBean.EpisodesBean> episodes)
    {

        super(recyclerView);
        this.episodes = episodes;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_bangumi_selection, parent, false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position)
    {

        if (holder instanceof ItemViewHolder)
        {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            BangumiDetailsInfo.ResultBean.EpisodesBean episodesBean = episodes.get(position);
            itemViewHolder.mIndex.setText("第 " + episodesBean.getIndex() + " 话");
            itemViewHolder.mTitle.setText(episodesBean.getIndex_title());

            if (position == layoutPosition)
            {
                itemViewHolder.mCardView.setForeground(getContext().getResources().getDrawable(R.drawable.bg_selection));
                itemViewHolder.mTitle.setTextColor(getContext().getResources().getColor(R.color.text_primary_color));
                itemViewHolder.mIndex.setTextColor(getContext().getResources().getColor(R.color.text_primary_color));
            } else
            {
                itemViewHolder.mCardView.setForeground(getContext().getResources().getDrawable(R.drawable.bg_normal));
                itemViewHolder.mTitle.setTextColor(getContext().getResources().getColor(R.color.black_alpha_45));
                itemViewHolder.mIndex.setTextColor(getContext().getResources().getColor(R.color.font_normal));
            }
        }
        super.onBindViewHolder(holder, position);
    }

    public void notifyItemForeground(int clickPosition)
    {

        layoutPosition = clickPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {

        return episodes.size();
    }

    public class ItemViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder
    {

        CardView mCardView;

        TextView mIndex;

        TextView mTitle;

        public ItemViewHolder(View itemView)
        {

            super(itemView);
            mCardView = $(R.id.card_view);
            mIndex = $(R.id.tv_index);
            mTitle = $(R.id.tv_title);
        }
    }
}
