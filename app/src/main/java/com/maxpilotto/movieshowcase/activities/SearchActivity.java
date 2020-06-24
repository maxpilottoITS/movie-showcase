package com.maxpilotto.movieshowcase.activities;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maxpilotto.kon.JsonObject;
import com.maxpilotto.kon.net.JsonService;
import com.maxpilotto.movieshowcase.R;
import com.maxpilotto.movieshowcase.adapters.MovieAdapter;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.models.MovieDecoder;
import com.maxpilotto.movieshowcase.protocols.AsyncTaskSimpleCallback;
import com.maxpilotto.movieshowcase.util.Routes;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.maxpilotto.movieshowcase.util.Util.asyncTask;

public class SearchActivity extends ThemedActivity {
    private RecyclerView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emoji;
    private Switch adultSwitch;

    private MovieAdapter adapter;
    private List<Movie> dataSource;
    private TextView query;
    private TextView year;
    private Spinner langSpinner;
    private Button apply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        query = findViewById(R.id.query);
        year = findViewById(R.id.year);
        langSpinner = findViewById(R.id.langSpinner);

        dataSource = new ArrayList<>();
        adapter = new MovieAdapter(dataSource);
        adapter.setEmptyView(findViewById(R.id.emptyView));

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            apply.callOnClick();
        });

        adultSwitch = findViewById(R.id.adultSwitch);
        adultSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                emoji.setText("\uD83D\uDE08");
            } else {
                emoji.setText("\uD83D\uDE07");
            }
        });

        emoji = findViewById(R.id.emoji);
        emoji.setText("\uD83D\uDE07");

        apply = findViewById(R.id.apply);
        apply.setOnClickListener(v -> {
            if (year.getText().toString().isEmpty()) {
                Toast.makeText(this,getString(R.string.emptyYear),Toast.LENGTH_SHORT).show();

                return;
            }

            if (query.getText().toString().isEmpty()) {
                Toast.makeText(this,getString(R.string.emptyQuery),Toast.LENGTH_SHORT).show();

                return;
            }

            String route = buildRoute();

            asyncTask(new AsyncTaskSimpleCallback() {
                List<Movie> movies;

                @Override
                public void run(AsyncTask task) {
                    JsonObject json = JsonService.fetchObject(route);
                    movies = json.getObjectList("results", MovieDecoder::decode);
                }

                @Override
                public void onComplete() {
                    dataSource.clear();
                    dataSource.addAll(movies);
                    adapter.notifyDataSetChanged();
                }
            });
        });

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                listView.setLayoutManager(new GridLayoutManager(this, 2));
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                listView.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            if (item.getItemId() == android.R.id.home) {
                finish();

                return true;
            }
        }

        return false;
    }

    private String buildRoute() {
        return Routes.search(
                query.getText().toString(),
                langSpinner.getSelectedItem().toString(),
                adultSwitch.isChecked(),
                Integer.parseInt(year.getText().toString())
        );
    }
}
