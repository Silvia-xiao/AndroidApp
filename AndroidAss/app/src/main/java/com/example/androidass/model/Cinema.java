package com.example.androidass.model;

import org.jetbrains.annotations.NotNull;

public class Cinema {
    private Integer cinemaId;
    private String cinemaName;
    private String location;

    public Cinema(String cinemaName, String location) {
        this.cinemaName = cinemaName;
        this.location = location;
    }

    public Cinema(Integer cinemaId, String cinemaName, String location) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.location = location;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @NotNull
    @Override
    public String toString(){
        return cinemaName + " " + location;
    }
}
