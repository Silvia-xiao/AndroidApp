package com.example.androidass.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class GetMovieImage extends AsyncTask<String, Void, Bitmap> {

    ImageView movieImage;

    public GetMovieImage(ImageView movieImage) {
        this.movieImage = movieImage;
    }

    @Override
    protected Bitmap doInBackground(String... urls)
    {
        String url = urls[0];
        Bitmap bitmap = null;
        try
        {
            //get the picture from omdb api.
            InputStream inputStream = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        movieImage.setImageBitmap(bitmap);
    }
}
