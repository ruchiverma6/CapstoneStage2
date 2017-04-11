package com.example.v_ruchd.capstonestage2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.example.v_ruchd.capstonestage2.listener.DataSaveListener;
import com.example.v_ruchd.capstonestage2.luis.LuisDataUpdateListener;
import com.example.v_ruchd.capstonestage2.luis.LuisHandler;
import com.example.v_ruchd.capstonestage2.modal.ChatMessage;
import com.example.v_ruchd.capstonestage2.modal.ChatMessageResponse;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //ProgressDialog object
    private ProgressDialog mProgressDialog;

    private Context mContext;

    private SharedPreferences sharedPref;
    InterstitialAd mInterstitialAd;
    boolean isApplicationFistTimeStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initComponents();

            showProgressDialog();

    }

    private void initComponents() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.addunitid));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startHomeActivity();

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(isApplicationFistTimeStarted){

                        procesSentMessageFromUser(getString(R.string.conversation_start_message));

                }
            }
        });

        sharedPref = mContext.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

         isApplicationFistTimeStarted = sharedPref.getBoolean(getString(R.string.fist_time_start), true);
        if (isApplicationFistTimeStarted) {
            editor.putBoolean(getString(R.string.fist_time_start), false);
            editor.commit();

        }else{
            stopProgressDialog();
            startHomeActivity();
        }


        if(isApplicationFistTimeStarted && !Utils.isNetworkAvailable(MainActivity.this)) {
            stopProgressDialog();
            startHomeActivity();
        }else {
            requestNewInterstitial();
        }
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    //Method to stop progress dialog.
    private void stopProgressDialog() {
        if (!isFinishing() && null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Method to show progress dialog
     */
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.getWindow().setGravity(Gravity.BOTTOM);
        if(!isFinishing() && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }


    /**
     * Method to start HomeActivity.
     */
    private void startHomeActivity() {
        Intent homeActivityIntent = new Intent(this, HomeActivity.class);
        startActivity(homeActivityIntent);
    }


    private void procesSentMessageFromUser(final String messageText) {
        LuisHandler luisHandler = new LuisHandler(this);
        luisHandler.sendMessageToLuis(messageText, new LuisDataUpdateListener() {
            @Override
            public void onLuisDataUpdate(String messageContent, int messageType) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(messageContent);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMessageType(messageType);
                chatMessage.setFrom(getString(R.string.bot));
                ChatMessageResponse chatMessageResponse = new ChatMessageResponse();
                chatMessageResponse.setChatMessages(new ChatMessage[]{chatMessage});

                DataSaverTask saverTask = new DataSaverTask(MainActivity.this, chatMessageResponse);
                saverTask.setDataSaveListener(new DataSaveListener() {
                    @Override
                    public void onDataSave() {

                        stopProgressDialog();
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }

                    }
                });
                saverTask.execute();
            }

            @Override
            public void onLuisDataErrorListener(String errorMessage) {
                stopProgressDialog();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                startHomeActivity();
            }
        });
    }
}
