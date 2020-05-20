package com.maxpilotto.movieshowcase.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.util.Settings;

public class ThemedActivity extends AppCompatActivity {
    protected SharedPreferences preferences;
    protected Integer currentTheme;

    @Override
    protected void onResume() {
        super.onResume();

        int theme = preferences.getInt(Settings.CURRENT_THEME,R.style.LightTheme);

        if (theme != currentTheme) {
            currentTheme = theme;
            recreate();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(Settings.DEFAULT_PREFERENCES, MODE_PRIVATE);
        currentTheme = preferences.getInt(Settings.CURRENT_THEME, R.style.LightTheme);

        setTheme(currentTheme);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggleTheme:
                currentTheme = currentTheme == R.style.LightTheme ? R.style.DarkTheme : R.style.LightTheme;
                preferences.edit().putInt(Settings.CURRENT_THEME, currentTheme).apply();

                recreate();
                break;
        }

        return true;
    }
}
