package com.maxpilotto.movieshowcase.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.Database;
import com.maxpilotto.movieshowcase.services.DataProvider;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {
    private ImageView backdrop;
    private TextView title;
    private TextView overview;
    private TextView userRating;
    private TextView personalRating;
    private TextView year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backdrop = findViewById(R.id.backdrop);
        title = findViewById(R.id.title);
        overview = findViewById(R.id.overview);
        userRating = findViewById(R.id.userRating);
        personalRating = findViewById(R.id.personalRating);
        year = findViewById(R.id.year);

        loadContent();

        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void setupToolbar() {
        Toolbar bar = findViewById(R.id.toolbar);

        setSupportActionBar(bar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void loadContent() {
        Integer id = getIntent().getIntExtra(MainActivity.ID_EXTRA, 0);
        Movie movie = Database.get().getLocalMovie(id);     //TODO All db calls should be async

        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        userRating.setText(getString(R.string.userRating,movie.getVoteAverage()));
        personalRating.setText(getString(R.string.yourRating,movie.getRating()));
        year.setText(getString(R.string.year,movie.getYear()));

        Glide.with(this)
                .load(movie.getCoverPath())
                .into(backdrop);
    }
}
