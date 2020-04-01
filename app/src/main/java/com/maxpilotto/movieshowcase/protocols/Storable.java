package com.maxpilotto.movieshowcase.protocols;

import android.content.ContentValues;

/**
 * Interface for objects that can be stored in a SQLite DB
 */
public interface Storable {
    ContentValues values();
}
