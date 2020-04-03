package com.maxpilotto.movieshowcase.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    public static Boolean isConnected(ConnectivityManager cm) {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static AsyncTask asyncTask(AsyncTaskSimpleCallback callback) {
        AsyncTask task = new AsyncTask() {
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

        return task;
    }

    public static Cursor rawQuery(SQLiteDatabase database, String query, Object... args) {
        String[] arguments = new String[args.length];

        for (int i = 0; i < args.length; i++) {
            arguments[i] = args[i].toString();
        }

        return database.rawQuery(query,arguments);
    }

    public static Cursor rawQuery(SQLiteDatabase database, String query) {
        return rawQuery(database,query,new Object[0]);
    }
}
