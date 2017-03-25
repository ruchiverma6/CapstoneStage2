package com.example.v_ruchd.capstonestage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.v_ruchd.capstonestage2.R;

/**
 * Created by v-ruchd on 3/24/2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Cursor cursor;
    private Context mContext;

    public ChatListAdapter(Context context) {

        this.mContext = context;
    }

    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browsed_content_list_item, parent, false);

        ChatListAdapter.ViewHolder vh = new ChatListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ChatListAdapter.ViewHolder holder, int position) {
     //   final Cursor cursor = this.getItem(position);

    }

    public void swapCursor(final Cursor cursor) {
        this.cursor = cursor;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 10;
      /*  return this.cursor != null
                ? this.cursor.getCount()
                : 0;*/
    }

  /*  public Cursor getItem(final int position) {
        if (this.cursor != null && !this.cursor.isClosed()) {
            this.cursor.moveToPosition(position);
        }

        return this.cursor;
    }*/

    public Cursor getCursor() {
        return this.cursor;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

        }
    }
}
