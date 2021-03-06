package com.example.slmolloy.movieapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchMovieTitlesTask extends AsyncTask<MovieSort, Void, Movie[]> {

    private final String LOG_TAG = FetchMovieTitlesTask.class.getSimpleName();
    private Context mContext;
    private MovieAdapter mMoviesAdapter;

    public FetchMovieTitlesTask(Context context, MovieAdapter moviesAdapter) {
        mContext = context;
        mMoviesAdapter = moviesAdapter;
    }

    @Override
    protected Movie[] doInBackground(MovieSort... params) {
        if (params.length != 1) {
            return null;
        }

        Movie[] movies = null;
        String json = null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        /*
         * Example of API query:
         * http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=<your api key>
         */
        final String URL_BASE = "http://api.themoviedb.org/3/discover/movie";
        final String SORT_PARAM = "sort_by";
        final String API_PARAM = "api_key";

        final String POP_DESC_PARAM_VAL = "popularity.desc";
        final String VOTE_SCORE_PARAM_VAL = "vote_average.desc";
        final String THE_MOVIE_DB_API_KEY_VAL = "api_key_the_movie_db";

        try {
            ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(
                    mContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String movieDbApi = bundle.getString(THE_MOVIE_DB_API_KEY_VAL);

            Uri.Builder builder = Uri.parse(URL_BASE).buildUpon()
                    .appendQueryParameter(API_PARAM, movieDbApi);

            switch(params[0]) {
                case POPULARITY:
                    builder.appendQueryParameter(SORT_PARAM, POP_DESC_PARAM_VAL);
                    break;
                case VOTE_SCORE:
                    builder.appendQueryParameter(SORT_PARAM, VOTE_SCORE_PARAM_VAL);
                    break;
            }


            URL url = new URL(builder.build().toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            json = buffer.toString();
            movies = getMoviesFromJson(json);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return movies;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        if (movies != null) {
            mMoviesAdapter.clear();
            mMoviesAdapter.addAll(movies);
        }
    }

    private Movie[] getMoviesFromJson(String json)
            throws JSONException {
        Movie[] result = null;

        final String MOVIE_LIST = "results";
        final String MOVIE_TITLE = "title";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_ID = "id";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_VOTE_SCORE = "vote_average";
        final String MOVIE_VOTE_COUNT = "vote_count";
        final String MOVIE_POPULARITY = "popularity";

        JSONObject response = new JSONObject(json);
        JSONArray moviesArray = response.getJSONArray(MOVIE_LIST);
        result = new Movie[moviesArray.length()];

        JSONObject jObj;
        for (int i = 0; i < moviesArray.length(); i++) {
            jObj = moviesArray.getJSONObject(i);
            result[i] = new Movie(jObj.getInt(MOVIE_ID));
            result[i].setTitle(jObj.getString(MOVIE_TITLE));
            result[i].setPoster(jObj.getString(MOVIE_POSTER));
            result[i].setReleaseDate(jObj.getString(MOVIE_RELEASE_DATE));
            result[i].setOverview(jObj.getString(MOVIE_OVERVIEW));
            result[i].setVoteScore(Float.parseFloat(jObj.getString(MOVIE_VOTE_SCORE)));
            result[i].setVoteCount(jObj.getInt(MOVIE_VOTE_COUNT));
            result[i].setPopularity(Float.parseFloat(jObj.getString(MOVIE_POPULARITY)));
        }
        return result;
    }
}

