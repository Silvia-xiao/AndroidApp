package com.example.androidass.model;

import java.sql.Date;

public class Memoir {
    private Integer memoirId;
    private String movieName;
    private Date movieReleaseDate;
    private Date userWatchDateTime;
    private String userComment;
    private double score;
    private Cinema cinemaId;
    private Person personId;
    private String imageurl;
    private String omdb;
    private double publicRating;

    public double getPublicRating() {
        return publicRating;
    }

    public void setPublicRating(double publicRating) {
        this.publicRating = publicRating;
    }

    public Memoir(String movieName, Date movieReleaseDate, Date userWatchDateTime, String userComment, double score, Cinema cinemaId, Person personId, String imageurl, String omdb, double publicRating) {
        this.movieName = movieName;
        this.movieReleaseDate = movieReleaseDate;
        this.userWatchDateTime = userWatchDateTime;
        this.userComment = userComment;
        this.score = score;
        this.cinemaId = cinemaId;
        this.personId = personId;
        this.imageurl = imageurl;
        this.omdb = omdb;
        this.publicRating = publicRating;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getOmdb() {
        return omdb;
    }

    public void setOmdb(String omdb) {
        this.omdb = omdb;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(Date movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public Date getUserWatchDateTime() {
        return userWatchDateTime;
    }

    public void setUserWatchDateTime(Date userWatchDateTime) {
        this.userWatchDateTime = userWatchDateTime;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Cinema getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Cinema cinemaId) {
        this.cinemaId = cinemaId;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }
}
