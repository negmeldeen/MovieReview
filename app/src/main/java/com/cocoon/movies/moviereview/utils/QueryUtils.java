package com.cocoon.movies.moviereview.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cocoon.movies.moviereview.dto.Movie;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public final class QueryUtils {


    private QueryUtils() {
    }

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            if (!Strings.isNullOrEmpty(stringUrl)) {
                url = new URL(stringUrl);
            }
        } catch (MalformedURLException e) {
            Log.e("Query Utils", "Malformed URL Exception", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (Exception e) {
            Log.e("QUERY UTILS", "Problem making the HTTP request.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder outputStream = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);

            String line = reader.readLine();
            while (line != null) {
                outputStream.append(line);
                line = reader.readLine();
            }
        }
        return outputStream.toString();
    }

    public static List<Movie> fetchMovieData(String requestURL) {
        URL url = createURL(requestURL);
        try {
            return Movie.fromJson(makeHttpRequest(url));
        } catch (Exception e) {
            Log.e("QUERY UTILS", "Problem getting movie information.", e);
        }
        return Lists.newArrayList();
    }

    public static String formatQueryLink(Context context, String query) {
        String apiUrl = ConfigHelper.getConfigValue(context, "api_url");
        String apiKey = ConfigHelper.getConfigValue(context, "api_key");
        Uri baseUri = Uri.parse(apiUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", apiKey);
        uriBuilder.appendQueryParameter("query", query);
        return uriBuilder.toString();
    }
}