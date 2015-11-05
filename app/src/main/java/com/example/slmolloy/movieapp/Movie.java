package com.example.slmolloy.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private int id;
    private String title;
    private String poster;
    private String releaseDate;
    private String overview;
    private Float voteScore;
    private int voteCount;
    private Float popularity;

    public Movie(int id) {
        this.id = id;
    }

    public Movie(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Movie(int id, String title, String poster) {
        this.id = id;
        this.title = title;
        this.poster = poster;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        voteScore = in.readFloat();
        voteCount = in.readInt();
        popularity = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return id + "--" + title + "--" + poster; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeFloat(voteScore);
        dest.writeInt(voteCount);
        dest.writeFloat(popularity);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Float getVoteScore() {
        return voteScore;
    }

    public void setVoteScore(Float voteScore) {
        this.voteScore = voteScore;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

}
