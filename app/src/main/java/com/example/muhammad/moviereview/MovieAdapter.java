package com.example.muhammad.moviereview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Muhammad on 4/21/2018.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Context context, ArrayList<Movie> Movies) {

        super(context, 0, Movies);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        final Movie currentMovie = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID Title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        // Get the version name from the current Movie object and
        // set this text on the name TextView
        titleTextView.setText(currentMovie.getmTitle());

        // Find the TextView in the list_item.xml layout with the ID Review
        TextView reviewTextView = (TextView) listItemView.findViewById(R.id.summary_short);
        // Get the Summary from the current Movie object
        // set this text on the Review TextView
        reviewTextView.setText(currentMovie.getmSummaryShort());


        TextView ratingView = (TextView) listItemView.findViewById(R.id.rating);
               ratingView.setText(currentMovie.getmRating());
        // Find the ImageView in the list_item.xml layout
        // Get the image resource ID from the current Movie object

        new DownloadImageTask((ImageView) listItemView.findViewById(R.id.poster))
                .execute(currentMovie.getmImageUrl().toString());
        //
        TextView readMore = listItemView.findViewById(R.id.read_more_text);

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openURL(currentMovie.getmReviewLink().toString());
            }
        });
        // Return the whole list item layout.
        // so that it can be shown in the ListView
        return listItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void openURL(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        getContext().startActivity(i);
    }
}
