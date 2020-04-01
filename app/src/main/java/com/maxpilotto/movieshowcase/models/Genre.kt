package com.maxpilotto.movieshowcase.models

/**
 * Genre related to a [Movie]
 *
 * Retrieved from the `genre/movie/list` endpoint
 *
 * @see <a href="api.themoviedb.org/3/genre/movie/list">genre/movie/list</a>
 */
data class Genre(
    val name: String
)