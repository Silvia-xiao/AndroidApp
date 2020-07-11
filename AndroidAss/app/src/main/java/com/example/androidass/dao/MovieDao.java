package com.example.androidass.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidass.entity.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAll();

    @Query("SELECT * FROM movie WHERE movieId = :movieId LIMIT 1")
    Movie findByID(int movieId);

    @Insert//(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Movie... movies);

    @Insert
    long insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Update(onConflict = REPLACE)
    void updateMovies(Movie... movies);

    @Query("DELETE FROM movie")
    void deleteAll(); }