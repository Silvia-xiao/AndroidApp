package com.example.androidass.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;

public class Credential implements Parcelable {
    private String username;
    private String paswd;
    private Date signupDate;
    private Person personId;


    public Credential(String username, String paswd,Person personId) {
        this.username = username;
        this.paswd = paswd;
        Calendar c = Calendar.getInstance();
        java.util.Date d = c.getTime();
        java.sql.Date date = new java.sql.Date(d.getTime());
        this.signupDate = date;
        this.personId = personId;
    }


    protected Credential(Parcel in) {
        username = in.readString();
        paswd = in.readString();
        personId = in.readParcelable(Person.class.getClassLoader());
    }

    public static final Creator<Credential> CREATOR = new Creator<Credential>() {
        @Override
        public Credential createFromParcel(Parcel in) {
            return new Credential(in);
        }

        @Override
        public Credential[] newArray(int size) {
            return new Credential[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(paswd);
        dest.writeParcelable(personId, flags);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaswd() {
        return paswd;
    }

    public void setPaswd(String paswd) {
        this.paswd = paswd;
    }

    public Date getSignup_date() {
        return signupDate;
    }

    public void setSignup_date(Date signup_date) {
        this.signupDate = signup_date;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }

    public static Creator<Credential> getCREATOR() {
        return CREATOR;
    }
}

