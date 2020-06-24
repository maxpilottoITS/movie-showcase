package com.maxpilotto.movieshowcase.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.protocols.MovieCellCallback;

import static com.maxpilotto.movieshowcase.util.Util.posterOf;

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
    public void bind(Movie movie, MovieCellCallback callback, boolean showActions) {
        Glide.with(context)
                .load(posterOf(movie.getPosterPath()))
                .error(R.drawable.noimg)
                .into(poster);

        root.setOnClickListener(v -> {
            callback.onClick(movie);
        });

        if (!showActions) {
            favourite.setVisibility(View.INVISIBLE);
            rate.setVisibility(View.INVISIBLE);
        }

        favourite.setImageResource(movie.getFavourite() ? R.drawable.ic_heart : R.drawable.ic_heart_outline);
        favourite.setOnClickListener(v -> {
            Boolean state = movie.toggleFavourite();

            ((ImageView)v).setImageResource(state ? R.drawable.ic_heart : R.drawable.ic_heart_outline);

            callback.onFavourite(movie);
        });

        rate.setOnClickListener(v -> {
            callback.onRate(movie);
        });
    }
}
