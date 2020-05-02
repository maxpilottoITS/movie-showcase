package com.maxpilotto.movieshowcase.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.maxpilotto.kon.JsonObject;
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
    private Integer totalResults = 0;
    private Integer totalPages = 0;

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
                    JsonObject movieJson = JsonService.fetchObject(Routes.discover(page));
                    List<Genre> remoteGenres = JsonService
                            .fetchObject(Routes.genres())
                            .getObjectList("genres", jsonObject -> {
                                return new Genre(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("name")
                                );
                            });
                    List<Movie> remoteMovies = movieJson.getObjectList("results", jsonObject -> {
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
                                jsonObject.optString("title",""),
                                jsonObject.optString("overview",""),
                                jsonObject.optCalendar("release_date", "yyyy-MM-dd", Locale.getDefault(), 0),
                                posterOf(jsonObject.optString("poster_path","")),
                                coverOf(jsonObject.optString("backdrop_path","")),
                                genreList,
                                jsonObject.optInt("vote_average",0)
                        );
                    });

                    totalPages = movieJson.getInt("total_pages");
                    totalResults = movieJson.getInt("total_results");

                    for (Movie m : remoteMovies) {
                        Log.d(App.TAG, "Poster: " + m.getPosterPath());
                    }

                    database.insertOrUpdate(remoteMovies, "movies");
                    database.insertOrUpdate(remoteGenres, "genres");
                }

                movies = database.getLocalMovies((page - 1) * getResultsPerPage(),getResultsPerPage());

                Log.d(App.TAG, "Loaded records: " + movies.size());
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);
            }
        });
    }

    public Integer getResultsPerPage() {
        return totalResults != 0 && totalPages != 0 ? totalResults / totalPages : 20;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }
}
