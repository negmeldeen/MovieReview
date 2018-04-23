package com.cocoon.movies.moviereview.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.cocoon.movies.moviereview.R;
import com.cocoon.movies.moviereview.adapters.MovieAdapter;
import com.cocoon.movies.moviereview.dao.Bookmark;
import com.cocoon.movies.moviereview.dto.Movie;

import java.util.ArrayList;

/**
 *
 */
public class Bookmarks extends AppCompatActivity {

    private MovieAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        ListView MovieListView = (ListView) findViewById(R.id.list);

        ArrayList<Movie> moviesList = new ArrayList<>(Bookmark.bookmarks.values());

        mAdapter = new MovieAdapter(this, moviesList);

        MovieListView.setAdapter(mAdapter);
    }
}
