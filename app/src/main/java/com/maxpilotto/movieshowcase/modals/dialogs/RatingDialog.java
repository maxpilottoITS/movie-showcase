package com.maxpilotto.movieshowcase.modals.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maxpilotto.movieshowcase.R;

public class RatingDialog extends DialogFragment {
    private DialogCallback callback;
    private Integer rating;

    public RatingDialog(Integer rating) {
        this.rating = rating;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View content = getActivity().getLayoutInflater().inflate(R.layout.dialog_rating, null);
        RatingBar ratingBar = content.findViewById(R.id.ratingBar);

        ratingBar.setRating(rating);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.ratingTitle)
                .setView(content)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    callback.onRate(ratingBar.getProgress());
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
    }

    public void setCallback(DialogCallback callback) {
        this.callback = callback;
    }

    public interface DialogCallback {
        void onRate(int rating);
    }
}
