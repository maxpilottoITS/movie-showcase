package com.maxpilotto.movieshowcase.util;

public final class Routes {
    public static final String BASE = "https://api.themoviedb.org/3/";
    public static final String DISCOVER = BASE + "discover/movie";
    public static final String SEARCH = BASE + "search/movie";
    public static String API_KEY = "";

    public static String discover(Integer page) {
        return DISCOVER +
                "?api_key=" + API_KEY +
                "&page=" + page;
    }

    public static String search(String query, String language, boolean includeAdult, int year) {
        return SEARCH +
                "?api_key=" + API_KEY +
                "&language=" + language +
                "&query=" + query +
                "&include_adult=" + includeAdult +
                "&year=" + year;
    }

    private Routes() {
    }
}
