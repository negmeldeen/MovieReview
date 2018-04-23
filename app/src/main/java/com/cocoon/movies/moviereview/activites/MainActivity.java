package com.cocoon.movies.moviereview.activites;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.cocoon.movies.moviereview.R;
import com.cocoon.movies.moviereview.adapters.MovieAdapter;
import com.cocoon.movies.moviereview.api.MovieLoader;
import com.cocoon.movies.moviereview.dto.Movie;
import com.cocoon.movies.moviereview.utils.QueryUtils;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Movie>> {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int MOVIE_LOADER_ID = 1;
    private MovieAdapter mAdapter;
    private String movieQuery = "";
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;
    private SearchView mSearchView;


    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader");

        String queryURL = QueryUtils.formatQueryLink(this, movieQuery);
        return new MovieLoader(this, queryURL);

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.clear();
        Log.d(LOG_TAG, "Resetting Adapter Content on destroying layout");
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mAdapter.clear();
        if (movies != null) {
            mAdapter.addAll(movies);
        }
        Log.d(LOG_TAG, "OnLoadFinished");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, Bookmarks.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the  ListView in the layout
        ListView MovieListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of Movies.
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        MovieListView.setAdapter(mAdapter);

        SearchView mSearchView = findViewById(R.id.sMovie_bar);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s)) {
                    movieQuery = s;
                    loadMovies();
                }
                return false;
            }
        });

        // testing for network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Preconditions.checkNotNull(cm);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.checkYourConnection);
        }
    }

    private void loadMovies() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
    }
}
