package com.maxpilotto.movieshowcase.services;

import android.content.Context;

import com.maxpilotto.kon.JsonRequest;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Genre;
import com.maxpilotto.movieshowcase.models.Movie;
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

    public static void init(Context context) {
        instance = new DataProvider();

        Routes.API_KEY = context.getString(R.string.api_key);
    }

    public static DataProvider get() {
        return instance;
    }

    private DataProvider() {
    }

    public void getMovie(Integer id, MovieResultCallback callback) {
        getMovies(localCopy -> {
            for (Movie m : localCopy) {
                if (m.getId().equals(id)) {
                    callback.onFind(m);
                    return;
                }
            }

            callback.onFind(null);
        });
    }

    public void getMovies(MovieUpdateCallback callback) {
        asyncTask(new AsyncTaskSimpleCallback() {
            List<Genre> genres;
            List<Movie> movies;

            @Override
            public void run() {
                //TODO Load from db

                // Load the data from the db
                // Call the callback when done

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

                            for (Genre g : genres){
                                if (genreIds.contains(g.getId())){
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
    }
}
