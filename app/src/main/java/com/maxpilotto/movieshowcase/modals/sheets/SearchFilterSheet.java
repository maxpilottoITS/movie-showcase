package com.maxpilotto.movieshowcase.modals.sheets;

import android.content.Context;
import android.content.DialogInterface;
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
import com.maxpilotto.movieshowcase.activities.SearchActivity;

import java.util.Arrays;

public class SearchFilterSheet extends BottomSheetDialogFragment {
    private TextView emojiText;
    private Switch adultSwitch;
    private TextView yearText;
    private Spinner langSpinner;
    private String[] langCodes;
    private SearchActivity receiver;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof SearchActivity) {
            receiver = (SearchActivity) context;
        } else {
            throw new RuntimeException("Receiver activity is not of type SearchActivity");
        }
    }

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

        loadData();

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        String language = langSpinner.getSelectedItem().toString();
        String year = yearText.getText().toString();

        if (language.equals(getString(R.string.anyLang))) {
            language = "";
        } else {
            language = langCodes[langSpinner.getSelectedItemPosition()];
        }

        receiver.setLanguage(language);
        receiver.setAdultContent(adultSwitch.isChecked());
        receiver.setYear(year);
    }

    private void loadData() {
        langCodes = getResources().getStringArray(R.array.lang_codes);

        adultSwitch.setChecked(receiver.isAdultContent());
        yearText.setText(receiver.getYear());

        int index = Arrays.asList(langCodes).indexOf(receiver.getLanguage());
        langSpinner.setSelection(index);
    }
}
