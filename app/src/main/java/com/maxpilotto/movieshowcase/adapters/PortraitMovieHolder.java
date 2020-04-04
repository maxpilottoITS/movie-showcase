package com.maxpilotto.movieshowcase.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.protocols.MovieCellCallback;

public class PortraitMovieHolder extends MovieHolder {
    private ImageView poster;
    private ImageView favourite;
    private ImageView rate;

    public PortraitMovieHolder(@NonNull View itemView) {
        super(itemView);

        this.poster = root.findViewById(R.id.poster);
        this.favourite = root.findViewById(R.id.favourite);
        this.rate = root.findViewById(R.id.rate);
    }

    @Override
    public void bind(Movie movie, MovieCellCallback callback) {
        Glide.with(context)
                .load(movie.getPosterPath())
                .into(poster);

        root.setOnClickListener(v -> {
            callback.onClick(movie);
        });

        favourite.setImageResource(movie.getFavourite() ? R.drawable.heart : R.drawable.heart_outline);
        favourite.setOnClickListener(v -> {
            Boolean state = movie.toggleFavourite();

            ((ImageView)v).setImageResource(state ? R.drawable.heart : R.drawable.heart_outline);

            callback.onFavourite(movie);
        });

        rate.setOnClickListener(v -> {
            callback.onRate(movie);
        });
    }
}
