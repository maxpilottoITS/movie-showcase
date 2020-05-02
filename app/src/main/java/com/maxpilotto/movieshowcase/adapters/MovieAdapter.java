package com.maxpilotto.movieshowcase.adapters;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.protocols.MovieCellCallback;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
    private List<Movie> source;
    private MovieCellCallback callback;
    private View emptyView;

    public MovieAdapter(List<Movie> source) {
        this.source = source;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_movie, parent, false);
        int orientation = parent.getContext().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return new PortraitMovieHolder(root);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return new LandscapeMovieHolder(root);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.bind(source.get(position), callback);
    }

    @Override
    public int getItemCount() {
        if (emptyView != null) {
            if (source.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.INVISIBLE);
            }
        }

        return source.size();
    }

    public Boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void setEmptyView(View view) {
        this.emptyView = view;
    }

    public void setMovieCallback(MovieCellCallback callback) {
        this.callback = callback;
    }
}
