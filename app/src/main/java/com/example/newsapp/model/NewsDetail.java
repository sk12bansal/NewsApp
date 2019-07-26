package com.example.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsDetail {

    @SerializedName("title")
    private String title;
    @SerializedName("urlToImage")
    private String image;
    @SerializedName("url")
    private String news;

    @SerializedName("articles")
    private List<NewsDetail> mNewsDetailsList;

    public NewsDetail(String title, String image, String news) {
        this.title = title;
        this.image = image;
        this.news = news;
    }


    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getNews() {
        return news;
    }

    public List<NewsDetail> getNewsListModel() {
        return mNewsDetailsList;
    }

}
