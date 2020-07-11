package com.example.androidass;

import android.app.Activity;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidass.model.Credential;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;

import java.nio.charset.StandardCharsets;

//import android.net.wifi.hotspot2.pps.Credential;


public class MainActivity extends AppCompatActivity
{
    EditText usernameEV;
    EditText passwordEV;
    Button logInBT;
    Button signUpBT;
    CheckBox checkBox;
    NetworkConnection networkConnection=null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkConnection = new NetworkConnection();
        signUpBT = findViewById(R.id.sign_up_bt);
        logInBT = findViewById(R.id.login_bt);
        usernameEV = findViewById(R.id.email_et);
        passwordEV = findViewById(R.id.password_et);
        checkBox = findViewById(R.id.password_checkbox);

        //hide the password.
        passwordEV.setTransformationMethod(PasswordTransformationMethod.getInstance());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //is checked them show password.
                    passwordEV.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //otherwise hide password.
                    passwordEV.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        //click to sigh up as new user.
        signUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
            } });

        //get the new user information
        Intent it = getIntent();
        if (it.getExtras() != null)
        {
            Credential c = it.getExtras().getParcelable("credential");
            usernameEV.setText(c.getUsername());
            passwordEV.setText(c.getPaswd());
        }

        //click to log in the system.
        logInBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                //inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(validation()){
                    String username = usernameEV.getText().toString();
                    String password = passwordEV.getText().toString();
                    //Get the verifying information from database.
                    VerifyPerson verifyPerson = new VerifyPerson();
                    verifyPerson.execute(username, password);
                }
            }
        });

        }

    /*
    validate the username and password if they are blank or not.
     */
    private boolean validation()
    {
        if(usernameEV.length() == 0)
        {
            usernameEV.setError("Invalid input");
            return false;
        }
        if(passwordEV.length() == 0)
        {
            passwordEV.setError("Invalid input");
            return false;
        }
        return true;
    }



    private class VerifyPerson extends AsyncTask<String, Void, Person>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Person doInBackground(String... strings) {
            //MessageDigest messageDigest = null;
            try{
                //MessageDigest.getInstance("SHA-256");
                //byte[] hash = messageDigest.digest(strings[1].getBytes("UTF-8"));
                //String hash = Hex.encodeHexString(hash);

                //Hash the password user entered and then compare with database information.
                String hash = Hashing.sha256().hashString(strings[1], StandardCharsets.UTF_8).toString();
                String url = "credentials/findByUsernameAndPassword/" + strings[0] + "/" + hash;
                String s = networkConnection.getData(url);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd'T'HH:mm:ss.SSSXXX").create();
                Person p = gson.fromJson(s, Person.class);
                if(gson != null)
                    return p;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Person person) {
            super.onPostExecute(person);
            if(person == null) {
                Toast.makeText(getApplicationContext(), "Password or email is not correct!", Toast.LENGTH_LONG).show();
            }
            else{
                Intent it = new Intent(MainActivity.this, HomePage.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("personHome",person);
                it.putExtras(bundle);
                startActivity(it);
            }

        }
    }




}
