package com.example.v_ruchd.capstonestage2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.v_ruchd.capstonestage2.fragments.BrowsedContentFragment;
import com.example.v_ruchd.capstonestage2.listener.DataSaveListener;
import com.example.v_ruchd.capstonestage2.listener.DataUpdateListener;
import com.example.v_ruchd.capstonestage2.modal.ChatMessageResponse;
import com.example.v_ruchd.capstonestage2.modal.NewsChannelResponse;
import com.example.v_ruchd.capstonestage2.modal.Sources;
import com.example.v_ruchd.capstonestage2.retrofitcalls.ApiClient;
import com.example.v_ruchd.capstonestage2.retrofitcalls.ApiInterface;

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
                if (fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
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

    public static void fetchNewsChannelsResponse(final Context context, String selectedData, final DataUpdateListener dataUpdateListener) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<NewsChannelResponse> callChannels = apiService.getNewsChannelsByCategory(Constants.API_KEY, "entertainment");
        callChannels.enqueue(new Callback<NewsChannelResponse>() {
            @Override
            public void onResponse(Call<NewsChannelResponse> call, Response<NewsChannelResponse> response) {
                List<Sources> sources = Arrays.asList(response.body().getSources());

                Log.d(TAG, "Number of movies received: " + sources.size());
                DataSaverTask saverTask = new DataSaverTask(context, response.body());
                saverTask.setDataSaveListener(new DataSaveListener() {
                    @Override
                    public void onDataSave() {
                        if (null != dataUpdateListener) {
                            dataUpdateListener.onDataRetrieved();
                        }

                    }
                });
                saverTask.execute();


            }

            @Override
            public void onFailure(Call<NewsChannelResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                if (null != dataUpdateListener) {
                    dataUpdateListener.onDataError(t.getMessage());
                }
            }
        });
    }

    public static void saveChatMessages(Context context,ChatMessageResponse chatMessageResponse, DataSaveListener dataSaveListener) {
        DataSaverTask saverTask = new DataSaverTask(context, chatMessageResponse);
        saverTask.setDataSaveListener(dataSaveListener);
        saverTask.execute();
    }
}
