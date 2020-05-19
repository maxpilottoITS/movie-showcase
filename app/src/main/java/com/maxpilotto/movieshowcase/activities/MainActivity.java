package com.maxpilotto.movieshowcase.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.adapters.MovieAdapter;
import com.maxpilotto.movieshowcase.modals.dialogs.RatingDialog;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.MovieProvider;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.protocols.MovieCellCallback;
import com.maxpilotto.movieshowcase.services.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ID_EXTRA = "movie.id.extra";
    public static final String DO_UPDATE_EXTRA = "service.do_update.extra";
    public static final String LAST_PAGE_EXTRA = "service.last_page.extra";

    private RecyclerView list;
    private MovieAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button refreshNoMovies;
    private List<Movie> dataSource;
    private DataProvider dataProvider;
    //    private Boolean shouldUpdate = true;
    private Integer lastPage = 1;
//    private ProgressDialog loadingDialog;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

//        outState.putBoolean(DO_UPDATE_EXTRA, shouldUpdate);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        list = findViewById(R.id.listView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        refreshNoMovies = findViewById(R.id.refreshNoMovies);

        dataSource = new ArrayList<>();
        adapter = new MovieAdapter(dataSource);
        dataProvider = DataProvider.get();

        restoreData(savedInstanceState);

        loadContent();

//        loadingDialog.show();

        dataProvider.getMovies(getContentResolver(), true, 1, movies -> {
//            dataSource.clear();
            dataSource.addAll(movies);

            adapter.notifyDataSetChanged();
//            shouldUpdate = false;

//            loadingDialog.dismiss();
        });
    }

    private void restoreData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
//            shouldUpdate = savedInstanceState.getBoolean(DO_UPDATE_EXTRA);
//            lastPage = savedInstanceState.getInt(LAST_PAGE_EXTRA);
        }
    }

    private void refreshDataSource() {
        if (!dataProvider.hasInternet()) {
            Toast.makeText(this, R.string.noInternet, Toast.LENGTH_LONG).show();

            return;
        }

//        loadingDialog.show();

        dataProvider.getMovies(getContentResolver(), true, 1, movies -> {
            dataSource.clear();
            dataSource.addAll(movies);

            adapter.notifyDataSetChanged();
//            shouldUpdate = false;

//            loadingDialog.dismiss();
        });
    }

    private void loadContent() {
//        loadingDialog = new ProgressDialog(this);
//        loadingDialog.setTitle(R.string.loading);
//        loadingDialog.setMessage(getString(R.string.loadingMessage));

        adapter.setEmptyView(findViewById(R.id.emptyView));
        adapter.setMovieCallback(new MovieCellCallback() {
            @Override
            public void onClick(Movie item) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
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

        refreshNoMovies.setOnClickListener(v -> {
            refreshDataSource();
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);

            refreshDataSource();
        });

        list.setAdapter(adapter);
        list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    lastPage++;
//                    loadingDialog.show();

                    dataProvider.getMovies(getContentResolver(), true, lastPage, movies -> {
                        dataSource.addAll(movies);

                        adapter.notifyDataSetChanged();

//                        loadingDialog.dismiss();
                    });
                    Log.d(App.TAG, "Reached the end");
                }

                //TODO Request new page
                // The getLocalMovie should return all the movies till (last page * 20)
                // The service should only return the 20 new movies
                // All of these numbers should be taken from the service
            }
        });

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                list.setLayoutManager(new GridLayoutManager(this, 2));
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                list.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
    }
}
