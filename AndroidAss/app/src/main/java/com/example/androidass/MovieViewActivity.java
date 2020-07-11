package com.example.androidass;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidass.Utility.GetMovieImage;
import com.example.androidass.database.MovieDB;
import com.example.androidass.entity.Movie;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;
import com.example.androidass.viewmodel.MovieViewModel;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MovieViewActivity extends AppCompatActivity {
    private TextView movieNameTV;
    private TextView movieReleaseDateTV;
    private TextView directorTV;
    private TextView genreTV;
    private TextView countryTV;
    private TextView plotTV;
    private TextView castTV;
    private ImageView moviePictureIV;
    private Button addWatchListBT;
    private Button addMemoirBT;
    private Movie movie;
    private String poster;
    private NetworkConnection networkConnection = null;
    private Person person;
    private RatingBar ratingBar;
    private String verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        networkConnection = new NetworkConnection();
        movieNameTV = findViewById(R.id.detail_movie_name_tv);
        movieReleaseDateTV = findViewById(R.id.detail_release_date_tv);
        directorTV = findViewById(R.id.detail_director_tv);
        genreTV = findViewById(R.id.detail_genre_tv);
        countryTV = findViewById(R.id.detail_country_tv);
        plotTV = findViewById(R.id.detail_plot_content_tv);
        castTV = findViewById(R.id.detail_cast_content_tv);
        moviePictureIV = findViewById(R.id.detail_movie_image_iv);
        addWatchListBT = findViewById(R.id.add_watchlist_bt);
        addMemoirBT = findViewById(R.id.add_memoir_bt);

        Intent intent = getIntent();
        final String movieId = intent.getStringExtra("id");
        Bundle bundle = intent.getExtras();
        person = bundle.getParcelable("personSearch");
        verify = intent.getStringExtra("verify");

        GetMovieDetail getMovieDetail = new GetMovieDetail();
        getMovieDetail.execute(movieId);
        movie = new Movie(movieId);

        addMemoirBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MovieViewActivity.this, AddNewMemoirActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("personView", person);
                String release = movieReleaseDateTV.getText().toString();
                String title = movieNameTV.getText().toString();

                it.putExtras(bundle);
                it.putExtra("Release", release);
                it.putExtra("Title", title);
                it.putExtra("MovieId", movie.getMovieId());
                it.putExtra("Poster", poster);
                it.putExtra("PublicRating",ratingBar.getRating());
                startActivity(it);
            }
        });

        //make sure if the add to watchlist button can be click or not.
        if (verify.equals("able")) {
            addWatchListBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddToWatchlist addToWatchlist = new AddToWatchlist();
                    addToWatchlist.execute(movie);
                }
            });
        }
    }


    class GetMovieDetail extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            try {
                String result = networkConnection.getMovieDetailInfoFromOmdb(id);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            try {
                JSONObject detail = new JSONObject(str);

                String title = detail.getString("Title");
                String releaseDate = detail.getString("Released");
                String genre = detail.getString("Genre");
                String director = detail.getString("Director");
                poster = detail.getString("Poster");
                String plot = detail.getString("Plot");
                String country = detail.getString("Country");
                String rating = detail.getString("Metascore");
                String actors = detail.getString("Actors");

                GetMovieImage getMovieImage = new GetMovieImage(moviePictureIV);
                getMovieImage.execute(poster);

                movieNameTV.setText(title);
                movieReleaseDateTV.setText(releaseDate);
                genreTV.setText(genre);
                directorTV.setText(director);
                countryTV.setText(country);
                plotTV.setText(plot);
                castTV.setText(actors);
                ratingBar = findViewById(R.id.detail_average_rating);
                ratingBar.setRating(Float.parseFloat(rating) / 20);

                movie.setReleaseDate(releaseDate);
                movie.setMovieName(title);
                Calendar c = Calendar.getInstance();
                movie.setTime(c.getTime().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class AddToWatchlist extends AsyncTask<Movie, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Movie... movies) {
            try {
                MovieDB.getInstance(getApplicationContext()).movieDao().insertAll(movie);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            String result = b ? "Add successfully!" : "Movie already exists!";
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

}
