package com.maxpilotto.movieshowcase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.services.DataProvider;

public class DetailActivity extends AppCompatActivity {
    private ImageView backdrop;
    private TextView title;
    private TextView overview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backdrop = findViewById(R.id.backdrop);
        title = findViewById(R.id.title);
        overview = findViewById(R.id.overview);

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

        DataProvider.get().getMovie(id, movie -> {
            title.setText(movie.getTitle());
            overview.setText(movie.getOverview());

            Glide.with(this)
                    .load(movie.getCoverPath())
                    .into(backdrop);
        });
    }
}
