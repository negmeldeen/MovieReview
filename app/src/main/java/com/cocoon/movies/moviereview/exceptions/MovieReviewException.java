package com.cocoon.movies.moviereview.exceptions;

public class MovieReviewException extends RuntimeException {
    public MovieReviewException(String message) {
        super(message);
    }

    public MovieReviewException(String message, Throwable cause) {
        super(message, cause);
    }

}
