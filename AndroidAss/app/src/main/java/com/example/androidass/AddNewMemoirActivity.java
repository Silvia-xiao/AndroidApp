package com.example.androidass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.androidass.Utility.GetMovieImage;
import com.example.androidass.model.Cinema;
import com.example.androidass.model.Memoir;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNewMemoirActivity extends AppCompatActivity {
    NetworkConnection networkConnection=null;
    Calendar date;
    double  rating;
    String comment;
    ArrayAdapter<Cinema> arrayAdapter;
    Cinema cinemaSelected;
    Spinner location;
    Cinema cinema;
    String pickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_memoir);

        final Button pickDateBT = findViewById(R.id.add_pickDate_bt);
        Button submitBT = findViewById(R.id.add_submit_bt);
        Button addCinemaBT= findViewById(R.id.add_add_cinema);
        final RatingBar ratingBar = findViewById(R.id.add_rating_bar);
        location = findViewById(R.id.add_location_spinner);
        TextView movieNameTV = findViewById(R.id.add_memoir_title_tv);
        TextView releaseDateTV = findViewById(R.id.add_release_date_tv);
        ImageView imageIV = findViewById(R.id.add_movie_image_iv);
        final EditText commentET = findViewById(R.id.add_comment_et);

        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        final String name = it.getStringExtra("Title");
        final String release = it.getStringExtra("Release");
        final String movieId = it.getStringExtra("MovieId");
        final String poster = it.getStringExtra("Poster");
        final Person person = bundle.getParcelable("personView");
        final Float ratingPublic = it.getFloatExtra("PublicRating",0);

        //get movie image from omdb api
        new GetMovieImage(imageIV).execute(poster);
        movieNameTV.setText(name);
        releaseDateTV.setText(release);
        networkConnection = new NetworkConnection();
        GetCinema getCinema = new GetCinema();
        getCinema.execute();
        pickDateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GetCinema getCinema = new GetCinema();
                //getCinema.execute();
                cinemaSelected = (Cinema) parent.getItemAtPosition(position);
                if(cinemaSelected != null){
                    Toast.makeText(parent.getContext(), "State selected is " + cinemaSelected.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
                try {
                    rating = ratingBar.getRating();
                    java.util.Date r = format.parse(release);
                    java.sql.Date re = new java.sql.Date(r.getTime());
                    java.util.Date watch = date.getTime();
                    java.sql.Date w = new java.sql.Date(watch.getTime());
                    comment = commentET.getText().toString();
                    Memoir memoir = new Memoir(name,re,w,comment,rating,cinema,person,poster,movieId,(double)ratingPublic);
                    PostMemoir postMemoir = new PostMemoir();
                    postMemoir.execute(memoir);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        addCinemaBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewMemoirActivity.this,AddCinemaActivity.class);
                startActivity(intent);
            }
        });


    }

    class GetCinema extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String result = networkConnection.getData("cinema");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str)
        {
            super.onPostExecute(str);
            List<Cinema> cinemas= new ArrayList<>();
            try{
                JSONArray jsonArray = new JSONArray(str);
                for(int i = 0; i< jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("cinemaId");
                    String cinemaName = jsonObject.getString("cinemaName");
                    String location = jsonObject.getString("location");
                    cinema = new Cinema(id,cinemaName,location);
                    cinemas.add(cinema);
                    //cinemaLocation.add(cinema.getLocation());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            arrayAdapter = new ArrayAdapter<>(AddNewMemoirActivity.this, android.R.layout.simple_spinner_item,cinemas);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            location.setAdapter(arrayAdapter);

        }
    }

    /**
     * Use date time picture to let use choose the date and time they watched the movie
     */
    public void showDateTimePicker()
    {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(AddNewMemoirActivity.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(AddNewMemoirActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    class PostMemoir extends AsyncTask<Memoir,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Memoir... memoirs) {
            try {
                networkConnection.postData("memoir",memoirs[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b){
            super.onPostExecute(b);
            if(b){
                Toast.makeText(getApplicationContext(),"Add memoir successful",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }






}
