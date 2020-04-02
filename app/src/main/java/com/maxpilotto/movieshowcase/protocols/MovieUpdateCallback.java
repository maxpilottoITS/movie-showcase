package com.maxpilotto.movieshowcase.protocols;

import com.maxpilotto.movieshowcase.models.Movie;

import java.util.List;

public interface MovieUpdateCallback {
    void onLoad(List<Movie> movies);

//    void onUpdate(List<Movie> updatedCopy);

    default void update() { }
}
