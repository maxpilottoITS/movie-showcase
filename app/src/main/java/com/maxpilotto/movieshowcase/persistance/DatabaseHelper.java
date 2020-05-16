package com.maxpilotto.movieshowcase.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.maxpilotto.movieshowcase.persistance.tables.GenreTable;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.persistance.tables.MovieWithGenresTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "MovieShowcase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MovieTable.CREATE);
        db.execSQL(GenreTable.CREATE);
        db.execSQL(MovieWithGenresTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
