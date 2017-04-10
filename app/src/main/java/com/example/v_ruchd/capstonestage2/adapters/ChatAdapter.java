package com.example.v_ruchd.capstonestage2.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v_ruchd.capstonestage2.Constants;
import com.example.v_ruchd.capstonestage2.HomeActivity;
import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.holders.BOTMessageViewHolder;
import com.example.v_ruchd.capstonestage2.holders.UserMessageViewHolder;
import com.example.v_ruchd.capstonestage2.listener.OnBrowseContentItemClickListener;
import com.example.v_ruchd.capstonestage2.modal.ChatMessage;

import java.util.List;




public class ChatAdapter extends RecyclerView.Adapter implements LoaderManager.LoaderCallbacks<Cursor>, OnBrowseContentItemClickListener {
    public static final int TYPE_BOT = 0;
    public static final int TYPE_USER = 1;
    public static final int TYPE_INPUT_SELETION_RESULT = 2;
    public static final int TYPE_NEWS_CHANNELS_RESULT = 3;
    private static final int INPUTSELECTION_LOADER = 101;
    private static final int NEWSCHANNELRESPONSE_LOADER = 102;
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
                View v1 = inflater.inflate(R.layout.item_chat_left, parent, false);
                viewHolder = new UserMessageViewHolder(v1);
                break;

            case TYPE_BOT:
                View userMessageViewHolder = inflater.inflate(R.layout.item_chat_right, parent, false);
                viewHolder = new BOTMessageViewHolder(userMessageViewHolder);
                break;
            case TYPE_INPUT_SELETION_RESULT:
                View inputSelectionViewHolder = inflater.inflate(R.layout.chat_inputselection_layout, parent, false);
                viewHolder = new InputSelectiomMessageViewHolder(inputSelectionViewHolder);
                // InputSelectiomMessageViewHolder inputSelectiomMessageViewHolder = (InputSelectiomMessageViewHolder) viewHolder;
             /*   RecyclerView.LayoutParams layoutParams=(RecyclerView.LayoutParams) inputSelectiomMessageViewHolder.mRecyclerView.getLayoutParams();
                layoutParams.width= RecyclerView.LayoutParams.WRAP_CONTENT;
                layoutParams.height=150;
                inputSelectiomMessageViewHolder.mRecyclerView.setLayoutParams(layoutParams);*/

                //  ((HomeActivity) context).getSupportLoaderManager().initLoader(INPUTSELECTION_LOADER, null, this);
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




    public class NewsChannelResultViewHolder extends RecyclerView.ViewHolder {
       public Button mNewsResultButton;

        public NewsChannelResultViewHolder(View itemView) {
            super(itemView);
            mNewsResultButton=(Button)itemView.findViewById(R.id.news_result_btn);

        }
    }

    public class InputSelectiomMessageViewHolder extends RecyclerView.ViewHolder implements OnBrowseContentItemClickListener{
        public RecyclerView mRecyclerView;

        public InputSelectiomMessageViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            //inputSelectiomMessageViewHolder.mRecyclerView.setNestedScrollingEnabled(false);
            mInputSelectionAdapter = new InputSelectionAdapter(context);
         //   mInputSelectionAdapter.setRecyclerViewListener(this);

            mRecyclerView.setAdapter(mInputSelectionAdapter);
           // mRecyclerView.setLayoutFrozen(true);
        }

