package com.maxpilotto.movieshowcase.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.maxpilotto.movieshowcase.persistance.Database;
import com.maxpilotto.movieshowcase.protocols.Storable;

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
    private Boolean favourite;
    private Integer rating;

    public Movie(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.title = cursor.getString(cursor.getColumnIndex("title"));
        this.overview = cursor.getString(cursor.getColumnIndex("overview"));
        this.releaseDate = calendarOf(cursor.getLong(cursor.getColumnIndex("releaseDate")));
        this.posterPath = cursor.getString(cursor.getColumnIndex("posterPath"));
        this.coverPath = cursor.getString(cursor.getColumnIndex("coverPath"));
        this.voteAverage = cursor.getInt(cursor.getColumnIndex("voteAverage"));
        this.genres = Database.get().getMovieGenres(id);
        this.favourite = cursor.getInt(cursor.getColumnIndex("favourite")) > 0;
        this.rating = cursor.getInt(cursor.getColumnIndex("rating"));
    }

    public Movie(Integer id, String title, String overview, Calendar releaseDate, String posterPath, String coverPath, List<Genre> genres, Integer voteAverage) {
        this(id, title, overview, releaseDate, posterPath, coverPath, genres, voteAverage, false, 0);
    }

    public Movie(Integer id, String title, String overview, Calendar releaseDate, String posterPath, String coverPath, List<Genre> genres, Integer voteAverage, Boolean favourite, Integer rating) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.coverPath = coverPath;
        this.genres = genres;
        this.voteAverage = voteAverage;
        this.favourite = favourite;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate=" + releaseDate +
                ", posterPath='" + posterPath + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", genres=" + genres +
                ", voteAverage=" + voteAverage +
                ", favourite=" + favourite +
                ", rating=" + rating +
                '}';
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

        return values;
    }

    /**
     * Returns a ContentValues instance with all the values from this model
     *
     * The {@link Movie#values()} method will return only the values sent by the web service
     */
    public ContentValues allValues() {
        ContentValues values = values();

        values.put("favourite", favourite);
        values.put("rating", rating);

        return values;
    }

    public Integer getYear() {
        return releaseDate.get(Calendar.YEAR);
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

    public List<Genre> getGenres() {
        return genres;
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
