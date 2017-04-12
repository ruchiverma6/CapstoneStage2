package com.example.v_ruchd.capstonestage2.adapters;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v_ruchd.capstonestage2.ChatActivity;
import com.example.v_ruchd.capstonestage2.Constants;
import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.helper.AsyncQueryHandlerListener;
import com.example.v_ruchd.capstonestage2.helper.CustomAsyncQueryHandler;
import com.example.v_ruchd.capstonestage2.holders.BOTMessageViewHolder;
import com.example.v_ruchd.capstonestage2.holders.UserMessageViewHolder;
import com.example.v_ruchd.capstonestage2.listener.OnDataItemClickListener;
import com.example.v_ruchd.capstonestage2.modal.ChatMessage;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter implements LoaderManager.LoaderCallbacks<Cursor>, OnDataItemClickListener, AsyncQueryHandlerListener {
    public static final int TYPE_BOT = 0;
    public static final int TYPE_USER = 1;
    public static final int TYPE_INPUT_SELETION_RESULT = 2;
    public static final int TYPE_NEWS_CHANNELS_RESULT = 3;
    private static final int INPUTSELECTION_LOADER = 101;
    private static final int NEWSCHANNELRESPONSE_LOADER = 102;
    private static final int QUERY_SELECTED_CATEGORY = 300;
    private static final int QUERY_SELECTED_CATEGORY_FOR_NEWS_RESULT = 301;
    private final NewsChannelResultAdapter mChannelResponseAdapter;
    private LayoutInflater inflater;
    private RecyclerView.ViewHolder viewHolder;
    private Cursor cursor;
    private InputSelectionAdapter mInputSelectionAdapter;

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
    public int getItemViewType(int position) {
        getItem(position);
        int type = cursor.getInt(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_TYPE));
        if (type == TYPE_USER) {
            return TYPE_USER;
        } else if (type == TYPE_BOT) {
            return TYPE_BOT;
        } else if (type == TYPE_INPUT_SELETION_RESULT) {
            return TYPE_INPUT_SELETION_RESULT;
        } else if (type == TYPE_NEWS_CHANNELS_RESULT) {
            return TYPE_NEWS_CHANNELS_RESULT;
        }
        return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_USER:
                View v1 = inflater.inflate(R.layout.item_chat_user, parent, false);
                viewHolder = new UserMessageViewHolder(v1);
                break;

            case TYPE_BOT:
                View userMessageViewHolder = inflater.inflate(R.layout.item_chat_bot, parent, false);
                viewHolder = new BOTMessageViewHolder(userMessageViewHolder);
                break;


            case TYPE_NEWS_CHANNELS_RESULT:
                View newChannelResultViewHolder = inflater.inflate(R.layout.newschannel_result_layout, parent, false);
                viewHolder = new NewsChannelResultViewHolder(newChannelResultViewHolder);


                break;

            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {

    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {

    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case QUERY_SELECTED_CATEGORY:
                NewsChannelResultViewHolder newsChannelResultViewHolder = (NewsChannelResultViewHolder) cookie;
                String selectedCategory = "";
                if (cursor.moveToFirst()) {
                    selectedCategory = cursor.getString(cursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE));
                }
                newsChannelResultViewHolder.mNewsResultButton.setText("View " + selectedCategory + " news");
                break;
            case QUERY_SELECTED_CATEGORY_FOR_NEWS_RESULT:
                String selectedCategoryForNewsResult = "";

                if (cursor.moveToFirst()) {
                    selectedCategoryForNewsResult = cursor.getString(cursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE));
                }
                ((ChatActivity) context).onChannelSelection(selectedCategoryForNewsResult);
                break;
        }
    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {

    }


    public class NewsChannelResultViewHolder extends RecyclerView.ViewHolder {
        public Button mNewsResultButton;

        public NewsChannelResultViewHolder(View itemView) {
            super(itemView);
            mNewsResultButton = (Button) itemView.findViewById(R.id.news_result_btn);

        }
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        configureViewHolder1(viewHolder.getItemViewType(), holder, position);

    }

    private void configureViewHolder1(final int type, RecyclerView.ViewHolder viewHolder, final int position) {
        final Cursor cursor = this.getItem(position);
        switch (viewHolder.getItemViewType()) {
            case TYPE_USER:
                UserMessageViewHolder userMessageViewHolder = (UserMessageViewHolder) viewHolder;
                String message = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_CONTENT));
                userMessageViewHolder.mTextMessageTextView.setText(message);
                break;
            case TYPE_BOT:

                BOTMessageViewHolder holder = (BOTMessageViewHolder) viewHolder;
                String messageBot = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_CONTENT));

                holder.mTextMessageTextView.setText(messageBot);
                break;



            case TYPE_NEWS_CHANNELS_RESULT:

                final NewsChannelResultViewHolder newsChannelResultViewHolder = (NewsChannelResultViewHolder) viewHolder;


                cursor.moveToPosition(position);
                String messageId = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));


                CustomAsyncQueryHandler customAsyncQueryHandler = new CustomAsyncQueryHandler(context.getContentResolver());
                customAsyncQueryHandler.setAsyncQueryHandlerListener(this);
                customAsyncQueryHandler.startQuery(QUERY_SELECTED_CATEGORY, newsChannelResultViewHolder, NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);

                //  Cursor selectedCategoryCursor = context.getContentResolver().query(NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);


                // newsChannelResultViewHolder.mNewsResultButton.setText("View "+ selectedCategory+ " news");


                newsChannelResultViewHolder.mNewsResultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cursor.moveToPosition(position);
                        String messageId = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));
                        CustomAsyncQueryHandler customAsyncQueryHandler = new CustomAsyncQueryHandler(context.getContentResolver());
                        customAsyncQueryHandler.setAsyncQueryHandlerListener(ChatAdapter.this);
                        customAsyncQueryHandler.startQuery(QUERY_SELECTED_CATEGORY_FOR_NEWS_RESULT, null, NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);
                      /*  String selectedCategory = "";
                      //  Cursor selectedCategoryCursor = context.getContentResolver().query(NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);
                        if (selectedCategoryCursor.moveToFirst()) {
                            selectedCategory = selectedCategoryCursor.getString(selectedCategoryCursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE));
                        }
                        ((ChatActivity) context).onChannelSelection(selectedCategory);*/
                    }
                });


                break;
            default:

                break;
        }

    }


    private Activity context;

    public ChatAdapter(Activity context, List<ChatMessage> chatMessages) {
        this.context = context;

        inflater = LayoutInflater.from(context);

        mChannelResponseAdapter = new NewsChannelResultAdapter(context);
        mChannelResponseAdapter.setRecyclerViewListener(this);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setClickFalse(View view, final boolean isClicked) {
        view.setEnabled(isClicked);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setClickFalse(child, isClicked);
            }
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case NEWSCHANNELRESPONSE_LOADER:
                String messageId = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));
                String selectedCategory = "";
                Cursor selectedCategoryCursor = context.getContentResolver().query(NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);
                if (selectedCategoryCursor.moveToFirst()) {
                    selectedCategory = selectedCategoryCursor.getString(selectedCategoryCursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE));
                }
                Uri channelResponseUri = NewsContract.NewsChannelEntry.buildNewsChannelWithCategory(selectedCategory);
                return new CursorLoader(context, channelResponseUri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {

            case NEWSCHANNELRESPONSE_LOADER:
                mChannelResponseAdapter.swapCursor(data);
                break;


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {

            case NEWSCHANNELRESPONSE_LOADER:
                mChannelResponseAdapter.swapCursor(null);
                break;


        }
    }

    @Override
    public void onClick(View view, int position, Bundle result) {
        String selectedData = null;
        int viewType = -1;
        if (null != result) {
            viewType = result.getInt("viewtype");
            selectedData = result.getString("selecteddata");

        }
        switch (viewType) {

            case Constants.NEWCHANNELREULT_CATEGORY_TYPE:
                Toast.makeText(context, "clicked item position " + position, Toast.LENGTH_LONG).show();
                ((ChatActivity) context).onChannelSelection(selectedData);
                break;
            default:
                break;
        }
    }


    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }
}
