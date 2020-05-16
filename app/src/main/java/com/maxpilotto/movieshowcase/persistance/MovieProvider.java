package com.maxpilotto.movieshowcase.persistance;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.maxpilotto.movieshowcase.persistance.tables.GenreTable;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.persistance.tables.MovieWithGenresTable;

public class MovieProvider extends ContentProvider {
    public static final String AUTHORITY = "com.maxpilotto.movieshowcase.database.ContentProvider";

    public static final String BASE_PATH_MOVIES = "movies";
    public static final String BASE_PATH_GENRES = "genres";
    public static final String BASE_PATH_GENRES_FOR_MOVIE = "genres_for_movie";

    public static final int ALL_MOVIES = 100;
    public static final int SINGLE_MOVIE = 120;
    public static final int ALL_GENRES = 200;
    public static final int SINGLE_GENRE = 220;
    public static final int ALL_GENRES_FOR_MOVIE = 300;

    public static final String MIME_TYPE_ALL_HOTELS = ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.all_movies";
    public static final String MIME_TYPE_HOTEL = ContentResolver.CURSOR_ITEM_BASE_TYPE + "vnd.single_movie";
    public static final String MIME_TYPE_ALL_GENRES = ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.all_genres";
    public static final String MIME_TYPE_GENRE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "vnd.single_genre";
    public static final String MIME_TYPE_ALL_GENRES_FOR_MOVIE = ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.all_genres_for_movie";

    public static final Uri URI_MOVIES = Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/" + BASE_PATH_MOVIES);
    public static final Uri URI_GENRES = Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/" + BASE_PATH_GENRES);
    public static final Uri URI_GENRES_FOR_MOVIE = Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/" + BASE_PATH_GENRES_FOR_MOVIE);

    private DatabaseHelper database;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIES, ALL_MOVIES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIES + "/#", SINGLE_MOVIE);

        uriMatcher.addURI(AUTHORITY, BASE_PATH_GENRES, ALL_GENRES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_GENRES + "/#", SINGLE_GENRE);

        uriMatcher.addURI(AUTHORITY, BASE_PATH_GENRES_FOR_MOVIE, ALL_GENRES_FOR_MOVIE);
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = database.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case SINGLE_MOVIE:
                builder.setTables(MovieTable.NAME);
                builder.appendWhere(MovieTable.ID + " = " + uri.getLastPathSegment());
                break;

            case ALL_MOVIES:
                builder.setTables(MovieTable.NAME);
                break;

            case SINGLE_GENRE:
                builder.setTables(GenreTable.NAME);
                builder.appendWhere(GenreTable.ID + " = " + uri.getLastPathSegment());
                break;

            case ALL_GENRES:
                builder.setTables(GenreTable.NAME);
                break;

            case ALL_GENRES_FOR_MOVIE:
                builder.setTables(GenreTable.GENRES_JOIN_MOVIE);
//                builder.setTables(MovieTable.NAME);
//                builder.appendWhere(selection);

//                builder.appendWhere(MovieWithGenresTable.COLUMN_GENRE + " = " + GenreTable.NAME + "." + GenreTable.ID);
//                builder.appendWhere(MovieWithGenresTable.COLUMN_MOVIE + " = " + uri.getLastPathSegment());
//                builder.appendWhere(MovieTable.ID + " = " + uri.getLastPathSegment());

                break;
        }

        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SINGLE_MOVIE:
                return MIME_TYPE_HOTEL;

            case ALL_MOVIES:
                return MIME_TYPE_ALL_HOTELS;

            case SINGLE_GENRE:
                return MIME_TYPE_GENRE;

            case ALL_GENRES:
                return MIME_TYPE_ALL_GENRES;

            case ALL_GENRES_FOR_MOVIE:
                return MIME_TYPE_ALL_GENRES_FOR_MOVIE;
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) == ALL_MOVIES) {
            long result = database.getWritableDatabase().insert(MovieTable.NAME, null, values);
            String resultString = ContentResolver.SCHEME_CONTENT + "://" + BASE_PATH_MOVIES + "/" + result;

            getContext().getContentResolver().notifyChange(uri, null);

            return Uri.parse(resultString);
        } else if (uriMatcher.match(uri) == ALL_GENRES) {
            long result = database.getWritableDatabase().insert(GenreTable.NAME, null, values);
            String resultString = ContentResolver.SCHEME_CONTENT + "://" + BASE_PATH_GENRES + "/" + result;

            getContext().getContentResolver().notifyChange(uri, null);

            return Uri.parse(resultString);
        }else if (uriMatcher.match(uri) == ALL_GENRES_FOR_MOVIE) {
            long result = database.getWritableDatabase().insert(MovieWithGenresTable.NAME, null, values);
            String resultString = ContentResolver.SCHEME_CONTENT + "://" + BASE_PATH_GENRES_FOR_MOVIE + "/" + result;

            getContext().getContentResolver().notifyChange(uri, null);

            return Uri.parse(resultString);
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = "", query = "";
        SQLiteDatabase db = database.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SINGLE_MOVIE:
                table = MovieTable.NAME;
                query = MovieTable.ID + " = " + uri.getLastPathSegment();
                if (selection != null) {
                    query += " AND " + selection;
                }
                break;

            case ALL_MOVIES:
                table = MovieTable.NAME;
                query = selection;
                break;

            case SINGLE_GENRE:
                table = GenreTable.NAME;
                query = GenreTable.ID + " = " + uri.getLastPathSegment();
                if (selection != null) {
                    query += " AND " + selection;
                }
                break;

            case ALL_GENRES:
                table = GenreTable.NAME;
                query = selection;
                break;

            case ALL_GENRES_FOR_MOVIE:
                table = MovieWithGenresTable.NAME;
                query = selection;
                break;
        }

        int deletedRows = db.delete(table, query, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = "", query = "";
        SQLiteDatabase db = database.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SINGLE_MOVIE:
                table = MovieTable.NAME;
                query = MovieTable.ID + " = " + uri.getLastPathSegment();
                if (selection != null) {
                    query += " AND " + selection;
                }
                break;

            case ALL_MOVIES:
                table = MovieTable.NAME;
                query = selection;
                break;

            case SINGLE_GENRE:
                table = GenreTable.NAME;
                query = GenreTable.ID + " = " + uri.getLastPathSegment();
                if (selection != null) {
                    query += " AND " + selection;
                }
                break;

            case ALL_GENRES:
                table = GenreTable.NAME;
                query = selection;
                break;

            case ALL_GENRES_FOR_MOVIE:
                table = GenreTable.NAME + "," + MovieTable.NAME;
                query = selection;
                break;
        }

        int updatedRows = db.update(table, values, query, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return updatedRows;
    }
}
