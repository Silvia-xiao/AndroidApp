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

import com.example.androidass.model.Credential;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CredentialActivity extends AppCompatActivity {

    EditText newNameET;
    EditText newPasswordET;
    EditText confirmedPassword;
    Button submitBT;
    NetworkConnection networkConnection=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credential);

        submitBT = findViewById(R.id.final_sign_up_bt);
        newNameET = findViewById(R.id.newName_et);
        newPasswordET = findViewById(R.id.newPassword_et);
        confirmedPassword = findViewById(R.id.confirmedPassword_et);
        networkConnection = new NetworkConnection();

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newNameET.getText().toString();
                String password = newPasswordET.getText().toString();
                String cPassword = confirmedPassword.getText().toString();

                //verify the email and password and then post to the server database.
                if(isEmail(name)){
                    if(password.equals(cPassword) && password.length() >= 4){
                        Intent it = getIntent();
                        Bundle bundle = it.getExtras();
                        Person p = bundle.getParcelable("person");
                        Credential credential = new Credential(name, password, p);
                        PostToBackEndTask postToBackEndTask = new PostToBackEndTask();
                        postToBackEndTask.execute(credential);
                    }else{
                        Toast.makeText(CredentialActivity.this,"Password must be same and more than 4 characters.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(CredentialActivity.this,"Please enter valid email", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    /**
     * make sure the email that user input is valid email format.
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private class PostToBackEndTask extends AsyncTask<Credential, Void, Credential>{

        @Override
        protected Credential doInBackground(Credential... credentials) {

            Credential cre = credentials[0];
            String password = cre.getPaswd();
            //Hash the password when send to the database.
            String hash = Hashing.sha256().hashString(password, Charset.defaultCharset()).toString();
            cre.setPaswd(hash);
            String response = networkConnection.postData("credentials/newCredential",cre);
            if(response == null)
                credentials[0] = null;
            else
                cre.setPaswd(password);
            return credentials[0];
        }

        @Override
        protected void onPostExecute(Credential credential) {
            super.onPostExecute(credential);
            if(credential == null){
                Toast.makeText(CredentialActivity.this,"Please enter another email", Toast.LENGTH_LONG);
            }else {
                //pass the email and password to the login page so that user don't need to enter again.
                Intent it = new Intent(CredentialActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("credential", credential);
                it.putExtras(bundle);
                startActivity(it);
            }
        }

    }

    /*
    private class VerifyEmail extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String response = networkConnection.getCinema(strings[0]);
            return response;
        }

        @Override
        protected void onPostExecute(String str){
            if(str != null){
                Toast.makeText(CredentialActivity.this,"Please enter another email", Toast.LENGTH_LONG).show();
            }
        }
    }
     */
}
