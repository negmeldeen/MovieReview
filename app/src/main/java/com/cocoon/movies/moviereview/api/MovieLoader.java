package com.cocoon.movies.moviereview.api;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.cocoon.movies.moviereview.dto.Movie;
import com.cocoon.movies.moviereview.utils.QueryUtils;
import com.google.common.base.Preconditions;

import java.util.List;


/**
 *
 */
public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private String mUrl;

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
        Preconditions.checkNotNull(mUrl, "Movie review url was Empty");

        // Perform the network request, parse the response, and extract a list of Movies.
        return QueryUtils.fetchMovieData(mUrl);
    }
}
