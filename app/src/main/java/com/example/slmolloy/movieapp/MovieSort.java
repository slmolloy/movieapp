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

    public static MovieSort newInstance(int value) {
        switch (value) {
            case 0:
                return POPULARITY;
            case 1:
                return VOTE_SCORE;
        }
        return POPULARITY;
    }
}