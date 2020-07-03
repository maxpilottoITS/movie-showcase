package com.maxpilotto.movieshowcase.services;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.maxpilotto.kon.JsonObject;
import com.maxpilotto.kon.net.JsonService;
import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.models.MovieDecoder;
import com.maxpilotto.movieshowcase.persistance.MovieProvider;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.protocols.AsyncTaskSimpleCallback;
import com.maxpilotto.movieshowcase.protocols.MovieUpdateCallback;
import com.maxpilotto.movieshowcase.util.Routes;

import java.util.ArrayList;
import java.util.List;

import static com.maxpilotto.movieshowcase.util.Util.asyncTask;

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

    /**
     * Fetches the movies from the first record until pageCount * resultsPerPage
     */
    public void restoreMovies(ContentResolver contentResolver, Integer pageCount, MovieUpdateCallback callback) {
        asyncTask(new AsyncTaskSimpleCallback() {
            List<Movie> movies = new ArrayList<>();

            @Override
            public void run(AsyncTask task) {
                movies = Movie.parseList(contentResolver.query(
                        MovieProvider.URI_MOVIES,
                        null,
                        null,
                        null,
                        String.format("%s ASC LIMIT %d,%d",
                                MovieTable._ID,
                                0,
                                getResultsPerPage() * pageCount
                        )
                ));
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);
            }
        });
    }

    public void getMovies(ContentResolver contentResolver, Integer page, MovieUpdateCallback callback) {
        asyncTask(new AsyncTaskSimpleCallback() {
            List<Movie> movies = new ArrayList<>();

            @Override
            public void run(AsyncTask task) {
                if (!hasInternet()) {
                    Log.d(App.TAG, "Not connected, won't look for updates");
                } else {
                    JsonObject json = JsonService.fetchObject(Routes.discover(page));
                    List<Movie> remoteMovies = json.getObjectList("results", MovieDecoder::decode);

                    totalPages = json.getInt("total_pages");
                    totalResults = json.getInt("total_results");

                    for (Movie m : remoteMovies) {
                        //TODO Check if the movie is already there by searching the description and title together,
                        // since the ID from the API service is different from the one stored locally

                        contentResolver.insert(MovieProvider.URI_MOVIES, m.values());
                    }
                }

                movies = Movie.parseList(contentResolver.query(
                        MovieProvider.URI_MOVIES,
                        null,
                        null,
                        null,
                        String.format("%s ASC LIMIT %d,%d",
                                MovieTable._ID,
                                (page - 1) * getResultsPerPage(),
                                getResultsPerPage()
                        )
                ));
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
}
