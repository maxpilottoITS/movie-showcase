package com.maxpilotto.movieshowcase.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maxpilotto.kon.JsonObject;
import com.maxpilotto.kon.net.JsonService;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.adapters.MovieAdapter;
import com.maxpilotto.movieshowcase.modals.sheets.SearchFilterSheet;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.models.MovieDecoder;
import com.maxpilotto.movieshowcase.protocols.AsyncTaskSimpleCallback;
import com.maxpilotto.movieshowcase.protocols.MovieCellCallback;
import com.maxpilotto.movieshowcase.util.Routes;

import java.util.ArrayList;
import java.util.List;

import static com.maxpilotto.movieshowcase.util.Util.asyncTask;

public class SearchActivity extends ThemedActivity {
    private RecyclerView listView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MovieAdapter adapter;
    private List<Movie> dataSource;
    private EditText searchBar;
    private String language;
    private String year;
    private boolean adultContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupToolbar();

        dataSource = new ArrayList<>();
        adapter = new MovieAdapter(dataSource, false);
        adapter.setEmptyView(findViewById(R.id.emptyView));
        adapter.setMovieCallback(new MovieCellCallback() {
            @Override
            public void onClick(Movie item) {

            }

            @Override
            public void onFavourite(Movie item) {   //FIXME Hide these options
            }

            @Override
            public void onRate(Movie item) {
            }
        });

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::performSearch);

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                listView.setLayoutManager(new GridLayoutManager(this, 2));
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                listView.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.filter:
                new SearchFilterSheet().show(getSupportFragmentManager(), null);
        }

        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchBar = toolbar.findViewById(R.id.searchbar);
        searchBar.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch();

                return true;
            }

            return false;
        });
    }

    private void performSearch() {
        if (searchBar.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.emptyQuery), Toast.LENGTH_SHORT).show();
            return;
        }

        String route = Routes.search(
                searchBar.getText().toString(),
                language,
                adultContent,
                year
        );

        swipeRefreshLayout.setRefreshing(true);

        asyncTask(new AsyncTaskSimpleCallback() {
            List<Movie> movies;

            @Override
            public void run(AsyncTask task) {
                JsonObject json = JsonService.fetchObject(route);   //TODO try-catch

                Log.d("JSON", json.toString());

                movies = json.getObjectList("results", MovieDecoder::decode);
            }

            @Override
            public void onComplete() {
                dataSource.clear();
                dataSource.addAll(movies);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isAdultContent() {
        return adultContent;
    }

    public void setAdultContent(boolean adultContent) {
        this.adultContent = adultContent;
    }
}
