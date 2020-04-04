package com.maxpilotto.movieshowcase.services;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.maxpilotto.kon.net.JsonService;
import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Genre;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.Database;
import com.maxpilotto.movieshowcase.protocols.AsyncTaskSimpleCallback;
import com.maxpilotto.movieshowcase.protocols.MovieUpdateCallback;
import com.maxpilotto.movieshowcase.util.Routes;
import com.maxpilotto.movieshowcase.util.Util;

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
    private ConnectivityManager connectivityManager;

    public static void init(Context context) {
        instance = new DataProvider();
        instance.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Routes.API_KEY = context.getString(R.string.api_key);
    }

    public static DataProvider get() {
        return instance;
    }

    private DataProvider() {
    }

    public void getMovies(MovieUpdateCallback callback) {
        final Database database = Database.get();

        // Task that fetches the data from the service and then saves
        // the data inside the local db
        AsyncTask remote = asyncTask(new AsyncTaskSimpleCallback() {
            List<Movie> movies;

            @Override
            public void run(AsyncTask task) {
                List<Genre> remoteGenres = JsonService
                        .fetchObject(Routes.genres())
                        .getObjectList("genres", jsonObject -> {
                            return new Genre(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name")
                            );
                        });
                List<Movie> remoteMovies = JsonService
                        .fetchObject(Routes.discover(1))
                        .getObjectList("results", jsonObject -> {
                            List<Integer> genreIds = jsonObject.getIntList("genre_ids");
                            List<Genre> genreList = new ArrayList<>();
                            Integer movieId = jsonObject.getInt("id");

                            for (Genre g : remoteGenres) {
                                if (genreIds.contains(g.getId())) {
                                    genreList.add(g);
                                }
                            }

                            database.insertMovieGenres(genreList, movieId);

                            return new Movie(
                                    movieId,
                                    jsonObject.getString("title"),
                                    jsonObject.getString("overview"),
                                    jsonObject.getCalendar("release_date", "yyyy-MM-dd", Locale.getDefault()),
                                    posterOf(jsonObject.getString("poster_path")),
                                    coverOf(jsonObject.getString("backdrop_path")),
                                    genreList,
                                    jsonObject.getInt("vote_average")
                            );
                        });

                database.insertOrUpdate(remoteMovies, "movies");
                database.insertOrUpdate(remoteGenres, "genres");

                movies = database.getLocalMovies();
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);
            }
        });

        // Task that loads the data from the local db
        // This data is not available on the first run
        asyncTask(true, new AsyncTaskSimpleCallback() {
            List<Movie> movies;

            @Override
            public void run(AsyncTask task) {
                movies = database.getLocalMovies();
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);

                // Update only if there's an active connection
                if (Util.isConnected(connectivityManager)) {
                    remote.execute();

                    Log.d(App.TAG, "Connected, will look for updates");
                } else {
                    Log.d(App.TAG, "Not connected, won't look for updates");
                }
            }
        });
    }
}
