package com.maxpilotto.movieshowcase.persistance.tables;

import android.provider.BaseColumns;

public class MovieWithGenresTable implements BaseColumns {
    public static final String ID = "id";
    public static final String COLUMN_MOVIE = "movie";
    public static final String COLUMN_GENRE = "genre";

    public static final String NAME = "movie_genres";
    public static final String CREATE = "CREATE TABLE " + NAME + "(" +
            ID + " INTEGER PRIMARY KEY," +
            COLUMN_MOVIE + " INTEGER," +
            COLUMN_GENRE + " INTEGER)";

}
