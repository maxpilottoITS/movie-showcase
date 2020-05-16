package com.maxpilotto.movieshowcase.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.maxpilotto.movieshowcase.persistance.tables.GenreTable;
import com.maxpilotto.movieshowcase.protocols.Storable;

import java.util.ArrayList;
import java.util.List;

/**
 * Genre related to a [Movie]
 *
 * Retrieved from the `genre/movie/list` endpoint
 *
 * @see <a href="api.themoviedb.org/3/genre/movie/list">genre/movie/list</a>
 */
public class Genre implements Storable {
    private Integer id;
    private String name;

    public static List<Genre> parseList(Cursor cursor) {
        List<Genre> genres = new ArrayList<>();

        while (cursor.moveToNext()) {
            genres.add(new Genre(cursor));
        }

        return genres;
    }

    public Genre(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(GenreTable.ID));
        this.name = cursor.getString(cursor.getColumnIndex(GenreTable.COLUMN_NAME));
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public ContentValues values() {
        ContentValues values = new ContentValues();

        values.put(GenreTable.ID,id);
        values.put(GenreTable.COLUMN_NAME,name);

        return values;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
