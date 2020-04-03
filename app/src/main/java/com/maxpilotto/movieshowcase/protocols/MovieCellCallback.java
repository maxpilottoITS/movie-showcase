package com.maxpilotto.movieshowcase.protocols;

import com.maxpilotto.movieshowcase.models.Movie;

/**
 * Callback which is invoked each time the user interacts with one of the
 * movie in the movie list
 */
public interface MovieCellCallback {
    void onClick(Movie item);

    void onFavourite(Movie item);

    void onRate(Movie item);
}
