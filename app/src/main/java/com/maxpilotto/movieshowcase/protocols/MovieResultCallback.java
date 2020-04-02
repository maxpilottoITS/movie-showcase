package com.maxpilotto.movieshowcase.protocols;

import com.maxpilotto.movieshowcase.models.Movie;

public interface MovieResultCallback {
    void onFind(Movie movie);
}
