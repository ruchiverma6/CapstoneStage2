package com.example.v_ruchd.capstonestage2.luis;

/**
 * Created by ruchi on 2/4/17.
 */

public interface LuisDataUpdateListener {
    public void onLuisDataUpdate(String messageComtent,int messageType);
    public void onLuisDataErrorListener(String errorMessage);
}
