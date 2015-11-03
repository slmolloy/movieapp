package com.example.slmolloy.movieapp;

public class Movie {
    private int mId;
    private String mTitle;
    private String mPoster;

    public Movie(int id) {
        mId = id;
    }

    public Movie(int id, String title) {
        mId = id;
        mTitle = title;
    }

    public Movie(int id, String title, String poster) {
        mId = id;
        mTitle = title;
        mPoster = poster;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }
}
