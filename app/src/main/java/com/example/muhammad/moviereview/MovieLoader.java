package com.example.muhammad.moviereview;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Muhammad on 4/22/2018.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>>{

    private String mUrl;

    private static final String LOG_TAG = MovieLoader.class.getName();

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of Movies.
        List<Movie> movies = QueryUtils.fetchMovieData(mUrl);
        return movies;
    }
}
