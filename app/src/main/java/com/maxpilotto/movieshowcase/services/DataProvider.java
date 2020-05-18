package com.maxpilotto.movieshowcase.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.maxpilotto.kon.net.JsonService;
import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.models.MovieDecoder;
import com.maxpilotto.movieshowcase.persistance.MovieProvider;
import com.maxpilotto.movieshowcase.protocols.AsyncTaskSimpleCallback;
import com.maxpilotto.movieshowcase.protocols.MovieUpdateCallback;
import com.maxpilotto.movieshowcase.util.Routes;

import java.util.ArrayList;
import java.util.List;

import static com.maxpilotto.movieshowcase.util.Util.asyncTask;

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

    public void getMovies(ContentResolver contentResolver, Boolean requestNewData, Integer page, MovieUpdateCallback callback) {
        asyncTask(true, new AsyncTaskSimpleCallback() {
            List<Movie> movies = new ArrayList<>();

            @Override
            public void run(AsyncTask task) {
                if (!requestNewData) {
                    Log.d(App.TAG, "No new data was requested, won't look for updates");
                } else if (!hasInternet()) {
                    Log.d(App.TAG, "Not connected, won't look for updates");
                } else {
                    List<Movie> remoteMovies = JsonService
                            .fetchObject(Routes.discover(page))
                            .getObjectList("results", MovieDecoder::decode);

                    for (Movie m : remoteMovies) {
                        contentResolver.insert(MovieProvider.URI_MOVIES, m.values());
                    }
                }

                Cursor cursor = contentResolver.query(MovieProvider.URI_MOVIES, null, null, null, null);

                movies = Movie.parseList(cursor);

                cursor.close();

                Log.d(App.TAG, "Loaded records: " + movies.size());
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);
            }
        });
    }
}
