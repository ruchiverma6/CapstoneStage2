package com.example.v_ruchd.capstonestage2.luis;

import android.content.Context;
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

        LUISIntent topIntent = response.getTopIntent();
        String[] greetingMsgEnteries = context.getResources().getStringArray(R.array.greeting_msg_enteries);
        for (String msg : greetingMsgEnteries) {
            if (topIntent.getName().equalsIgnoreCase(msg)) {
                String greetingMsg=getGreetingMsg ();
                luisDataUpdateListener.onLuisDataUpdate(greetingMsg, ChatAdapter.TYPE_BOT);
                return;
            }

        }


        String[] newsCategoryEntries = context.getResources().getStringArray(R.array.newscategory_msg_enteries);
        for (String msg : newsCategoryEntries) {
            if (topIntent.getName().contains(msg)) {
                luisDataUpdateListener.onLuisDataUpdate(msg, ChatAdapter.TYPE_INPUT_SELETION_RESULT);
                return;
            }

        }


        List<LUISEntity> entities = response.getEntities();


        String[] newsCategoryEnteriesForEntities = context.getResources().getStringArray(R.array.newscategory_msg_enteries);

        for (LUISEntity luisEntity : entities) {
            for (String msg : newsCategoryEnteriesForEntities) {
                if (Pattern.compile(Pattern.quote(luisEntity.getName()), Pattern.CASE_INSENSITIVE).matcher(msg).find()
                        ) {
                    final String selectedData = luisEntity.getName();
                    luisDataUpdateListener.onLuisDataUpdate(context.getString(R.string.input_category)+":" + selectedData, ChatAdapter.TYPE_NEWS_CHANNELS_RESULT);

                    return;
                }
            }
        }


        luisDataUpdateListener.onLuisDataUpdate(context.getString(R.string.select_category_msg), ChatAdapter.TYPE_BOT);
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
        return (context.getString(R.string.select_category_msg));

    }
}