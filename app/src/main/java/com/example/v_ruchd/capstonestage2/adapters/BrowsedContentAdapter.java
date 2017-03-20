package com.example.v_ruchd.capstonestage2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.listener.OnBrowseContentItemClickListener;

/**
 * Created by v-ruchd on 3/17/2017.
 */

public class BrowsedContentAdapter extends RecyclerView.Adapter<BrowsedContentAdapter.ViewHolder>{


    private final String[] dataSets;
    private static OnBrowseContentItemClickListener onBrowseContentItemClickListener;

    public BrowsedContentAdapter(String[] dataSets){
        this.dataSets=dataSets;
    }
    @Override
    public BrowsedContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browsed_content_list_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitleTextView.setText(dataSets[position]);
    }

    @Override
    public int getItemCount() {
        return dataSets.length;
    }

    public void setRecylerViewItemListener(OnBrowseContentItemClickListener onBrowseContentItemClickListener){
        this.onBrowseContentItemClickListener=onBrowseContentItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView mTitleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.news_title_text_view);
        }


        @Override
        public void onClick(View v) {
            onBrowseContentItemClickListener.onClick(v);
        }
    }
}
