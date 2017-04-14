package com.example.v_ruchd.capstonestage2.luis;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.v_ruchd.capstonestage2.Constants;
import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.adapters.ChatAdapter;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISClient;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISDialog;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISEntity;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISIntent;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponse;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponseHandler;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by ruchi on 2/4/17.
 */

public class LuisHandler {

    private final Context context;

    public LuisHandler(Context context) {
        this.context = context;
    }

    public void sendMessageToLuis(String msg, final LuisDataUpdateListener luisDataUpdateListener) {
        try {
            LUISClient client = new LUISClient(Constants.APP_ID, Constants.SUBCRIPTION_KEY, true);
            client.predict(msg, new LUISResponseHandler() {
                @Override
                public void onSuccess(LUISResponse response) {
                    processResponse(response, luisDataUpdateListener);
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();


                }
            });
        } catch (Exception e) {
            //  printToResponse(e.getMessage());
        }
    }


    public void processResponse(LUISResponse response, final LuisDataUpdateListener luisDataUpdateListener) {
        String[] newsCategoryEnteriesForEntities = context.getResources().getStringArray(R.array.newscategory_msg_enteries);
        LUISIntent topIntent = response.getTopIntent();
        String[] greetingMsgEnteries = context.getResources().getStringArray(R.array.greeting_msg_enteries);
        for (String msg : greetingMsgEnteries) {
            if (topIntent.getName().equalsIgnoreCase(msg)) {
                String greetingMsg = getGreetingMsg();
                luisDataUpdateListener.onLuisDataUpdate(greetingMsg, ChatAdapter.TYPE_BOT);
                return;
            }

        }

        for (String msg : newsCategoryEnteriesForEntities) {
            if (topIntent.getName().equalsIgnoreCase(msg)) {
                String categoryMessage = topIntent.getName();
                luisDataUpdateListener.onLuisDataUpdate(categoryMessage, ChatAdapter.TYPE_BOT);
                return;
            }


        }


        List<LUISIntent> intents = response.getIntents();

        List<LUISEntity> entities = response.getEntities();
        for (LUISIntent luisIntent : intents) {
            for (String msg : newsCategoryEnteriesForEntities) {
                if (luisIntent.getName().equalsIgnoreCase(msg)) {
                     String selectedData="";
                    if (selectedData.equalsIgnoreCase(context.getString(R.string.sports_category))){
                        selectedData=context.getString(R.string.sport_category);
                    }else if (selectedData.equalsIgnoreCase(context.getString(R.string.nature_category)) || selectedData.equalsIgnoreCase(context.getString(R.string.science_category)) ){
                        selectedData=context.getString(R.string.science_nature_category);
                    }
                    else {
                        selectedData = luisIntent.getName();
                    }
                        luisDataUpdateListener.onLuisDataUpdate(context.getString(R.string.input_category) + ":" + selectedData, ChatAdapter.TYPE_NEWS_CATEGORY_RESULT);

                    return;
                } else if (context.getString(R.string.none).equalsIgnoreCase(luisIntent.getName())) {
                    if (entities == null || entities.size() == 0) {
                        luisDataUpdateListener.onLuisDataUpdate(context.getString(R.string.select_category_msg), ChatAdapter.ERROR_REULT_LAYOUT_FOR_CATEGORY_SELECTION);
                        return;
                    }

                }
            }
        }


        for (LUISEntity luisEntity : entities) {
            for (String msg : newsCategoryEnteriesForEntities) {
                if (luisEntity.getName().equalsIgnoreCase(msg)
                        ) {
                    String selectedData=luisEntity.getName();
                    if (selectedData.equalsIgnoreCase(context.getString(R.string.sports_category))){
                        selectedData=context.getString(R.string.sport_category);
                    }else if (selectedData.equalsIgnoreCase(context.getString(R.string.nature_category)) || selectedData.equalsIgnoreCase(context.getString(R.string.science_category)) ){
                        selectedData=context.getString(R.string.science_nature_category);
                    }

                    luisDataUpdateListener.onLuisDataUpdate(context.getString(R.string.input_category) + ":" + selectedData, ChatAdapter.TYPE_NEWS_CATEGORY_RESULT);

                    return;
                }
            }
        }


        luisDataUpdateListener.onLuisDataUpdate(context.getString(R.string.select_category_msg), ChatAdapter.ERROR_REULT_LAYOUT_FOR_CATEGORY_SELECTION);
        LUISDialog dialog = response.getDialog();
        if (dialog != null) {
            Log.v("Dialog Status: ", dialog.getStatus());
            if (!dialog.isFinished()) {
                //  printToResponse("Dialog prompt: " + dialog.getPrompt());
            }
        }


    }

    private String getGreetingMsg() {
        String[] greetingMsgArray = context.getResources().getStringArray(R.array.greeting_msges_from_bot);
        int idx = new Random().nextInt(greetingMsgArray.length);
        return (greetingMsgArray[idx]);

    }
}