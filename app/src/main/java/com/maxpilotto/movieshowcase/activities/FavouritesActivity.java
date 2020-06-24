package com.maxpilotto.movieshowcase.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.adapters.MovieAdapter;
import com.maxpilotto.movieshowcase.modals.dialogs.RatingDialog;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.MovieProvider;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.protocols.MovieCellCallback;

import java.util.ArrayList;
import java.util.List;

import static com.maxpilotto.movieshowcase.activities.DetailActivity.ID_EXTRA;

public class FavouritesActivity extends ThemedActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int LOADER_ID = 124515131;

    private RecyclerView list;
    private MovieAdapter adapter;
    private List<Movie> dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.favourites));

        dataSource = new ArrayList<>();

        adapter = new MovieAdapter(dataSource);
        adapter.setEmptyView(findViewById(R.id.emptyView));
        adapter.setMovieCallback(new MovieCellCallback() {
            @Override
            public void onClick(Movie item) {
                Intent intent = new Intent(FavouritesActivity.this, DetailActivity.class);
                intent.putExtra(ID_EXTRA, item.getId());

                startActivity(intent);
            }

            @Override
            public void onFavourite(Movie item) {
                getContentResolver().update(MovieProvider.URI_MOVIES, item.allValues(), MovieTable._ID + "=" + item.getId(), null);
            }

            @Override
            public void onRate(Movie item) {
                RatingDialog dialog = new RatingDialog(item.getRating());

                dialog.setCallback(rating -> {
                    item.setRating(rating);

                    getContentResolver().update(MovieProvider.URI_MOVIES, item.allValues(), MovieTable._ID + "=" + item.getId(), null);
                });
                dialog.show(getSupportFragmentManager(), null);
            }
        });

        list = findViewById(R.id.listView);
        list.setAdapter(adapter);

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                list.setLayoutManager(new GridLayoutManager(this, 2));
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                list.setLayoutManager(new LinearLayoutManager(this));
                break;
        }

        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,MovieProvider.URI_MOVIES,null,MovieTable.COLUMN_FAVOURITE + "=1",null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        dataSource.clear();
        dataSource.addAll(Movie.parseList(data));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        dataSource.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            if (item.getItemId() == android.R.id.home) {
                finish();

                return true;
            }
        }

        return false;
    }
}
