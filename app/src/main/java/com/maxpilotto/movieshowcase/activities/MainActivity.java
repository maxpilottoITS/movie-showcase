package com.maxpilotto.movieshowcase.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.adapters.MovieAdapter;
import com.maxpilotto.movieshowcase.modals.dialogs.RatingDialog;
import com.maxpilotto.movieshowcase.modals.sheets.ProgressSheet;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.MovieProvider;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.protocols.MovieCellCallback;
import com.maxpilotto.movieshowcase.services.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ThemedActivity {
    private static final String SHEET_TAG = "MainActivity.ProgressSheet.Tag";

    public static final String DO_UPDATE_EXTRA = "service.do_update.extra";
    public static final String LAST_PAGE_EXTRA = "service.last_page.extra";

    private RecyclerView list;
    private MovieAdapter adapter;
    private Button refreshNoMovies;
    private DataProvider dataProvider;
    private Boolean doUpdate = true;
    private Integer lastPage = 1;
    private ProgressSheet progressSheet;
    private List<Movie> dataSource = new ArrayList<>();
    private boolean listBusy = false;
    private int listPageThreshold;
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleCount = layoutManager.getChildCount();
            int total = layoutManager.getItemCount();
            int pastVisible = layoutManager.findFirstVisibleItemPosition();

            if (pastVisible + visibleCount >= total - listPageThreshold && !listBusy) {
                lastPage++;
                listBusy = true;

                setLoading(true);

                dataProvider.getMovies(getContentResolver(), lastPage, movies -> {
                    dataSource.addAll(movies);
                    adapter.notifyDataSetChanged();
                    listBusy = false;

                    setLoading(false);
                });

                Log.d(App.TAG, "Loading new page");
            }
        }
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(DO_UPDATE_EXTRA, doUpdate);
        outState.putInt(LAST_PAGE_EXTRA, lastPage);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        if (savedInstanceState != null) {
            doUpdate = savedInstanceState.getBoolean(DO_UPDATE_EXTRA, true);
            lastPage = savedInstanceState.getInt(LAST_PAGE_EXTRA, 1);
        }

        loadContent();

        setLoading(true);

        listBusy = true;

        if (doUpdate) {
            dataProvider.getMovies(getContentResolver(), 1, movies -> {
                dataSource.addAll(movies);
                adapter.notifyDataSetChanged();
                doUpdate = false;
                listBusy = false;

                setLoading(false);

                Log.d(App.TAG, "Movies loaded: " + dataSource.size());
            });
        } else {
            dataProvider.restoreMovies(getContentResolver(), lastPage, movies -> {
                dataSource.addAll(movies);
                adapter.notifyDataSetChanged();
                listBusy = false;

                setLoading(false);

                Log.d(App.TAG, "Movies loaded: " + dataSource.size());
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggleTheme:
                showThemeDialog();
                break;

            case R.id.favourites:
                startActivity(new Intent(this, FavouritesActivity.class));
                break;

            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }

        return true;
    }

    private void refreshDataSource() {
        if (!dataProvider.hasInternet()) {
            Toast.makeText(this, R.string.errorNoInternet, Toast.LENGTH_LONG).show();

            return;
        }

        setLoading(true);

        lastPage = 1;
        listBusy = true;

        dataProvider.getMovies(getContentResolver(), 1, movies -> {
            dataSource.clear();
            dataSource.addAll(movies);
            adapter.notifyDataSetChanged();
            listBusy = false;

            setLoading(false);
        });
    }

    private void setLoading(boolean loading) {
        if (loading) {
            if (getSupportFragmentManager().findFragmentByTag(SHEET_TAG) == null) {
                progressSheet.show(getSupportFragmentManager(), SHEET_TAG);
            }
        } else {
            progressSheet.dismiss();
        }
    }

    private void loadContent() {
        LinearLayoutManager layoutManager;

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                layoutManager = new GridLayoutManager(this, 2);
                listPageThreshold = 6;  // Around 3 rows
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                layoutManager = new LinearLayoutManager(this);
                listPageThreshold = 5;
                break;

            default:
                layoutManager = null;
                break;
        }

        progressSheet = new ProgressSheet();
        dataProvider = DataProvider.get();

        adapter = new MovieAdapter(dataSource);
        adapter.setEmptyView(findViewById(R.id.emptyView));
        adapter.setMovieCallback(new MovieCellCallback() {
            @Override
            public void onClick(Movie item) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.ID_EXTRA, item.getId());

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

        refreshNoMovies = findViewById(R.id.refreshNoMovies);
        refreshNoMovies.setOnClickListener(v -> {
            refreshDataSource();
        });

        list = findViewById(R.id.listView);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        list.setOnScrollListener(scrollListener);
    }
}
