package com.example.v_ruchd.capstonestage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.v_ruchd.capstonestage2.R;

/**
 * Created by v-ruchd on 3/24/2017.
 */

public class NewsChannelResultAdapter extends RecyclerView.Adapter<NewsChannelResultAdapter.ViewHolder> {


    private final LayoutInflater mLayoutInflater;

    public NewsChannelResultAdapter(Context context){
        mLayoutInflater= LayoutInflater.from(context);
    }

    @Override
    public NewsChannelResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem=mLayoutInflater.inflate(R.layout.newchannel_result_item,null);
        ViewHolder viewHolder=new ViewHolder(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsChannelResultAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
