package com.example.v_ruchd.capstonestage2;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.v_ruchd.capstonestage2.listener.DataSaveListener;
import com.example.v_ruchd.capstonestage2.listener.DataUpdateListener;
import com.example.v_ruchd.capstonestage2.modal.Articles;
import com.example.v_ruchd.capstonestage2.modal.NewsResponse;
import com.example.v_ruchd.capstonestage2.retrofitcalls.ApiClient;
import com.example.v_ruchd.capstonestage2.retrofitcalls.ApiInterface;
import com.example.v_ruchd.capstonestage2.widget.NewsListWidgetProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ruchi on 18/3/17.
 */

public class Utils {


    private static final String TAG = Utils.class.getSimpleName();

    public static boolean returnBackStackImmediate(FragmentManager fm) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (null != fragment && null != fragment.getChildFragmentManager() && fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                    if (fragment.getChildFragmentManager().popBackStackImmediate()) {
                        return true;
                    } else {
                        return returnBackStackImmediate(fragment.getChildFragmentManager());
                    }
                }
            }
        }
        return false;
    }


    public static void fetchArticleResponse(final Context context, String selectedData, final DataUpdateListener dataUpdateListener) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<NewsResponse> call = apiService.getNewsResponseByChannel(selectedData, Constants.NEWS_OPEN_API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {

                final List<Articles> articlesList = (response.body() == null ? new ArrayList<Articles>() : Arrays.asList(response.body().getArticles()));
                Log.d(TAG, "Number of articlesList received: " + articlesList.size());
                DataSaverTask saverTask = new DataSaverTask(context, response.body());
                saverTask.setDataSaveListener(new DataSaveListener() {
                    @Override
                    public void onDataSave() {
                        sendBroadCastToWodget(context);
                        if (null != dataUpdateListener) {
                            dataUpdateListener.onDataRetrieved(articlesList);
                        }

                    }
                });
                saverTask.execute();
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                String errorMsg = "";
                if (t instanceof IOException) {
                    errorMsg = context.getString(R.string.no_internet_connectivity);
                }
                ;
                if (null != dataUpdateListener) {
                    dataUpdateListener.onDataError(errorMsg);
                }
            }
        });
    }

    private static void sendBroadCastToWodget(Context context) {
        Intent intentWidget = new Intent(context, NewsListWidgetProvider.class);
        intentWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        ComponentName thisWidget = new ComponentName(context,
                NewsListWidgetProvider.class);
        int[] allWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisWidget);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
      /*  int[] ids = {AppWidgetManager.EXTRA_APPWIDGET_ID};*/
        intentWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        context.sendBroadcast(intentWidget);
    }


    public static String retrieveChannelForCategory(Context context, String selectedChannel) {
        String newsChannel = null;
        if (selectedChannel.equalsIgnoreCase(context.getString(R.string.general_category))) {
            newsChannel = context.getString(R.string.channelid_for_general_category);
        } else if (selectedChannel.equalsIgnoreCase(context.getString(R.string.entertainment_category))) {
            newsChannel = context.getString(R.string.channelid_for_entertainment_category);
        } else if (selectedChannel.equalsIgnoreCase(context.getString(R.string.technology_category))) {
            newsChannel = context.getString(R.string.channelid_for_technology_category);
        } else if (selectedChannel.equalsIgnoreCase(context.getString(R.string.science_nature_category))) {
            newsChannel = context.getString(R.string.channelid_for_science_nature_category);
        } else if (selectedChannel.equalsIgnoreCase(context.getString(R.string.music_category))) {
            newsChannel = context.getString(R.string.channelid_for_music_category);
        } else if (selectedChannel.equalsIgnoreCase(context.getString(R.string.politics_category))) {
            newsChannel = context.getString(R.string.channelid_for_politics_category);
        } else if (selectedChannel.equalsIgnoreCase(context.getString(R.string.gaming_category))) {
            newsChannel = context.getString(R.string.channelid_for_gaming_category);
        } else if (selectedChannel.equalsIgnoreCase(context.getString(R.string.sport_category))) {
            newsChannel = context.getString(R.string.channelid_for_sport_category);
        } else if (selectedChannel.equalsIgnoreCase(context.getString(R.string.business_category))) {
            newsChannel = context.getString(R.string.channelid_for_business_category);
        }
        return newsChannel;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
