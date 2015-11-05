package com.example.slmolloy.movieapp;

public enum MovieSort {
    POPULARITY(0), VOTE_SCORE(1);

    private final int value;
    private MovieSort(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}