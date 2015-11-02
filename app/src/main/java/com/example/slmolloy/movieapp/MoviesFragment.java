package com.example.slmolloy.movieapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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

    private ArrayAdapter<String> mMoviesAdapter;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_refresh:
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
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        List<String> movieTitleList = new ArrayList<>();
        mMoviesAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_movie,
                R.id.list_item_movie_textview,
                movieTitleList);

        GridView gv = (GridView) rootView.findViewById(R.id.gridView);
        if (gv != null) {
            gv.setAdapter(mMoviesAdapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(
                            getActivity(),
                            mMoviesAdapter.getItem(position),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        return rootView;
    }

    private void updateMovies() {
        FetchMovieTitlesTask task = new FetchMovieTitlesTask(getActivity(), mMoviesAdapter);
        task.execute();
    }
}
