package com.maxpilotto.movieshowcase.modals.sheets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.activities.SearchActivity;

import java.util.Arrays;

public class SearchFilterSheet extends BottomSheetDialogFragment {
    private TextView emojiText;
    private Switch adultSwitch;
    private Switch offlineSwitch;
    private TextView yearText;
    private Spinner langSpinner;
    private Spinner regionSpinner;
    private String[] langCodes;
    private String[] regionCodes;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);

        d.setOnShowListener(dialog -> {
            FrameLayout sheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior behavior = BottomSheetBehavior.from(sheet);

            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        });

        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sheet_search_filter, container, false);

        emojiText = v.findViewById(R.id.emojiText);
        adultSwitch = v.findViewById(R.id.adultSwitch);
        yearText = v.findViewById(R.id.yearText);
        langSpinner = v.findViewById(R.id.langSpinner);
        regionSpinner = v.findViewById(R.id.regionSpinner);
        offlineSwitch = v.findViewById(R.id.offlineSwitch);

        adultSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                emojiText.setText("\uD83D\uDE08");
            } else {
                emojiText.setText("\uD83D\uDE07");
            }
        });
        offlineSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            receiver.setOfflineSearch(isChecked);
        });

        loadData();

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        String language = langSpinner.getSelectedItem().toString();
        String year = yearText.getText().toString();
        String region = regionSpinner.getSelectedItem().toString();

        if (language.equals(getString(R.string.anyLang))) {
            language = "";
        } else {
            language = langCodes[langSpinner.getSelectedItemPosition()];
        }

        if (region.equals(getString(R.string.anyLang))) {
            region = "";
        }else {
            region = regionCodes[regionSpinner.getSelectedItemPosition()];
        }

        receiver.setLanguage(language);
        receiver.setAdultContent(adultSwitch.isChecked());
        receiver.setYear(year);
        receiver.setRegion(region);
        receiver.setOfflineSearch(offlineSwitch.isChecked());
    }

    private void loadData() {
        langCodes = getResources().getStringArray(R.array.lang_codes);
        regionCodes = getResources().getStringArray(R.array.region_codes);

        langSpinner.setSelection(Arrays.asList(langCodes).indexOf(receiver.getLanguage()));
        regionSpinner.setSelection(Arrays.asList(regionCodes).indexOf(receiver.getRegion()));

        adultSwitch.setChecked(receiver.isAdultContent());
        offlineSwitch.setChecked(receiver.isOfflineSearch());
        yearText.setText(receiver.getYear());
    }
}
