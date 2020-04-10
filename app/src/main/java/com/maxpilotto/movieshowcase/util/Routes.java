package com.maxpilotto.movieshowcase.util;

public final class Routes {
    public static final String DISCOVER = "https://api.themoviedb.org/3/discover/movie";
    public static String API_KEY = "";

    public static String discover(Integer page) {
        return "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY +
                "&page=" + page +
                "&sort_by=release_date.desc";
    }

    public static String genres() {
        return "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY;
    }

    private Routes() {
    }
}
