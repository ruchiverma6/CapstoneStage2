package com.example.v_ruchd.capstonestage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.listener.OnDataItemClickListener;
import com.squareup.picasso.Picasso;



/**
 * Created by v-ruchd on 3/24/2017.
 */

public class NewsChannelResultAdapter extends RecyclerView.Adapter<NewsChannelResultAdapter.ViewHolder> {


    private final LayoutInflater mLayoutInflater;
    private final Context context;
    private Cursor cursor;
    private OnDataItemClickListener onBrowseContentItemClickListener;

    public NewsChannelResultAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }



    public void swapCursor(final Cursor cursor) {
        this.cursor = cursor;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.cursor != null
                ? this.cursor.getCount()
                : 0;
    }

    public Cursor getItem(final int position) {
        if (this.cursor != null && !this.cursor.isClosed()) {
            this.cursor.moveToPosition(position);
        }

        return this.cursor;
    }

    @Override
    public NewsChannelResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = mLayoutInflater.inflate(R.layout.newchannel_result_item, null);
        ViewHolder viewHolder = new ViewHolder(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsChannelResultAdapter.ViewHolder holder, int position) {
        cursor = getItem(position);

        String logoUrl = cursor.getString(cursor.getColumnIndex(NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_URL_TO_LOGOS));
        Picasso.with(context).load(logoUrl).into(holder.mChannelLogo);
    }




    public class ViewHolder extends RecyclerView.ViewHolder  {

        public ImageView mChannelLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            mChannelLogo = (ImageView) itemView.findViewById(R.id.channel_logo_imageview);

        }

    }
}

