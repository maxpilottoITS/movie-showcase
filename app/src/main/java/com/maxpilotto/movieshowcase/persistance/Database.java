package com.maxpilotto.movieshowcase.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.protocols.Storable;

import java.util.List;

import static com.maxpilotto.movieshowcase.util.Util.rawQuery;

@Deprecated
public class Database {
    private static Database instance;
    private SQLiteDatabase database;

    public static void init(Context context) {
        instance = new Database();
        instance.database = new DatabaseHelper(context).getWritableDatabase();
    }

    public static Database get() {
        return instance;
    }

    private Database() {
    }

    public <T extends Storable> void insert(List<Storable> values, String table) {
        for (Storable value : values) {
            insert(value.values(), table);
        }
    }

    public long insert(ContentValues values, String table) {
        return database.insertWithOnConflict(
                table,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
        );
    }

    public void insertMovies(List<Movie> movies) {
        for (Movie m : movies) {
            insert(m.values(), MovieTable.NAME);
        }
    }

    /**
     * Returns a local movie for the given [id]
     */
    public Movie getLocalMovie(Integer id) {
        Cursor cursor = rawQuery(database, "SELECT * FROM movies WHERE _id=?", id);

        if (cursor.moveToFirst()) {
            Movie m = new Movie(cursor);

            cursor.close();
            return m;
        }

        cursor.close();
        return null;
    }

    /**
     * Returns the local movies
     *
     * @param count Number of movies required
     * @param offset Offset for the query
     */
    public List<Movie> getLocalMovies(Integer offset, Integer count) {
        Cursor cursor = rawQuery(database, "SELECT * FROM movies LIMIT ?,?", offset, count);

        return Movie.parseList(cursor);
    }
}
