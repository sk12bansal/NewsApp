package com.example.newsapp.rest;

import com.example.newsapp.model.NewsDetail;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiEndPoints {

    @GET("/v2/top-headlines")
    Call<NewsDetail> getTopHeadLine(@Query("country") String name, @Query("apiKey") String key);
}
