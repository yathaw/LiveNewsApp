package com.example.live.api;

import com.example.live.models.techcrunch.Techcrunch;
import com.example.live.models.topheadline.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface
{

    @GET("top-headlines")
    Call<Techcrunch> getTechcrunch (
            @Query("sources") String sources,
            @Query("apiKey") String apiKey
    );

    @GET("top-headlines")
    Call<News> getNews (
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}
