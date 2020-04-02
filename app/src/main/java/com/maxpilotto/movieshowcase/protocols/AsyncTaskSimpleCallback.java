package com.maxpilotto.movieshowcase.protocols;

import android.os.AsyncTask;

public interface AsyncTaskSimpleCallback {
    void run(AsyncTask task);

    void onComplete();
}
