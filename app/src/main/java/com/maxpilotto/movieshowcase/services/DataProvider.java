package com.maxpilotto.movieshowcase.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.maxpilotto.kon.JsonRequest;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Genre;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.MovieDatabaseHelper;
import com.maxpilotto.movieshowcase.protocols.AsyncTaskSimpleCallback;
import com.maxpilotto.movieshowcase.protocols.MovieResultCallback;
import com.maxpilotto.movieshowcase.protocols.MovieUpdateCallback;
import com.maxpilotto.movieshowcase.util.Routes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.maxpilotto.movieshowcase.util.Util.asyncTask;
import static com.maxpilotto.movieshowcase.util.Util.coverOf;
import static com.maxpilotto.movieshowcase.util.Util.posterOf;

/**
 * Data provider for this application
 */
public final class DataProvider {
    private static DataProvider instance;
    private SQLiteDatabase database;

    public static void init(Context context) {
        instance = new DataProvider();
        instance.database = new MovieDatabaseHelper(context).getWritableDatabase();

        Routes.API_KEY = context.getString(R.string.api_key);
    }

    public static DataProvider get() {
        return instance;
    }

    private DataProvider() {
    }

    public void getMovies(MovieUpdateCallback callback) {
        AsyncTask remote = asyncTask(false, new AsyncTaskSimpleCallback() {
            List<Genre> genres;
            List<Movie> movies;

            @Override
            public void run(AsyncTask task) {
                genres = JsonRequest
                        .fetchObjectSync(Routes.genres())
                        .getObjectList("genres", jsonObject -> {
                            return new Genre(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name")
                            );
                        });
                movies = JsonRequest
                        .fetchObjectSync(Routes.discover(1))
                        .getObjectList("results", jsonObject -> {
                            List<Integer> genreIds = jsonObject.getIntList("genre_ids");
                            List<Genre> genreList = new ArrayList<>();

                            for (Genre g : genres) {
                                if (genreIds.contains(g.getId())) {
                                    genreList.add(g);
                                }
                            }

                            return new Movie(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("title"),
                                    jsonObject.getString("overview"),
                                    jsonObject.getCalendar("release_date", "yyyy-MM-dd", Locale.getDefault()),
                                    posterOf(jsonObject.getString("poster_path")),
                                    coverOf(jsonObject.getString("backdrop_path")),
                                    genreList,
                                    jsonObject.getInt("vote_average")
                            );
                        });

                // Call the callback once again if data was changed

                //TODO Save genres to the db
                //TODO Save movies to the db
                //TODO Save movie_genres to the db
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);
            }
        });

        asyncTask(true, new AsyncTaskSimpleCallback() {
            List<Movie> movies;

            @Override
            public void run(AsyncTask task) {
                movies = getLocalMovies();
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);

                remote.execute();
            }
        });
    }

    public Movie getLocalMovie(Integer id) {
        Cursor cursor = database.rawQuery("SELECT * FROM movies WHERE id=" + id, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return null;
        } else {
            cursor.close();
            return new Movie(cursor);
        }
    }

    public List<Movie> getLocalMovies() {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM movies", null);

        while (cursor.moveToNext()) {
            movies.add(new Movie(cursor));
        }

        cursor.close();
        return movies;
    }

    public List<Genre> getMovieGenres(Integer movie) {
        List<Genre> genres = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM genres WHERE movie=" + movie, null);

        while (cursor.moveToNext()) {
            genres.add(new Genre(cursor));
        }

        cursor.close();
        return genres;
    }
}
