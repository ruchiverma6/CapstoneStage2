package com.example.v_ruchd.capstonestage2.adapters;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.v_ruchd.capstonestage2.ChatActivity;
import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.helper.AsyncQueryHandlerListener;
import com.example.v_ruchd.capstonestage2.helper.CustomAsyncQueryHandler;
import com.example.v_ruchd.capstonestage2.holders.BOTMessageViewHolder;
import com.example.v_ruchd.capstonestage2.holders.UserMessageViewHolder;


public class ChatAdapter extends RecyclerView.Adapter implements AsyncQueryHandlerListener {
    public static final int TYPE_BOT = 0;
    public static final int TYPE_USER = 1;
    public static final int ERROR_REULT_LAYOUT_FOR_CATEGORY_SELECTION = 2;
    public static final int TYPE_NEWS_CATEGORY_RESULT = 3;

    private static final int QUERY_SELECTED_CATEGORY = 300;
    private static final int QUERY_SELECTED_CATEGORY_FOR_NEWS_RESULT = 301;
    private static final int DELETE_MESSAGE_RESULT = 303;

    private LayoutInflater inflater;
    private RecyclerView.ViewHolder viewHolder;
    private Cursor cursor;
    private CustomAsyncQueryHandler customAsyncQueryHandler;

    private Activity context;

