package com.maxpilotto.movieshowcase.modals.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maxpilotto.movieshowcase.R;

public class ThemeDialog extends DialogFragment {
    private Callback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View content = getActivity().getLayoutInflater().inflate(R.layout.dialog_themes, null);
        RadioGroup group = content.findViewById(R.id.radioGroup);
        Dialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.themeTitle)
                .setView(content)
                .create();

        group.setOnCheckedChangeListener((group1, checkedId) -> {
            dialog.dismiss();

            if (callback != null) callback.onSelect(checkedId);
        });

        return dialog;
    }

    public ThemeDialog setCallback(Callback callback) {
        this.callback = callback;

        return this;
    }

    public interface Callback {
        void onSelect(int id);
    }
}
