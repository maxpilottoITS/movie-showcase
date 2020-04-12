package com.maxpilotto.movieshowcase.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.models.Genre;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.protocols.Storable;

import java.util.ArrayList;
import java.util.List;

import static com.maxpilotto.movieshowcase.util.Util.rawQuery;

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

    public <T extends Storable> void insertOrUpdate(List<T> values, String table) {
        for (Storable value : values) {
            insertOrUpdate(value.values(), table);
        }
    }

    public <T extends Storable> void insert(List<Storable> values, String table) {
        for (Storable value : values) {
            insert(value.values(), table);
        }
    }

    public void insertOrUpdate(ContentValues values, String table) {
        if (insert(values, table) == -1) {
            update(values, table);
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

    public void update(ContentValues values, String table) {
        int result = database.update(table, values, "id=?", new String[]{values.getAsString("id")});
    }

    public void insertMovies(List<Movie> movies) {
        for (Movie m : movies) {
            insert(m.values(), "movies");
        }
    }

    public void insertGenres(List<Genre> genres) {
        for (Genre g : genres) {
            insert(g.values(), "genres");
        }
    }

    public void insertMovieGenres(List<Genre> genres, Integer movieId) {
        for (Genre g : genres) {
            ContentValues values = new ContentValues();

            values.put("movie", movieId);
            values.put("genre", g.getId());

            insert(values, "movie_genres");
        }
    }

    /**
     * Returns a local movie for the given [id]
     */
    public Movie getLocalMovie(Integer id) {
        Cursor cursor = rawQuery(database, "SELECT * FROM movies WHERE id=?", id);

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
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = rawQuery(database, "SELECT * FROM movies ORDER BY releaseDate LIMIT ?,?", offset, count);

        while (cursor.moveToNext()) {
            movies.add(new Movie(cursor));
        }

        cursor.close();
        return movies;
    }

    public List<Genre> getMovieGenres(Integer movie) {
        List<Genre> genres = new ArrayList<>();
        Cursor cursor = rawQuery(
                database,
                "SELECT * FROM genres,movie_genres WHERE movie_genres.genre = genres.id AND movie_genres.movie=?",
                movie
        );

        while (cursor.moveToNext()) {
            genres.add(new Genre(cursor));
        }

        cursor.close();
        return genres;
    }
}
