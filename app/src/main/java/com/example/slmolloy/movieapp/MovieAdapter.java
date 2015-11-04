package com.example.slmolloy.movieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private ImageView mIv;

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_movie, parent, false);
        }

        mIv = (ImageView) convertView.findViewById(R.id.list_item_movie_imageview);
        mIv.setLayoutParams(new GridView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        /*
         * Sample code for more complex loading of image to view. Allows access to bitmap.
         */
        /*Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + movie.getPoster()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(LOG_TAG, "Image size: " + bitmap.getWidth() + " x " + bitmap.getHeight());
                mIv.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e(LOG_TAG, "Failed to load image");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/
        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster())
                .into(mIv);

        return convertView;
    }
}
