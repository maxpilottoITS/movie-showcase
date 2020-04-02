package com.maxpilotto.movieshowcase.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.adapters.MovieAdapter;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.services.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView list;
    private MovieAdapter adapter;
    private List<Movie> dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        list = findViewById(R.id.listView);

        dataSource = new ArrayList<>();
        adapter = new MovieAdapter(dataSource);
        adapter.setClickListener(item -> {
            startActivity(new Intent(this, DetailActivity.class));
        });

        list.setLayoutManager(new GridLayoutManager(this, 2));
        list.setAdapter(adapter);

        DataProvider.get().getMovies(localCopy -> {
            dataSource.clear();
            dataSource.addAll(localCopy);

            adapter.notifyDataSetChanged();
        });
    }
}
