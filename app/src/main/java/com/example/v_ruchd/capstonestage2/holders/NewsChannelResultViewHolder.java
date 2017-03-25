package com.example.v_ruchd.capstonestage2.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.v_ruchd.capstonestage2.R;

/**
 * Created by v-ruchd on 3/24/2017.
 */
public class NewsChannelResultViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView mRecyclerView;

    public NewsChannelResultViewHolder(View itemView) {
        super(itemView);
        mRecyclerView=(RecyclerView)itemView.findViewById(R.id.horizontal_list);
    }
}