        @Override
        public void onClick(View view, int position, Bundle result) {
            String selectedData = null;
            int viewType = -1;
            if (null != result) {
                viewType = result.getInt("viewtype");
                selectedData = result.getString("selecteddata");

            }

            viewHolder.getLayoutPosition();
            mRecyclerView.getChildLayoutPosition(view);
            int positionCategory = viewHolder.getAdapterPosition();
            if(positionCategory==-1){
                return;
            }
            cursor.moveToPosition(positionCategory);
            String messageId=cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));
            ContentValues contentValues=new ContentValues();
            contentValues.put(NewsContract.MessageEntry.COLUMN_MESSAGE_ID,messageId);
            contentValues.put(NewsContract.MessageEntry.COLUMN_MESSAGE_RESULT_ISCLICK,true);
            context.getContentResolver().update(NewsContract.MessageEntry.buildMessageWithId(messageId),contentValues,null,null);

            Toast.makeText(context, "clicked item position " + position, Toast.LENGTH_LONG).show();
            ((HomeActivity) context).onCategoryelection(selectedData);

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

            case TYPE_INPUT_SELETION_RESULT:
                cursor.moveToPosition(position);
                InputSelectiomMessageViewHolder inputSelectiomMessageViewHolder = (InputSelectiomMessageViewHolder) viewHolder;
                String isClick=cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_RESULT_ISCLICK));
                String is=cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));
                int isAlreadyClicked=0;
                if(null!=isClick && Integer.valueOf(isClick)==1){
                    setClickFalse((View) ((InputSelectiomMessageViewHolder) viewHolder).mRecyclerView,false);
                    isAlreadyClicked=100;
                   // inputSelectiomMessageViewHolder.mRecyclerView.setEnabled(false);
                 /* ((InputSelectiomMessageViewHolder) viewHolder).mRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
                        @Override
                        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                            // true: consume touch event
                            // false: dispatch touch event
                            return false;
                        }
                    });*/
                }else {
                    setClickFalse((View) ((InputSelectiomMessageViewHolder) viewHolder).mRecyclerView,true);
                }


                mInputSelectionAdapter.setRecyclerViewListener(new OnBrowseContentItemClickListener() {
                    @Override
                    public void onClick(View view, int position, Bundle result) {
                        String selectedData = null;
                        int viewType = -1;
                        if (null != result) {
                            viewType = result.getInt("viewtype");
                            selectedData = result.getString("selecteddata");

                        }

                        /*viewHolder.getLayoutPosition();
                        mRecyclerView.getChildLayoutPosition(view);
                        int positionCategory = viewHolder.getAdapterPosition();
                        if(positionCategory==-1){
                            return;
                        }*/
                        cursor.moveToPosition(position);
                        String messageId=cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));
                        ContentValues contentValues=new ContentValues();
                        contentValues.put(NewsContract.MessageEntry.COLUMN_MESSAGE_ID,messageId);
                        contentValues.put(NewsContract.MessageEntry.COLUMN_MESSAGE_RESULT_ISCLICK,true);
                        context.getContentResolver().update(NewsContract.MessageEntry.buildMessageWithId(messageId),contentValues,null,null);

                        Toast.makeText(context, "clicked item position " + position, Toast.LENGTH_LONG).show();
                        ((HomeActivity) context).onCategoryelection(selectedData);


                    }
                });

                mInputSelectionAdapter.setData(context.getResources().getStringArray(R.array.inputcategoryselectionenteries));
            //    mInputSelectionAdapter.setMessageId(cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID)),isAlreadyClicked);
                mInputSelectionAdapter.notifyDataSetChanged();
                break;

            case TYPE_NEWS_CHANNELS_RESULT:
                //  String selectedCategory;
                NewsChannelResultViewHolder newsChannelResultViewHolder = (NewsChannelResultViewHolder) viewHolder;


               cursor.moveToPosition(position);
                String messageId = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));
                String selectedCategory = "";
                Cursor selectedCategoryCursor = context.getContentResolver().query(NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);
                if (selectedCategoryCursor.moveToFirst()) {
                    selectedCategory = selectedCategoryCursor.getString(selectedCategoryCursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE));
                }


                newsChannelResultViewHolder.mNewsResultButton.setText("View "+ selectedCategory+ " news");
              //  ((HomeActivity) context).onChannelection(selectedCategory);


               newsChannelResultViewHolder.mNewsResultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cursor.moveToPosition(position);
                        String messageId = cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));
                  /*      ContentValues contentValues=new ContentValues();
                        contentValues.put(NewsContract.MessageEntry.COLUMN_MESSAGE_ID,messageId);
                        contentValues.put(NewsContract.MessageEntry.COLUMN_MESSAGE_RESULT_ISCLICK,true);
                        context.getContentResolver().insert(NewsContract.MessageEntry.CONTENT_URI,contentValues);*/

                        String selectedCategory = "";
                        Cursor selectedCategoryCursor = context.getContentResolver().query(NewsContract.MessageCategorySelectionEntry.buildselectedCategoryForMessage(messageId), null, null, null, null);
                        if (selectedCategoryCursor.moveToFirst()) {
                            selectedCategory = selectedCategoryCursor.getString(selectedCategoryCursor.getColumnIndex(NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE));
                        }
                        ((HomeActivity) context).onChannelection(selectedCategory);
                    }
                });



           //     ((HomeActivity) context).getSupportLoaderManager().restartLoader(NEWSCHANNELRESPONSE_LOADER, null, this);
                break;
            default:

                break;
        }

    }


    //private final List<ChatMessage> chatMessages;
    private Activity context;

    public ChatAdapter(Activity context, List<ChatMessage> chatMessages) {
        this.context = context;
        //   this.chatMessages = chatMessages;
        inflater = LayoutInflater.from(context);
       /* mInputSelectionAdapter = new InputSelectionAdapter(context);
        mInputSelectionAdapter.setRecyclerViewListener(this);
*/        //mI.set
        mChannelResponseAdapter = new NewsChannelResultAdapter(context);
        mChannelResponseAdapter.setRecyclerViewListener(this);
    }



 /*   @Override
    public ChatMessage getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }*/


    @Override
    public long getItemId(int position) {
        return position;
    }

   public void setClickFalse(View view,final boolean isClicked){
       view.setEnabled(isClicked);
       if(view instanceof ViewGroup){
           ViewGroup viewGroup=(ViewGroup)view;
           for(int i=0;i<viewGroup.getChildCount();i++){
               View child=viewGroup.getChildAt(i);
               setClickFalse(child,isClicked);
           }
       }
   }


    /* @Override
     public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder holder;
         ChatMessage chatMessage = getItem(position);
         LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         if (convertView == null) {
             convertView = vi.inflate(R.layout.list_item_chat_message, null);
             holder = createViewHolder(convertView);
             convertView.setTag(holder);
         } else {
             holder = (ViewHolder) convertView.getTag();
         }

         boolean myMsg = chatMessage.getType() ;//Just a dummy check to simulate whether it me or other sender
         setAlignment(holder, myMsg);
         holder.txtMessage.setText(chatMessage.getMessage());
         holder.txtInfo.setText(chatMessage.getDate());


         return convertView;
     }
 */
    // public void add(ChatMessage message) {
    //  chatMessages.add(message);
    // }

   /* public void add(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
    }*/

    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (!isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
           /* case INPUTSELECTION_LOADER:
                Uri uri = NewsContract.CategoryEntry.CONTENT_URI;

                return new CursorLoader(context, uri, null, null, null, null);*/

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
            /*case INPUTSELECTION_LOADER:
               // mInputSelectionAdapter.swapCursor(data);
                break;*/
            case NEWSCHANNELRESPONSE_LOADER:
                mChannelResponseAdapter.swapCursor(data);
                break;


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            /*case INPUTSELECTION_LOADER:
                mInputSelectionAdapter.swapCursor(null);
                break;*/
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
            case Constants.INPUT_CATEGORY_TYPE:
                viewHolder.getLayoutPosition();
             //  mRecyclerView.getChildLayoutPosition(view);
                int positionCategory = viewHolder.getAdapterPosition();
                cursor.moveToPosition(positionCategory);
                String messageId=cursor.getString(cursor.getColumnIndex(NewsContract.MessageEntry.COLUMN_MESSAGE_ID));
                ContentValues contentValues=new ContentValues();
                contentValues.put(NewsContract.MessageEntry.COLUMN_MESSAGE_ID,messageId);
                contentValues.put(NewsContract.MessageEntry.COLUMN_MESSAGE_RESULT_ISCLICK,true);
                context.getContentResolver().update(NewsContract.MessageEntry.buildMessageWithId(messageId),contentValues,null,null);

                Toast.makeText(context, "clicked item position " + position, Toast.LENGTH_LONG).show();
                ((HomeActivity) context).onCategoryelection(selectedData);

                break;

            case Constants.NEWCHANNELREULT_CATEGORY_TYPE:
                Toast.makeText(context, "clicked item position " + position, Toast.LENGTH_LONG).show();
                ((HomeActivity) context).onChannelection(selectedData);
                break;
            default:
                break;
        }
    }
/*
    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        return holder;
    }*/


    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }
}
