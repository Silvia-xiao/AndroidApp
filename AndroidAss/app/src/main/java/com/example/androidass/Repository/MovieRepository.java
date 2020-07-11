package com.example.androidass.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.androidass.dao.MovieDao;
import com.example.androidass.database.MovieDB;
import com.example.androidass.entity.Movie;

import java.util.List;

public class MovieRepository {
    private MovieDao dao;
    private LiveData<List<Movie>> allMovies;
    private Movie movie;

    public MovieRepository(Application application) {
        MovieDB db = MovieDB.getInstance(application);
        dao = db.movieDao();
    }

    public LiveData<List<Movie>> getAllMovies() {
        allMovies = dao.getAll();
        return allMovies;
    }

    public void insert(final Movie movie) {
        MovieDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(movie);
            }
        });
    }

    public void deleteAll() {
        MovieDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public void delete(final Movie movie) {
        MovieDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(movie);
            }
        });
    }

    public void insertAll(final Movie... movies) {
        MovieDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(movies);
            }
        });
    }

    public void updateMovies(final Movie... movies) {
        MovieDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateMovies();
            }
        });
    }

    public Movie findByID(final int movieId) {
        MovieDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Movie runMovie = dao.findByID(movieId);
                setMovie(runMovie);
            }
        });
        return movie;
    }

    public void setMovie(Movie movie ) {
        this.movie = movie;
    }
}



