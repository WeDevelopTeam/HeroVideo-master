package com.github.bigexcalibur.herovideo.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.bigexcalibur.herovideo.R;
import com.github.bigexcalibur.herovideo.adapter.helper.AbsRecyclerViewAdapter;

import java.util.List;

/**
 * 番剧选集adapter
 */

public class VideoDetaisSelectionAdapter extends AbsRecyclerViewAdapter {

    private int layoutPosition = 0;

    private List<String> urls;
    private String title;

    public VideoDetaisSelectionAdapter(RecyclerView recyclerView, List<String> urls,String title) {
        super(recyclerView);
        this.urls = urls;
        this.title = title;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_bangumi_selection, parent, false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            String url = urls.get(position);
            itemViewHolder.mIndex.setText("" +position);

            if (position == layoutPosition) {
                itemViewHolder.mCardView.setForeground(getContext().getResources().getDrawable(R.drawable.bg_selection));
                itemViewHolder.mIndex.setTextColor(getContext().getResources().getColor(R.color.text_primary_color));
            } else {
                itemViewHolder.mCardView.setForeground(getContext().getResources().getDrawable(R.drawable.bg_normal));
                itemViewHolder.mIndex.setTextColor(getContext().getResources().getColor(R.color.font_normal));
            }
        }
        super.onBindViewHolder(holder, position);
    }

    public void notifyItemForeground(int clickPosition) {

        layoutPosition = clickPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return urls.size();
    }

    public class ItemViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {

        CardView mCardView;

        TextView mIndex;

        public ItemViewHolder(View itemView) {

            super(itemView);
            mCardView = $(R.id.card_view);
            mIndex = $(R.id.tv_index);
        }
    }


}
