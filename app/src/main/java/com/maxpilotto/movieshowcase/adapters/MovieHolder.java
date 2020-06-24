package com.maxpilotto.movieshowcase.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.protocols.MovieCellCallback;

public abstract class MovieHolder extends RecyclerView.ViewHolder {
    protected View root;
    protected Context context;

    public MovieHolder(@NonNull View itemView) {
        super(itemView);

        this.root = itemView;
        this.context = itemView.getContext();
    }

    abstract public void bind(Movie item, MovieCellCallback callback,boolean showActions);
}
