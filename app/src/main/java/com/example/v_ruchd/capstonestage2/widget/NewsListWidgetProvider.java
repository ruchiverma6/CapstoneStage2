package com.example.v_ruchd.capstonestage2.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.v_ruchd.capstonestage2.NewsListActivity;
import com.example.v_ruchd.capstonestage2.ChatActivity;
import com.example.v_ruchd.capstonestage2.NewsDetailActivity;
import com.example.v_ruchd.capstonestage2.R;

/**
 * Created by v-ruchd on 4/10/2017.
 */

public class NewsListWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.news_widget_layout);

            Intent intent = new Intent(context, ChatActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, remoteViews);
            } else {
                setRemoteAdapterV11(context, remoteViews);
            }
            boolean useStockGraph=context.getResources().getBoolean(R.bool.news_detail_enabled);
            Intent clickIntentTemplate=useStockGraph?new Intent(context, NewsDetailActivity.class):new Intent(context, NewsListActivity.class);
            PendingIntent clickPendingIntentTemplate= TaskStackBuilder.create(context).addNextIntentWithParentStack(clickIntentTemplate).getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list,clickPendingIntentTemplate);
            remoteViews.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapterV11(Context context, RemoteViews remoteViews) {
        remoteViews.setRemoteAdapter(R.id.widget_list, new Intent(context, NewsWidgetRemoteViewService.class));
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapter(Context context, RemoteViews remoteViews) {
        remoteViews.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context, NewsWidgetRemoteViewService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }
}

