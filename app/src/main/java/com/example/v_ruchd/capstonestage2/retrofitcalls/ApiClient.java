package com.example.v_ruchd.capstonestage2.retrofitcalls;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ruchi on 18/3/17.
 */

public class ApiClient {

    public static final String BASE_URL="https://newsapi.org/";

    public static Retrofit retrofit=null;

    public static Retrofit getClient(){
        if(retrofit==null){

            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }

}
