package com.example.v_ruchd.capstonestage2.listener;

import java.util.List;

/**
 * Created by ruchi on 29/3/17.
 */

public interface DataUpdateListener<E> {

    public void onDataRetrieved(List<E> resultList);

    public void onDataError(String message);
}
