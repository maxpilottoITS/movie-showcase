package com.maxpilotto.movieshowcase.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    public Boolean hasInternet() {
        NetworkInfo activeNetwork = instance.connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void getMovies(Boolean requestNewData, Integer page, MovieUpdateCallback callback) {
        final Database database = Database.get();

        asyncTask(true, new AsyncTaskSimpleCallback() {
            List<Movie> movies = new ArrayList<>();

            @Override
            public void run(AsyncTask task) {
                if (!requestNewData) {
                    Log.d(App.TAG, "No new data was requested, won't look for updates");
                } else if (!hasInternet()) {
                    Log.d(App.TAG, "Not connected, won't look for updates");
                } else {
                    List<Genre> remoteGenres = JsonService
                            .fetchObject(Routes.genres())
                            .getObjectList("genres", jsonObject -> {
                                return new Genre(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("name")
                                );
                            });
                    List<Movie> remoteMovies = JsonService
                            .fetchObject(Routes.discover(page))
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
                }

                movies = database.getLocalMovies(); //TODO Add LIMIT

                Log.d(App.TAG, "Loaded records: " + movies.size());
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);
            }
        });
    }
}
