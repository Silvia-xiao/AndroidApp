package com.example.androidass.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.androidass.dao.MovieDao;
import com.example.androidass.entity.Movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class MovieDB extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static MovieDB INSTANCE;

    //we create an ExecutorService with a fixed thread pool so we can later run database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized MovieDB getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDB.class, "MovieDatabase") .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE; }
}