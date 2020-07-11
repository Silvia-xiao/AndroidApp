package com.example.androidass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import com.example.androidass.model.Credential;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener
{
    Button pickDateButton;
    Button nextButton;
    EditText firstNameET;
    EditText surnameET;
    RadioGroup genderRG;
    TextView birthdayTV;
    String birthday;
    EditText addressET;
    EditText postcodeET;
    TextView stateTV;
    String states;
    NetworkConnection networkConnection = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        pickDateButton = findViewById(R.id.pickDate_bt);
        nextButton = findViewById(R.id.next_bt);
        firstNameET = findViewById(R.id.firstName_et);
        surnameET = findViewById(R.id.surname_et);
        genderRG = findViewById(R.id.gender_rd);
        birthdayTV = findViewById(R.id.birthday_tv);
        addressET = findViewById(R.id.address_et);
        postcodeET = findViewById(R.id.postcode_et);
        stateTV = findViewById(R.id.state_tv);
        pickDateButton.setOnClickListener(this);
        networkConnection = new NetworkConnection();

        //State Spinner
        List<String> list = new ArrayList<String>();
        list.add("VIC");
        list.add("QLD");
        list.add("NSW");
        list.add("WA");
        list.add("SA");
        list.add("TAS");
        list.add("ACT");
        final Spinner state = findViewById(R.id.state_spinner);
        //final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.state_array, R.layout.support_simple_spinner_dropdown_item);
        final ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                states = parent.getItemAtPosition(position).toString();
                if(states != null){
                    Toast.makeText(parent.getContext(), "State selected is " + states, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Next button to get to add credential information and the user information will be post to database.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameET.getText().toString();
                String surname = surnameET.getText().toString();
                String gender = ((RadioButton)findViewById(genderRG.getCheckedRadioButtonId())).getText().toString();
                String address = addressET.getText().toString();
                String postcodeTmp = postcodeET.getText().toString();

                //validate the user input data.
                if(firstName.length() != 0 && surname.length() !=0 )
                {
                    if (postcodeTmp.length() == 4 && isNumeric(postcodeTmp))
                    {
                        if(address.length() != 0) {
                            if(birthday != null) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                java.util.Date d = null;
                                try {
                                    d = format.parse(birthday);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //get the sql date to post to server side database
                                java.sql.Date date = new java.sql.Date(d.getTime());
                                int postcode = Integer.valueOf(postcodeTmp);
                                Person person = new Person(firstName, surname, gender.charAt(0), date, address, states, postcode);
                                PostToBackTask postToBackTask = new PostToBackTask();
                                postToBackTask.execute(person);
                            }else{
                                Toast.makeText(SignUpActivity.this, "Please pick your birthday", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(SignUpActivity.this, "Please enter valid address", Toast.LENGTH_LONG).show();
                        }
                    } else
                        Toast.makeText(SignUpActivity.this, "Please enter valid postcode", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SignUpActivity.this, "Please enter valid name", Toast.LENGTH_LONG).show();
                }

            } });
    }

    /**
     * To verify if the user in put is number or not.
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * To provide a picture view for user to select date.
     * @param v
     */
    @Override
    public void onClick(View v){
        Calendar c = Calendar.getInstance();
        DatePickerDialog dd = new DatePickerDialog(this, SignUpActivity.this, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        dd.getDatePicker().setMaxDate(c.getTimeInMillis());
        dd.show();
    }

    /**
     * Set the birthday.
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String birth = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);
        pickDateButton.setText(birth);
        birthday = birth;
        Toast.makeText(this, "Your birthday is " + birthday, Toast.LENGTH_LONG).show();

    }

    private class PostToBackTask extends AsyncTask<Person, Void, Person> {
        @Override
        protected Person doInBackground(Person... people) {
            Person p = people[0];
            //Post new user information to database.
            String response = networkConnection.postData("person/newPerson", p);
            try{
                JSONObject object = new JSONObject(response);
                String personId = object.getString("personId");
                p.setPersonId(Integer.parseInt(personId));
                return p;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Person person) {
            super.onPostExecute(person);
            //nextButton.setText(String.valueOf(person.getPersonId()));
            //Pass the person value to the credential information screen to get user's credential information.
            Bundle bundle = new Bundle();
            bundle.putParcelable("person", person);
            Intent intent = new Intent(SignUpActivity.this, CredentialActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }

    }


}
