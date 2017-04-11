package com.example.v_ruchd.capstonestage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.listener.OnDataItemClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by v-ruchd on 3/17/2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {


    private final String[] dataSets;
    private static OnDataItemClickListener onBrowseContentItemClickListener;
    private Cursor cursor;
    private Context mContext;

    public ArticlesAdapter(Context context, String[] dataSets) {
        this.dataSets = dataSets;
        this.mContext = context;
    }

    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_content_list_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Cursor cursor = this.getItem(position);
        String content = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.COLUMN_TITLE));
        String urlLogo = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.COLUMN_URL_TO_IMAGE));
        holder.mTitleTextView.setText(content);
        Picasso.with(mContext).load(urlLogo).into(holder.mNewsLogoImageView);
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

    public Cursor getCursor() {
        return this.cursor;
    }


    public void setRecylerViewItemListener(OnDataItemClickListener onBrowseContentItemClickListener) {
        this.onBrowseContentItemClickListener = onBrowseContentItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitleTextView;
        public ImageView mNewsLogoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.news_title_text_view);
            mNewsLogoImageView = (ImageView) itemView.findViewById(R.id.news_image_logo);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            cursor = getItem(position);
            String sourceUrl = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.COLUMN_URL));
            String title = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.COLUMN_TITLE));
            Bundle bundle = new Bundle();
            bundle.putString(mContext.getString(R.string.source_url_key), sourceUrl);
            bundle.putString(mContext.getString(R.string.title_key), title);
            onBrowseContentItemClickListener.onClick(v, 0, bundle);
        }
    }
}
