package com.example.androidass.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Person implements Parcelable
{
    private int personId;
    private String firstname;
    private String surname;
    private Character gender;
    private Date dob;
    private String address;
    private String state;
    private int postcode;


    protected Person(Parcel in) {
        personId = in.readInt();
        firstname = in.readString();
        surname = in.readString();
        int tmpGender = in.readInt();
        gender = tmpGender != Integer.MAX_VALUE ? (char) tmpGender : null;
        address = in.readString();
        state = in.readString();
        postcode = in.readInt();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public Person(String firstname, String surname, Character gender, Date dob, String address, String state, int postcode) {
        this.firstname = firstname;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.postcode = postcode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(personId);
        dest.writeString(firstname);
        dest.writeString(surname);
        dest.writeInt(gender != null ? (int) gender : Integer.MAX_VALUE);
        dest.writeString(address);
        dest.writeString(state);
        dest.writeInt(postcode);
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int username) {
        this.personId = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public static Creator<Person> getCREATOR() {
        return CREATOR;
    }
}
