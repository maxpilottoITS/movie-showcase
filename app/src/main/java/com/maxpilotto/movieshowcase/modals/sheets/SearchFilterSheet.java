package com.maxpilotto.movieshowcase.modals.sheets;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.maxpilotto.movieshowcase.R;

public class SearchFilterSheet extends BottomSheetDialogFragment {
    private TextView emojiText;
    private Switch adultSwitch;
    private TextView yearText;
    private Spinner langSpinner;
    private DismissCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sheet_search_filter, container, false);

        emojiText = v.findViewById(R.id.emojiText);
        adultSwitch = v.findViewById(R.id.adultSwitch);
        yearText = v.findViewById(R.id.yearText);
        langSpinner = v.findViewById(R.id.langSpinner);

        adultSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                emojiText.setText("\uD83D\uDE08");
            } else {
                emojiText.setText("\uD83D\uDE07");
            }
        });

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        String language = langSpinner.getSelectedItem().toString();
        String year = yearText.getText().toString();

        if (language.equals(getString(R.string.anyLang))) {
            language = "";
        }

        callback.onDismiss(
                language,
                adultSwitch.isChecked(),
                year
        );
    }

    public SearchFilterSheet setCallback(DismissCallback callback) {
        this.callback = callback;

        return this;
    }

    public interface DismissCallback {
        void onDismiss(String language, boolean adultContent, String year);
    }
}
