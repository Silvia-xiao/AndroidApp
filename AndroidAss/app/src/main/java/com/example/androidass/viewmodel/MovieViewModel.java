package com.example.androidass.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidass.Repository.MovieRepository;
import com.example.androidass.entity.Movie;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private MutableLiveData<List<Movie>> allMovies;
    public MovieViewModel () {
        allMovies = new MutableLiveData<>();
    }
    public void setMovies(List<Movie> movies) {
        allMovies.setValue(movies);
    }
    public LiveData<List<Movie>> getAllMovies() {
        return movieRepository.getAllMovies();
    }
    public void initializeVars(Application application){
        movieRepository = new MovieRepository(application);
    }
    public void insert(Movie movie) {
        movieRepository.insert(movie);
    }
    public void insertAll(Movie... movies) {
        movieRepository.insertAll(movies);
    }
    public void deleteAll() {
        movieRepository.deleteAll();
    }
    public void insertAll(Movie movie) {
        movieRepository.delete(movie);
    }
    public void update(Movie... movies) {
        movieRepository.updateMovies(movies);
    }
    public Movie insertAll(int id) {
        return movieRepository.findByID(id);
    }
    public Movie findByID(int movieId){
        return movieRepository.findByID(movieId);
    } }