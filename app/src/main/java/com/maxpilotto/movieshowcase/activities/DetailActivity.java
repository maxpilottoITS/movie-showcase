package com.maxpilotto.movieshowcase.activities;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.maxpilotto.movieshowcase.App;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Genre;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.persistance.MovieProvider;
import com.maxpilotto.movieshowcase.persistance.tables.GenreTable;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.persistance.tables.MovieWithGenresTable;

import java.util.List;
import static com.maxpilotto.movieshowcase.util.Util.coverOf;
import static com.maxpilotto.movieshowcase.util.Util.posterOf;

public class DetailActivity extends AppCompatActivity {
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

        title = findViewById(R.id.title);
        overview = findViewById(R.id.overview);
        userRating = findViewById(R.id.userRating);
        personalRating = findViewById(R.id.personalRating);
        year = findViewById(R.id.year);

        orientation = getResources().getConfiguration().orientation;

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
        RequestManager glide = Glide.with(this);
        Integer id = getIntent().getIntExtra(MainActivity.ID_EXTRA, 0);
        Cursor cursor = getContentResolver().query(
                MovieProvider.URI_MOVIES,
                null,
                MovieTable.ID + "=" + id,
                null,
                null
        );
        cursor.moveToNext();

        Movie movie = new Movie(cursor);

        List<Genre> genres = Genre.parseList(getContentResolver().query(
                MovieProvider.URI_GENRES_FOR_MOVIE,
                new String[]{
                        GenreTable.NAME + "." + GenreTable.ID,
                        GenreTable.COLUMN_NAME
                },
                MovieWithGenresTable.COLUMN_MOVIE + "=" + id,
                null,
                null
        ));

        Log.d(App.TAG, "loadContent: " + genres.size());

        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        userRating.setText(getString(R.string.userRating, movie.getVoteAverage()));
        personalRating.setText(getString(R.string.yourRating, movie.getRating()));
        year.setText(getString(R.string.year, movie.getYear()));

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            glide.load(coverOf(movie.getCoverPath()))
                    .into((ImageView) findViewById(R.id.backdrop));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            glide.load(posterOf(movie.getPosterPath()))
                    .into((ImageView) findViewById(R.id.poster));
        }
    }
}
