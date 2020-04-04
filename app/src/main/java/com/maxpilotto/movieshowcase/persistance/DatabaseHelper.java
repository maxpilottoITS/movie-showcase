package com.maxpilotto.movieshowcase.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_MOVIES = "CREATE TABLE movies(" +
            "id INTEGER PRIMARY KEY," +
            "title TEXT," +
            "overview TEXT," +
            "releaseDate INTEGER," +
            "posterPath TEXT," +
            "coverPath TEXT," +
            "voteAverage INTEGER," +
            "favourite INTEGER," +
            "rating INTEGER)";
    private static final String TABLE_GENRES = "CREATE TABLE genres(" +
            "id INTEGER PRIMARY KEY," +
            "name TEXT)";
    private static final String TABLE_MOVIE_GENRES = "CREATE TABLE movie_genres(" +
            "id INTEGER PRIMARY KEY," +
            "movie INTEGER," +
            "genre INTEGER," +
            "FOREIGN KEY(movie) REFERENCES movies(id)," +
            "FOREIGN KEY(genre) REFERENCES genres(id))";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "MovieShowcase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_MOVIES);
        db.execSQL(TABLE_GENRES);
        db.execSQL(TABLE_MOVIE_GENRES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
