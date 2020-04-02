package com.maxpilotto.movieshowcase.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.maxpilotto.movieshowcase.protocols.Storable;
import com.maxpilotto.movieshowcase.services.DataProvider;

import java.util.Calendar;
import java.util.List;

import static com.maxpilotto.movieshowcase.util.Util.calendarOf;

/**
 * Movie model
 * <p>
 * Movies are retrieved from the `/discover/movie` endpoint
 *
 * @see <a href="api.themoviedb.org/3/discover/movie">genre/movie/list</a>
 */
public class Movie implements Storable {
    private Integer id;
    private String title;
    private String overview;
    private Calendar releaseDate;
    private String posterPath;
    private String coverPath;
    private List<Genre> genres;
    private Integer voteAverage;
    private Boolean starred;
    private Integer rating;

    public Movie(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.title = cursor.getString(cursor.getColumnIndex("title"));
        this.overview = cursor.getString(cursor.getColumnIndex("overview"));
        this.releaseDate = calendarOf(cursor.getLong(cursor.getColumnIndex("releaseDate")));
        this.posterPath = cursor.getString(cursor.getColumnIndex("posterPath"));
        this.coverPath = cursor.getString(cursor.getColumnIndex("coverPath"));
        this.voteAverage = cursor.getInt(cursor.getColumnIndex("voteAverage"));
        this.genres = DataProvider.get().getMovieGenres(id);
        this.starred = cursor.getInt(cursor.getColumnIndex("starred")) > 0;
        this.rating = cursor.getInt(cursor.getColumnIndex("rating"));
    }

    public Movie(Integer id, String title, String overview, Calendar releaseDate, String posterPath, String coverPath, List<Genre> genres, Integer voteAverage) {
        this(id, title, overview, releaseDate, posterPath, coverPath, genres, voteAverage, false, 0);
    }

    public Movie(Integer id, String title, String overview, Calendar releaseDate, String posterPath, String coverPath, List<Genre> genres, Integer voteAverage, Boolean starred, Integer rating) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.coverPath = coverPath;
        this.genres = genres;
        this.voteAverage = voteAverage;
        this.starred = starred;
        this.rating = rating;
    }

    @Override
    public ContentValues values() {
        ContentValues values = new ContentValues();

        values.put("id", id);
        values.put("title", title);
        values.put("overview", overview);
        values.put("releaseDate", releaseDate.getTimeInMillis());
        values.put("posterPath", posterPath);
        values.put("coverPath", coverPath);
//            put("genres", genres)
        values.put("voteAverage", voteAverage);
        values.put("starred", starred);
        values.put("rating", rating);

        return values;
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

    public List<Genre> getGenres() {
        return genres;
    }

    public Integer getVoteAverage() {
        return voteAverage;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
