package com.example.muhammad.moviereview;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {


    private QueryUtils() {
    }


    private static URL createURL(String StringUrl){
        URL url = null;
        try {
            url = new URL(StringUrl);
        }catch (MalformedURLException e){
            Log.e("Query Utils", "Malformed URL Exception", e);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream= null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }catch (IOException e){

        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder outputStream = new StringBuilder();
        if(inputStream != null){
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);

            String line = reader.readLine();
            while(line != null){
                outputStream.append(line);
                line = reader.readLine();
            }
        }
        return outputStream.toString();
    }

    private static List<Movie> extractFeatureFromJson(String movieJson){

        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Movie objects with the corresponding data.

            JSONObject mJSONObject = new JSONObject(movieJson);
            JSONArray jMovies = mJSONObject.getJSONArray("results");

            for(int i = 0; i < jMovies.length();i++){
                JSONObject obj = jMovies.getJSONObject(i);

                String title = obj.getString("display_title");
                String rating = obj.getString("mpaa_rating");
                String headline = obj.getString("headline");
                String summary = obj.getString("summary_short");
                //Link Object in Json
                JSONObject prop = obj.getJSONObject("link");
                String reviewLink = prop.getString("url");
                //Media Object in Json
                JSONObject media = obj.getJSONObject("multimedia");
                String imageLink = media.getString("src");

                Movie eq = new Movie( title, summary, headline, rating, Uri.parse(imageLink), Uri.parse(reviewLink));
                movies.add(eq);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }

        // Return the list of Movies
        return movies;
    }

    public static List<Movie> fetchMovieData(String requestURL){
        URL url = createURL(requestURL);

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e("QUERY UTILS", "Problem making the HTTP request.", e);
        }

        List<Movie> movies = extractFeatureFromJson(jsonResponse);

        return movies;
    }
}