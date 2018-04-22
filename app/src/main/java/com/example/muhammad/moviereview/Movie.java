package com.example.muhammad.moviereview;

import android.net.Uri;

/**
 * Created by Muhammad on 4/21/2018.
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
}