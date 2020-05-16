package com.maxpilotto.movieshowcase.persistance.tables;

import android.provider.BaseColumns;

import com.maxpilotto.movieshowcase.models.Movie;

public class GenreTable implements BaseColumns {
    public static final String COLUMN_NAME = "name";

    public static final String ID = "id";
    public static final String NAME = "genres";
    public static final String GENRES_JOIN_MOVIE = NAME + " LEFT JOIN " + MovieWithGenresTable.NAME + " ON " + NAME + "." + ID + " = " + MovieWithGenresTable.COLUMN_GENRE;
    public static final String CREATE = "CREATE TABLE " + NAME + "(" +
            ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME + " TEXT);";
}
