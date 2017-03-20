package com.example.v_ruchd.capstonestage2.retrofitcalls;

import com.example.v_ruchd.capstonestage2.modal.NewsChannelResponse;
import com.example.v_ruchd.capstonestage2.modal.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ruchi on 18/3/17.
 */

public interface ApiInterface {


    @GET("v1/articles")
    Call<NewsResponse> getNewsResponseByChannel(@Query("source") String source,@Query("apiKey") String apiKey);


    @GET("v1/sources")
    Call<NewsChannelResponse> getNewsChannelsByCategory(@Query("apiKey") String apiKey, @Query("category") String category);



}
