package com.maxpilotto.movieshowcase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.protocols.ListItemClickListener;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
    private List<Movie> source;
    private ListItemClickListener<Movie> clickListener;

    public MovieAdapter(List<Movie> source) {
        this.source = source;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_movie, parent, false);

        return new MovieHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.bind(source.get(position),clickListener);
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

    public void setClickListener(ListItemClickListener<Movie> clickListener) {
        this.clickListener = clickListener;
    }
}
