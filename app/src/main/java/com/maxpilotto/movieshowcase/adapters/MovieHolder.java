package com.maxpilotto.movieshowcase.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.protocols.ListItemClickListener;

public class MovieHolder extends RecyclerView.ViewHolder {
    private Context context;
    private View root;
    private ImageView poster;

    public MovieHolder(@NonNull View itemView) {
        super(itemView);

        this.context = itemView.getContext();
        this.root = itemView;
        this.poster = itemView.findViewById(R.id.poster);
    }

    public void bind(Movie movie,ListItemClickListener<Movie> clickListener) {
        Glide.with(context)
                .load(movie.getPosterPath())
                .into(poster);

        root.setOnClickListener(v -> {
            clickListener.onItemClick(movie);
        });
    }
}