    public ChatAdapter(Activity context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        customAsyncQueryHandler = new CustomAsyncQueryHandler(context.getContentResolver());
        customAsyncQueryHandler.setAsyncQueryHandlerListener(this);
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
    public int getItemViewType(int position) {
        getItem(position);
        int type = cursor.getInt(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_TYPE));
        if (type == TYPE_USER) {
            return TYPE_USER;
        } else if (type == TYPE_BOT) {
            return TYPE_BOT;
        } else if (type == ERROR_REULT_LAYOUT_FOR_CATEGORY_SELECTION) {
            return ERROR_REULT_LAYOUT_FOR_CATEGORY_SELECTION;
        } else if (type == TYPE_NEWS_CATEGORY_RESULT) {
            return TYPE_NEWS_CATEGORY_RESULT;
        }
        return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_USER:
                View userMessageView = inflater.inflate(R.layout.item_chat_user, parent, false);
                viewHolder = new UserMessageViewHolder(userMessageView);
                break;

            case TYPE_BOT:
                View botMessageeView = inflater.inflate(R.layout.item_chat_bot, parent, false);
                viewHolder = new BOTMessageViewHolder(botMessageeView);
                break;

            case ERROR_REULT_LAYOUT_FOR_CATEGORY_SELECTION:
                View errorLayoutView = inflater.inflate(R.layout.category_selection_result_layout, parent, false);
                viewHolder = new CategorySelectionResultViewHolder(errorLayoutView);
                break;

            case TYPE_NEWS_CATEGORY_RESULT:
                View newsCategoryResultView = inflater.inflate(R.layout.newscategory_result_layout, parent, false);
                viewHolder = new NewsCategoryResultViewHolder(newsCategoryResultView);
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
        switch (token) {
            case DELETE_MESSAGE_RESULT:
                notifyDataSetChanged();
                Bundle bundle = (Bundle) cookie;
                String selectedCategoryForNewsResult = bundle.getString(context.getString(R.string.selected_channel_for_news_result));
                ((ChatActivity) context).onCategorySelection(selectedCategoryForNewsResult);
                break;
            default:
                break;
        }
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case QUERY_SELECTED_CATEGORY:
                NewsCategoryResultViewHolder newsCategoryResultViewHolder = (NewsCategoryResultViewHolder) cookie;
                String selectedCategory = "";
                if (cursor.moveToFirst()) {
                    selectedCategory = cursor.getString(cursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE));
                }
                newsCategoryResultViewHolder.mNewsCategoryResultButton.setText(context.getString(R.string.view) + " " + selectedCategory + " " + context.getString(R.string.news));
                break;
            case QUERY_SELECTED_CATEGORY_FOR_NEWS_RESULT:
                String selectedCategoryForNewsResult = "";
                String messgaeID = "";
                if (cursor.moveToFirst()) {
                    selectedCategoryForNewsResult = cursor.getString(cursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE));
                    messgaeID = cursor.getString(cursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_ID));

                }
                Bundle bundle = new Bundle();
                bundle.putString(context.getString(R.string.selected_channel_for_news_result), selectedCategoryForNewsResult);
                customAsyncQueryHandler.startDelete(DELETE_MESSAGE_RESULT, bundle, NewsContract.MessageEntry.buildMessageWithId(messgaeID), null, null);


                break;
        }
    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {

    }


    public class NewsCategoryResultViewHolder extends RecyclerView.ViewHolder {
        public Button mNewsCategoryResultButton;

        public NewsCategoryResultViewHolder(View itemView) {
            super(itemView);
            mNewsCategoryResultButton = (Button) itemView.findViewById(R.id.news_result_btn);

        }
    }

    public class CategorySelectionResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button mMusicCategoryButton;
        public Button mGeneralCategoryButton;
        public Button mEntertainmentCategoryButton;
        public Button mGamingCategoryButton;
        public Button mPoliticsCategoryButton;
        public Button mScienceNatureCategoryButton;
        public Button mSportCategoryButton;
        public Button mTechnologyCategoryButton;
        public Button mBusinessCategoryButton;
        private String messageId;

        public CategorySelectionResultViewHolder(View itemView) {
            super(itemView);
            mBusinessCategoryButton = (Button) itemView.findViewById(R.id.businss_category_btn);
            mBusinessCategoryButton.setOnClickListener(this);
            mGeneralCategoryButton = (Button) itemView.findViewById(R.id.general_category_btn);
            mEntertainmentCategoryButton = (Button) itemView.findViewById(R.id.entertainment_category_btn);
            mGamingCategoryButton = (Button) itemView.findViewById(R.id.gaming_category_btn);
            mMusicCategoryButton = (Button) itemView.findViewById(R.id.music_category_btn);
            mPoliticsCategoryButton = (Button) itemView.findViewById(R.id.politics_category_btn);
            mScienceNatureCategoryButton = (Button) itemView.findViewById(R.id.science_nature_category_btn);
            mSportCategoryButton = (Button) itemView.findViewById(R.id.sport_category_button);
            mTechnologyCategoryButton = (Button) itemView.findViewById(R.id.technology_category_button);
            mGeneralCategoryButton.setOnClickListener(this);
            mEntertainmentCategoryButton.setOnClickListener(this);
            mGamingCategoryButton.setOnClickListener(this);
            mMusicCategoryButton.setOnClickListener(this);
            mPoliticsCategoryButton.setOnClickListener(this);
            mScienceNatureCategoryButton.setOnClickListener(this);
            mSportCategoryButton.setOnClickListener(this);
            mTechnologyCategoryButton.setOnClickListener(this);

        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.businss_category_btn:
                case R.id.general_category_btn:
                case R.id.politics_category_btn:
                case R.id.sport_category_button:
                case R.id.gaming_category_btn:
                case R.id.music_category_btn:
                case R.id.technology_category_button:
                case R.id.science_nature_category_btn:
                case R.id.entertainment_category_btn:
                    Bundle bundle = new Bundle();
                    bundle.putString(context.getString(R.string.selected_channel_for_news_result), ((Button) v).getText().toString());
                    customAsyncQueryHandler.startDelete(DELETE_MESSAGE_RESULT, bundle, NewsContract.MessageEntry.buildMessageWithId(messageId), null, null);
                default:
                    break;
            }
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        configureViewHolder1(holder, position);

    }

    private void configureViewHolder1(RecyclerView.ViewHolder viewHolder, final int position) {
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
            case ERROR_REULT_LAYOUT_FOR_CATEGORY_SELECTION:
                final CategorySelectionResultViewHolder errorCategoryResultViewHolder = (CategorySelectionResultViewHolder) viewHolder;
                cursor.moveToPosition(position);
                String messageIdForErrorView = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));

                errorCategoryResultViewHolder.setMessageId(messageIdForErrorView);


                break;

            case TYPE_NEWS_CATEGORY_RESULT:

                final NewsCategoryResultViewHolder newsCategoryResultViewHolder = (NewsCategoryResultViewHolder) viewHolder;


                cursor.moveToPosition(position);
                String messageId = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));

                customAsyncQueryHandler.startQuery(QUERY_SELECTED_CATEGORY, newsCategoryResultViewHolder, NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);


                newsCategoryResultViewHolder.mNewsCategoryResultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cursor.moveToPosition(position);
                        String messageId = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));

                        customAsyncQueryHandler.setAsyncQueryHandlerListener(ChatAdapter.this);
                        customAsyncQueryHandler.startQuery(QUERY_SELECTED_CATEGORY_FOR_NEWS_RESULT, null, NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);

                    }
                });


                break;
            default:

                break;
        }

    }


    @Override
    public long getItemId(int position) {
        return position;
    }


}
