package com.maxpilotto.movieshowcase.persistance.tables;

import android.provider.BaseColumns;

public class MovieTable implements BaseColumns {
    public static final String ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RELEASE_DATE = "releaseDate";
    public static final String COLUMN_POSTER_PATH = "posterPath";
    public static final String COLUMN_COVER_PATH = "coverPath";
    public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
    public static final String COLUMN_FAVOURITE = "favourite";
    public static final String COLUMN_RATING = "rating";

    public static final String NAME = "movies";
    public static final String CREATE = "CREATE TABLE " + NAME + "(" +
            ID + " INTEGER PRIMARY KEY," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_OVERVIEW + " TEXT," +
            COLUMN_RELEASE_DATE + " INTEGER," +
            COLUMN_POSTER_PATH + " TEXT," +
            COLUMN_COVER_PATH + " TEXT," +
            COLUMN_VOTE_AVERAGE + " INTEGER," +
            COLUMN_FAVOURITE + " INTEGER," +
            COLUMN_RATING + " INTEGER)";
}
