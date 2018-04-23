package com.cocoon.movies.moviereview.utils;

import android.content.Context;
import android.content.res.Resources;

import com.cocoon.movies.moviereview.R;
import com.cocoon.movies.moviereview.exceptions.MovieReviewException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigHelper {

    public static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            throw (new MovieReviewException("Unable to find the config file: ", e));
        } catch (IOException e) {
            throw (new MovieReviewException("Failed to open config file.", e));
        }
    }
}
