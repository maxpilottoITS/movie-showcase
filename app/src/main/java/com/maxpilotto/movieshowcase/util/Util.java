package com.maxpilotto.movieshowcase.util;

import android.os.AsyncTask;

import com.maxpilotto.movieshowcase.protocols.AsyncTaskSimpleCallback;

import java.util.Calendar;

public final class Util {
    private Util() {
    }

    public static Calendar calendarOf(long time) {
        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(time);

        return c;
    }

    public static String posterOf(String path) {
        return "https://image.tmdb.org/t/p/w500/" + path;
    }

    public static String coverOf(String path) {
        return "https://image.tmdb.org/t/p/original/" + path;
    }

    public static AsyncTask asyncTask(Boolean autoStart, AsyncTaskSimpleCallback callback) {
        AsyncTask task =  new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                callback.run(this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                callback.onComplete();
            }
        };

        if (autoStart){
            task.execute();
        }

        return task;
    }
}
