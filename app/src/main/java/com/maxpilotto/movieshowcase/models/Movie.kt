package com.maxpilotto.movieshowcase.models

import java.net.URL
import java.util.*

/**
 * Movie model
 *
 * Retrieved from the `/discover/movie` endpoint
 *
 * @see <a href="api.themoviedb.org/3/discover/movie">genre/movie/list</a>
 */
data class Movie (
    val id: Long,
    val title: String,
    val overview: String,
    val releaseDate: Calendar,
    val posterPath: URL,
    val coverPath: URL,
    val genres: List<Genre>,
    val voteAverage: Int
)