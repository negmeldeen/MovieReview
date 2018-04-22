package com.example.muhammad.moviereview;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Movie>> {
    public static final String LOG_TAG = MainActivity.class.getName();

    private final String NYT_MOVIE_REVIEWS_LINK = "https://api.nytimes.com/svc/movies/v2/reviews/search.json";
    private final String API_KEY = "65ec98d0a3ea4a72ae6a068e1eaa8f3f";
    private  String QUERY_MOVIE = "";
    private static final int MOVIE_LOADER_ID = 1;

    private MovieAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private ProgressBar mProgressBar;

    private SearchView mSearchView;


    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader");

        String queryURL = formatQueryLink(QUERY_MOVIE);
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
        try {
            if (movies != null || !movies.isEmpty()) {
                mAdapter.addAll(movies);
            }
        }catch(NullPointerException e){
            Log.e(LOG_TAG, "Null pointer Exception");
        }
        Log.d(LOG_TAG, "OnLoadFinished");

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

        mSearchView = findViewById(R.id.sMovie_bar);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s)){
                    QUERY_MOVIE = s;
                    SearchMovie();
                }
                return false;
            }
        });


        // testing for network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        }else{
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText("Please, check your connection!");
        }
    }

    private String formatQueryLink(String query){

        Uri baseUri = Uri.parse(NYT_MOVIE_REVIEWS_LINK);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", API_KEY);
        uriBuilder.appendQueryParameter("query", query);
        return uriBuilder.toString();
    }

    private void SearchMovie(){
        LoaderManager loaderManager = getLoaderManager();

        loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
    }
}
