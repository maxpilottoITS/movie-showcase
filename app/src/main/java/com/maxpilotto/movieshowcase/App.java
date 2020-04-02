package com.maxpilotto.movieshowcase;

import android.app.Application;

import com.maxpilotto.movieshowcase.services.DataProvider;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DataProvider.init(this);
    }
}
