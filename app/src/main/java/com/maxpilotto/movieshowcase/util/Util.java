package com.maxpilotto.movieshowcase.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        return !path.equals("null") ? "https://image.tmdb.org/t/p/w500/" + path : path;
    }

    public static String coverOf(String path) {
        return !path.equals("null") ? "https://image.tmdb.org/t/p/original/" + path : path;
    }

    public static AsyncTask asyncTask(Boolean autoStart, AsyncTaskSimpleCallback callback) {
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

        if (autoStart) {
            task.execute();
        }

        return task;
    }

    public static AsyncTask asyncTask(AsyncTaskSimpleCallback callback) {
        return asyncTask(true,callback);
    }

    public static Cursor rawQuery(SQLiteDatabase database, String query, Object... args) {
        String[] arguments = new String[args.length];

        for (int i = 0; i < args.length; i++) {
            arguments[i] = args[i].toString();
        }

        return database.rawQuery(query, arguments);
    }

    public static Cursor rawQuery(SQLiteDatabase database, String query) {
        return rawQuery(database, query, new Object[0]);
    }

    public static Integer scrollPositionOf(RecyclerView recyclerView) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

        if (lm instanceof GridLayoutManager) {
            return ((GridLayoutManager) lm).findFirstVisibleItemPosition();
        } else if (lm instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
        }

        return -1;
    }

    public static Boolean isNearTheEnd(RecyclerView recyclerView) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        Integer visibleItems = lm.getChildCount();
        Integer total = lm.getItemCount();
        Integer first = scrollPositionOf(recyclerView);
        Integer offset = 1;

        return (first + visibleItems >= (total - offset));
    }

    public static Integer percentageOf(Integer input, Integer discount) {
        return input - (input * discount) / 100;
    }
}
