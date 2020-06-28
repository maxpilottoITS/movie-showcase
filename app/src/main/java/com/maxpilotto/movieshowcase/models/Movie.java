package com.maxpilotto.movieshowcase.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.maxpilotto.kon.annotations.JsonDate;
import com.maxpilotto.kon.annotations.JsonEncodable;
import com.maxpilotto.kon.annotations.JsonProperty;
import com.maxpilotto.movieshowcase.persistance.tables.MovieTable;
import com.maxpilotto.movieshowcase.protocols.Storable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.maxpilotto.movieshowcase.util.Util.calendarOf;

/**
 * Movie model
 * <p>
 * Movies are retrieved from the `/discover/movie` endpoint
 *
 * @see <a href="api.themoviedb.org/3/discover/movie">Discover</a>
 */
@JsonEncodable
public class Movie implements Storable {
    @JsonProperty(name = "id")
    private Integer remoteId;

    private String title;
    private String overview;

    @JsonDate
    @JsonProperty(name = "release_date", isOptional = true, defaultValue = "null")      //FIXME Crashes if the date is empty
    private Calendar releaseDate;

    @JsonProperty(name = "poster_path",isOptional = true)
    private String posterPath;

    @JsonProperty(name = "backdrop_path",isOptional = true)
    private String coverPath;

    @JsonProperty(name = "vote_average",isOptional = true, defaultValue = "0")
    private Integer voteAverage;

    @JsonProperty(isIgnored = true)
    private Boolean favourite;

    @JsonProperty(isIgnored = true)
    private Integer rating;

    @JsonProperty(isIgnored =  true)
    private Integer id;

    public static List<Movie> parseList(Cursor cursor) {
        List<Movie> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            list.add(new Movie(cursor));
        }

        return list;
    }

    public Movie(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(MovieTable._ID));
        this.remoteId = cursor.getInt(cursor.getColumnIndex(MovieTable.COLUMN_REMOTE_ID));
        this.title = cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_TITLE));
        this.overview = cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_OVERVIEW));
        this.releaseDate = calendarOf(cursor.getLong(cursor.getColumnIndex(MovieTable.COLUMN_RELEASE_DATE)));
        this.posterPath = cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_POSTER_PATH));
        this.coverPath = cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_COVER_PATH));
        this.voteAverage = cursor.getInt(cursor.getColumnIndex(MovieTable.COLUMN_VOTE_AVERAGE));
        this.favourite = cursor.getInt(cursor.getColumnIndex(MovieTable.COLUMN_FAVOURITE)) > 0;
        this.rating = cursor.getInt(cursor.getColumnIndex(MovieTable.COLUMN_RATING));
    }

    public Movie(Integer remoteId, String title, String overview, Calendar releaseDate, String posterPath, String coverPath,  Integer voteAverage) {
        this(0, remoteId,title, overview, releaseDate, posterPath, coverPath, voteAverage, false, 0);
    }

    public Movie(Integer id, Integer remoteId, String title, String overview, Calendar releaseDate, String posterPath, String coverPath, Integer voteAverage, Boolean favourite, Integer rating) {
        this.id = id;
        this.title = title;
        this.remoteId = remoteId;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.coverPath = coverPath;
        this.voteAverage = voteAverage;
        this.favourite = favourite;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", remoteId=" + remoteId +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate=" + releaseDate +
                ", posterPath='" + posterPath + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", voteAverage=" + voteAverage +
                ", favourite=" + favourite +
                ", rating=" + rating +
                '}';
    }

    @Override
    public ContentValues values() {
        ContentValues values = new ContentValues();

//        values.put(MovieTable._ID, id);
        values.put(MovieTable.COLUMN_REMOTE_ID,remoteId);
        values.put(MovieTable.COLUMN_TITLE, title);
        values.put(MovieTable.COLUMN_OVERVIEW, overview);
        values.put(MovieTable.COLUMN_RELEASE_DATE, releaseDate != null ? releaseDate.getTimeInMillis() : 0);
        values.put(MovieTable.COLUMN_POSTER_PATH, posterPath);
        values.put(MovieTable.COLUMN_COVER_PATH, coverPath);
        values.put(MovieTable.COLUMN_VOTE_AVERAGE, voteAverage);

        return values;
    }

    public ContentValues allValues() {
        ContentValues values = values();

        values.put(MovieTable.COLUMN_FAVOURITE, favourite);
        values.put(MovieTable.COLUMN_RATING, rating);

        return values;
    }

    public Integer getYear() {
        return releaseDate.get(Calendar.YEAR);
    }

    public Integer getRemoteId() {
        return remoteId;
    }

    public Boolean toggleFavourite() {
        favourite = !favourite;

        return favourite;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public Integer getVoteAverage() {
        return voteAverage;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
