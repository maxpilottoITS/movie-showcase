package com.maxpilotto.movieshowcase.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.maxpilotto.movieshowcase.protocols.Storable;

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

    public Genre(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.name = cursor.getString(cursor.getColumnIndex("name"));
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public ContentValues values() {
        ContentValues values = new ContentValues();

        values.put("id",id);
        values.put("name",name);

        return values;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
