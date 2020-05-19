package com.maxpilotto.movieshowcase;

import android.app.Application;

import com.maxpilotto.movieshowcase.services.DataProvider;

public class App extends Application {
    public static final String TAG = "MovieShowcase";

    @Override
    public void onCreate() {
        super.onCreate();

        DataProvider.init(this);
    }
}
