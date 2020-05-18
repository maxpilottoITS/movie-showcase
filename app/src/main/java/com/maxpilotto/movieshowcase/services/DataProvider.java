package com.maxpilotto.movieshowcase.services;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.maxpilotto.kon.JsonObject;
import com.maxpilotto.kon.net.JsonService;
import com.maxpilotto.kon.processor.EncodableProcessor;
import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.models.MovieDecoder;
import com.maxpilotto.movieshowcase.models.MovieEncoder;
import com.maxpilotto.movieshowcase.persistance.Database;
import com.maxpilotto.movieshowcase.persistance.MovieProvider;
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

    public void getMovies(ContentResolver contentResolver, Boolean requestNewData, Integer page, MovieUpdateCallback callback) {
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
                    List<Movie> remoteMovies = movieJson.getObjectList("results", json -> {
                        return MovieDecoder.decode(json);

//                        return new Movie(
//                                json.getInt("id"),
//                                json.optString("title", ""),
//                                json.optString("overview", ""),
//                                json.optCalendar("release_date", "yyyy-MM-dd", Locale.getDefault(), 0),
//                                posterOf(json.optString("poster_path", "")),
//                                coverOf(json.optString("backdrop_path", "")),
//                                json.optInt("vote_average", 0)
//                        );
                    });

//                    totalPages = movieJson.getInt("total_pages");
//                    totalResults = movieJson.getInt("total_results");

                    for (Movie m : remoteMovies) {
                        contentResolver.insert(MovieProvider.URI_MOVIES, m.values());
                    }
                }

                movies = Movie.parseList(
                        contentResolver.query(MovieProvider.URI_MOVIES, null, null, null, null)
                );

//                movies = database.getLocalMovies((page - 1) * getResultsPerPage(),getResultsPerPage());

                Log.d(App.TAG, "Loaded records: " + movies.size());
            }

            @Override
            public void onComplete() {
                callback.onLoad(movies);
            }
        });
    }
}
