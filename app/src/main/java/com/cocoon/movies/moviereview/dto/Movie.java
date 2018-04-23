package com.cocoon.movies.moviereview.dto;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class Movie {
    private String mTitle;
    private String mSummaryShort;
    private String mHeadLine;
    private String mRating;
    private Uri mImageUrl;
    private Uri mReviewLink;

    public Movie(String mTitle, String mSummaryShort, String mHeadLine, String mRating, Uri mImageUrl, Uri mReviewLink) {
        this.mTitle = mTitle;
        this.mSummaryShort = mSummaryShort;
        this.mHeadLine = mHeadLine;
        this.mRating = mRating;
        this.mImageUrl = mImageUrl;
        this.mReviewLink = mReviewLink;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSummaryShort() {
        return mSummaryShort;
    }

    public String getmHeadLine() {
        return mHeadLine;
    }

    public String getmRating() {
        return mRating;
    }

    public Uri getmImageUrl() {
        return mImageUrl;
    }

    public Uri getmReviewLink() {
        return mReviewLink;
    }

    public static List<Movie> fromJson(String movieJson) {
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();
        try {

            JSONObject mJSONObject = new JSONObject(movieJson);
            JSONArray jMovies = mJSONObject.getJSONArray("results");

            for (int i = 0; i < jMovies.length(); i++) {
                JSONObject obj = jMovies.getJSONObject(i);

                String title = obj.getString("display_title");
                String rating = obj.getString("mpaa_rating");
                String headline = obj.getString("headline");
                String summary = obj.getString("summary_short");

                JSONObject prop = obj.getJSONObject("link");
                String reviewLink = prop.getString("url");
                //Media Object in Json
                JSONObject media = obj.getJSONObject("multimedia");
                String imageLink = media.getString("src");

                Movie eq = new Movie(title, summary, headline, rating, Uri.parse(imageLink), Uri.parse(reviewLink));
                movies.add(eq);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }
        // Return the list of Movies
        return movies;


    }

}