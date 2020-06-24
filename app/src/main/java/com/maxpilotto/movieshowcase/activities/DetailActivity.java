package com.maxpilotto.movieshowcase.activities;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.MovieProvider;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;

import static com.maxpilotto.movieshowcase.util.Util.coverOf;
import static com.maxpilotto.movieshowcase.util.Util.posterOf;

public class DetailActivity extends ThemedActivity {
    public static final String ID_EXTRA = "movie.id.extra";

    private TextView title;
    private TextView overview;
    private TextView userRating;
    private TextView personalRating;
    private TextView year;
    private Integer orientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setupToolbar();

        title = findViewById(R.id.title);
        overview = findViewById(R.id.overview);
        userRating = findViewById(R.id.userRating);
        personalRating = findViewById(R.id.personalRating);
        year = findViewById(R.id.yearText);

        orientation = getResources().getConfiguration().orientation;

        loadContent();
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
        RequestManager glide = Glide.with(this);
        Integer id = getIntent().getIntExtra(ID_EXTRA, 0);
        Cursor cursor = getContentResolver().query(
                MovieProvider.URI_MOVIES,
                null,
                MovieTable._ID + "=" + id,
                null,
                null
        );
        cursor.moveToNext();

        Movie movie = new Movie(cursor);

        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        userRating.setText(getString(R.string.userRating, movie.getVoteAverage()));
        personalRating.setText(getString(R.string.yourRating, movie.getRating()));
        year.setText(getString(R.string.year, movie.getYear()));

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            glide.load(movie.getCoverPath().isEmpty() ? R.drawable.noimg : coverOf(movie.getCoverPath()))
                    .error(R.drawable.noimg)
                    .into((ImageView) findViewById(R.id.backdrop));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            glide.load(movie.getPosterPath().isEmpty() ? R.drawable.noimg : posterOf(movie.getPosterPath()))
                    .error(R.drawable.noimg)
                    .into((ImageView) findViewById(R.id.poster));
        }
    }
}
