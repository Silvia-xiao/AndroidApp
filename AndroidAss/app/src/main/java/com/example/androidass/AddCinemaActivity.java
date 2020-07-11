package com.example.androidass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidass.model.Cinema;
import com.example.androidass.model.Credential;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddCinemaActivity extends AppCompatActivity {

    EditText newNameET;
    EditText newLocationET;
    Button submitBT;
    NetworkConnection networkConnection=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cinema);

        submitBT = findViewById(R.id.cinema_submit_bt);
        newNameET = findViewById(R.id.cinema_name_et);
        newLocationET = findViewById(R.id.cinema_location_et);
        networkConnection = new NetworkConnection();

        //click the submit button to post the cinema information to the server side database.
        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newNameET.getText().toString();
                String location = newLocationET.getText().toString();

                if(name != null && location != null){
                Cinema cinema = new Cinema(name, location);
                PostToBackEndTask postToBackEndTask = new PostToBackEndTask();
                postToBackEndTask.execute(cinema);
                }else{
                    Toast.makeText(getApplicationContext(), "Name or location can not be blank", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class PostToBackEndTask extends AsyncTask<Cinema, Void, Cinema>{

        @Override
        protected Cinema doInBackground(Cinema... cinemas) {
            String response = networkConnection.postCinema("cinema/newCinema",cinemas[0]);
            if(response == null)
            {
                return null;}
            return cinemas[0];
        }

        @Override
        protected void onPostExecute(Cinema cinema) {
            super.onPostExecute(cinema);
            if(cinema == null){
                Toast.makeText(AddCinemaActivity.this,"Invalid information!", Toast.LENGTH_LONG);
            }else {
                Intent it = new Intent(AddCinemaActivity.this, AddNewMemoirActivity.class);
                Toast.makeText(getApplicationContext(),"Add cinema successful",Toast.LENGTH_LONG).show();
                //Bundle bundle = new Bundle();
                //bundle.putParcelable("credential", credential);
                //it.putExtras(bundle);
                startActivity(it);
            }
        }

    }
}

