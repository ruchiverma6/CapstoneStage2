package com.example.v_ruchd.capstonestage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.listener.DataSaveListener;
import com.example.v_ruchd.capstonestage2.modal.Articles;
import com.example.v_ruchd.capstonestage2.modal.ChatMessage;
import com.example.v_ruchd.capstonestage2.modal.ChatMessageResponse;
import com.example.v_ruchd.capstonestage2.modal.Data;
import com.example.v_ruchd.capstonestage2.modal.NewsChannelResponse;
import com.example.v_ruchd.capstonestage2.modal.NewsResponse;
import com.example.v_ruchd.capstonestage2.modal.Sources;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by v-ruchd on 3/20/2017.
 */

public class DataSaverTask extends AsyncTask<Void,Void,Void> {


    private final Data resultData;
    private final Context mContext;
    private DataSaveListener dataSaveListener;

    public DataSaverTask(Context context,Data resultData){
      this.resultData=resultData;
        this.mContext=context;
}

    public void setDataSaveListener(DataSaveListener dataSaveListener){
        this.dataSaveListener=dataSaveListener;
    }
    @Override
    protected Void doInBackground(Void... params) {
        saveDataInDb();
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(null!=dataSaveListener){
            dataSaveListener.onDataSave();
        }
    }

    private void saveDataInDb(){
        if( resultData instanceof NewsChannelResponse){
            List<Sources> sources= Arrays.asList(((NewsChannelResponse) resultData).getSources());

            saveSourceChannels(sources);



        }

       else if( resultData instanceof NewsResponse){
            String sourceId=((NewsResponse) resultData).getSource();
            List<Articles> articles= Arrays.asList(((NewsResponse) resultData).getArticles());
            saveArticles(articles,sourceId);
        }


        else if( resultData instanceof ChatMessageResponse){
         //   String sourceId=((ChatMessageResponse) resultData).getChat;
            List<ChatMessage> chatMessages= Arrays.asList(((ChatMessageResponse) resultData).getChatMessages());
            saveMessages(chatMessages);
        }

        Cursor cursor= mContext.getContentResolver().query(NewsContract.ArticleEntry.buildNewsArticleWithChannel("daily-mail"),null,null,null,null);
        cursor.getCount();
    }

    private void saveMessages(List<ChatMessage> chatMessages) {
        Vector<ContentValues> contentValuesVector = new Vector<>(chatMessages.size());
      //  Cursor cursor= mContext.getContentResolver().query(NewsContract.MessageEntry.CONTENT_URI,null,null,null,null);
     //   cursor.getCount();

       // int totalMessageCount=
        for (ChatMessage resultData : chatMessages) {
            ContentValues contentValuesMovies = new ContentValues();
            contentValuesMovies.put(NewsContract.MessageEntry.COLUMN_MESSAGE_ID, resultData.getId());
            contentValuesMovies.put(NewsContract.MessageEntry.COLUMN_MESSAGE_CONTENT, resultData.getMessage());
            contentValuesMovies.put(NewsContract.MessageEntry.COLUMN_DATE, resultData.getDate());
            contentValuesMovies.put(NewsContract.MessageEntry.COLUMN_MESSAGE_TYPE, resultData.getType());
            contentValuesVector.add(contentValuesMovies);



        }
        if (contentValuesVector.size() > 0) {
            ContentValues[] contentValuesArray = contentValuesVector.toArray(new ContentValues[contentValuesVector.size()]);
            mContext.getContentResolver().bulkInsert(NewsContract.MessageEntry.CONTENT_URI, contentValuesArray);
        }

        Cursor cursor= mContext.getContentResolver().query(NewsContract.MessageEntry.CONTENT_URI,null,null,null,null);
        cursor.getCount();
    }

    private void saveArticles(List<Articles> articles,String sourceId) {
        Vector<ContentValues> contentValuesVector = new Vector<>(articles.size());

        for (Articles resultData : articles) {
            ContentValues contentValuesMovies = new ContentValues();
            contentValuesMovies.put(NewsContract.ArticleEntry.COLUMN_ARTICLE_SOURCE_CHANNEL_ID, sourceId);
            contentValuesMovies.put(NewsContract.ArticleEntry.COLUMN_AUTHOR, resultData.getAuthor());
            contentValuesMovies.put(NewsContract.ArticleEntry.COLUMN_TITLE, resultData.getTitle());
            contentValuesMovies.put(NewsContract.ArticleEntry.COLUMN_DESCRIPTION, resultData.getDescription());
            contentValuesMovies.put(NewsContract.ArticleEntry.COLUMN_PUBLISHEDAT, resultData.getPublishedAt());
            contentValuesMovies.put(NewsContract.ArticleEntry.COLUMN_URL, resultData.getUrl());
            contentValuesMovies.put(NewsContract.ArticleEntry.COLUMN_URL_TO_IMAGE, resultData.getUrlToImage());
            contentValuesVector.add(contentValuesMovies);



        }
        if (contentValuesVector.size() > 0) {
            ContentValues[] contentValuesArray = contentValuesVector.toArray(new ContentValues[contentValuesVector.size()]);
            mContext.getContentResolver().bulkInsert(NewsContract.ArticleEntry.CONTENT_URI, contentValuesArray);
        }

    }

    private void saveSourceChannels(List<Sources> sources) {
        Vector<ContentValues> contentValuesVector = new Vector<>(sources.size());
        Vector<ContentValues> contentValuesVectorCategoryType = new Vector<>(sources.size());
        for (Sources resultData : sources) {
            ContentValues contentValuesMovies = new ContentValues();
            contentValuesMovies.put(NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_SOURCE_ID, resultData.getId());
            contentValuesMovies.put(NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_NAME, resultData.getName());
            contentValuesMovies.put(NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_DESCRIPTION, resultData.getDescription());
            contentValuesMovies.put(NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_URL, resultData.getUrl());
            contentValuesMovies.put(NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_URL_TO_LOGOS, resultData.getUrlsToLogos().getMedium());

            contentValuesVector.add(contentValuesMovies);

            ContentValues contentValuesCategoryType = new ContentValues();
            contentValuesCategoryType.put(NewsContract.CategoryEntry.COLUMN_CATEGORY_TYPE, resultData.getCategory());
            contentValuesCategoryType.put(NewsContract.CategoryEntry.COLUMN_NEWSCHANNEL_KEY, resultData.getId());

            contentValuesVectorCategoryType.add(contentValuesCategoryType);

        }
        if (contentValuesVector.size() > 0) {
            ContentValues[] contentValuesArray = contentValuesVector.toArray(new ContentValues[contentValuesVector.size()]);
            mContext.getContentResolver().bulkInsert(NewsContract.NewsChannelEntry.CONTENT_URI, contentValuesArray);
        }

        if (contentValuesVectorCategoryType.size() > 0) {
            ContentValues[] contentValuesArray = contentValuesVectorCategoryType.toArray(new ContentValues[contentValuesVectorCategoryType.size()]);
            mContext.getContentResolver().bulkInsert(NewsContract.CategoryEntry.CONTENT_URI, contentValuesArray);
        }
        Cursor cursor= mContext.getContentResolver().query(NewsContract.NewsChannelEntry.buildNewsChannelWithCategory("sport"),null,null,null,null);
        cursor.getCount();
    }

}
