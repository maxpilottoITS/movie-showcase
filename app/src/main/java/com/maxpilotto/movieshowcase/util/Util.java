package com.maxpilotto.movieshowcase.util;

import java.util.Calendar;

public final class Util {
    private Util() {
    }

    public static Calendar calendarOf(long time) {
        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(time);

        return c;
    }

    public static String getPoster(String path) {
        return "https://image.tmdb.org/t/p/w500/" + path;
    }
}
