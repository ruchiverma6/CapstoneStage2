package com.example.v_ruchd.capstonestage2.widget;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.Utils;
import com.example.v_ruchd.capstonestage2.data.NewsContract;

import java.io.IOException;
import java.net.URL;

/**
 * Created by v-ruchd on 4/10/2017.
 */

public class NewsWidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;



            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (null != data) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                String selectedChannel=  retriveSavedSelectedChannel();
                String selectedChannelIdForCategory= Utils.retrieveChannelForCategory(NewsWidgetRemoteViewService.this,selectedChannel);
                Uri uri = NewsContract.ArticleEntry.buildNewsArticleWithChannel(selectedChannelIdForCategory);
                data = getContentResolver().query(uri,
                        null,
                        null, null, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.news_list_item);
String title=data.getString(data.getColumnIndex(NewsContract.ArticleEntry.COLUMN_TITLE));
                views.setTextViewText(R.id.news_title_text_view, title);

                Intent fillIntent = new Intent();
              String sourceUrl=  data.getString(data.getColumnIndex(NewsContract.ArticleEntry.COLUMN_URL));
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.source_url_key), sourceUrl);
                bundle.putString(getString(R.string.title_key), title);
                fillIntent.putExtras(bundle);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }

    private String retriveSavedSelectedChannel() {
       SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
       String selectedChannel= sharedPref.getString(getString(R.string.selected_channel_for_news_result),getString(R.string.channelid_for_general_category));
        return selectedChannel;
    }



}
