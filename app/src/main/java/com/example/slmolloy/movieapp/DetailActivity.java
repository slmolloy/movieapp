package com.example.slmolloy.movieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DetailFragment extends Fragment {

        private final String LOG_TAG = DetailFragment.class.getSimpleName();

        private ShareActionProvider mShareActionProvider;
        private Movie mMovie;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                //mMovie = new Movie(0, intent.getStringExtra(Intent.EXTRA_TEXT));
                mMovie = intent.getParcelableExtra("movie");
                ImageView iv = (ImageView) rootView.findViewById(R.id.movie_poster_image_view);
                Picasso.with(getActivity())
                        .load("http://image.tmdb.org/t/p/w185/" + mMovie.getPoster())
                        .into(iv);

                Drawable d = iv.getDrawable();
                Bitmap b = ((BitmapDrawable)d).getBitmap();
                int width = b.getWidth();
                int height = b.getHeight();
                int bounding = dpToPx(200);
                float xScale = ((float) bounding) / width;
                float yScale = ((float) bounding) / height;
                float scale = (xScale <= yScale) ? xScale : yScale;
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                Bitmap scaledBitmap = Bitmap.createBitmap(b, 0, 0, width, height, matrix, true);
                width = scaledBitmap.getWidth();
                height = scaledBitmap.getHeight();
                BitmapDrawable result = new BitmapDrawable(scaledBitmap);
                iv.setImageDrawable(result);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                params.width = width;
                params.height = height;
                iv.setLayoutParams(params);

                ((TextView) rootView.findViewById(R.id.movie_title_text_view))
                        .setText(mMovie.getTitle());
                ((TextView) rootView.findViewById(R.id.movie_overview_text_view))
                        .setText(mMovie.getOverview());
                ((TextView) rootView.findViewById(R.id.movie_vote_score_text_view))
                        .setText(mMovie.getVoteScore() + "/10 ("
                                + mMovie.getVoteCount() + " votes)");
                ((TextView) rootView.findViewById(R.id.movie_release_date_text_view))
                        .setText("Released: " + mMovie.getReleaseDate());

                getActivity().setTitle(mMovie.getTitle());
            }

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment, menu);

            MenuItem menuItem = menu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            setShareIntent(createShareIntent());
        }

        private void setShareIntent(Intent shareIntent) {
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(shareIntent);
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }

        private Intent createShareIntent() {
            return new Intent(Intent.ACTION_SEND)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, "Lets go watch a " + mMovie.getTitle() + "!");
        }

        private int dpToPx(int dp)
        {
            float density = getActivity().getResources().getDisplayMetrics().density;
            return Math.round((float) dp * density);
        }
    }
}
