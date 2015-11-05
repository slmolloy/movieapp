package com.example.slmolloy.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private Context context;
    private View rootView;
    private Menu menu;
    private MovieAdapter mMoviesAdapter;
    private MovieSort sortOrder = MovieSort.POPULARITY;

    public MoviesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (PreferenceManager.getDefaultSharedPreferences(context)
                .contains("sort")) {
            String val = "";
            val = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("sort", "");
            if (val != "") {
                sortOrder = MovieSort.newInstance(Integer.parseInt(val));
                Log.d(LOG_TAG, "Got sort: " + sortOrder);
            } else {
                Log.d(LOG_TAG, "sort was blank");
            }
        } else {
            Log.d(LOG_TAG, "Didn't find sort");
        }
        updateMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;

        inflater.inflate(R.menu.moviesfragment, menu);
        switch(sortOrder) {
            case POPULARITY:
                menu.findItem(R.id.action_sort_popularity).setVisible(false);
                menu.findItem(R.id.action_sort_vote_score).setVisible(true);
                break;
            case VOTE_SCORE:
                menu.findItem(R.id.action_sort_popularity).setVisible(true);
                menu.findItem(R.id.action_sort_vote_score).setVisible(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_refresh:
                updateMovies();
                return true;
            case R.id.action_sort_popularity:
                sortOrder = MovieSort.POPULARITY;
                saveSortPreference();
                updateMovies();
                return true;
            case R.id.action_sort_vote_score:
                sortOrder = MovieSort.VOTE_SCORE;
                saveSortPreference();
                updateMovies();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        List<Movie> movieTitleList = new ArrayList<>();
        mMoviesAdapter = new MovieAdapter(
                getActivity(),
                movieTitleList);

        GridView gv = (GridView) rootView.findViewById(R.id.gridView);
        if (gv != null) {
            gv.setAdapter(mMoviesAdapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, mMoviesAdapter.getItem(position).getTitle())
                            .putExtra("movie", mMoviesAdapter.getItem(position));
                    startActivity(detailIntent);
                }
            });

            Log.d(LOG_TAG, "GridView Size: " + gv.getWidth() + " x " + gv.getHeight());
        }
        return rootView;
    }

    private void updateMovies() {
        FetchMovieTitlesTask task = new FetchMovieTitlesTask(getActivity(), mMoviesAdapter);
        task.execute(sortOrder);
    }

    private void saveSortPreference() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("sort", String.valueOf(sortOrder.getValue()))
                .commit();

        Log.d(LOG_TAG, "Saved sort: " + sortOrder);

        MenuItem popularity = menu.findItem(R.id.action_sort_popularity);
        MenuItem voteScore = menu.findItem(R.id.action_sort_vote_score);

        switch (sortOrder) {
            case POPULARITY:
                popularity.setVisible(false);
                voteScore.setVisible(true);
                break;
            case VOTE_SCORE:
                popularity.setVisible(true);
                voteScore.setVisible(false);
        }
    }

}
