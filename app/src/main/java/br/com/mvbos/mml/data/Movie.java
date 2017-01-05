package br.com.mvbos.mml.data;

import java.io.Serializable;

/**
 * Created by Marcus Becker on 04/01/2017.
 */
public class Movie implements Serializable {
    private long id;
    private String title;
    private String imagePath;

    private float rating;
    private String overview;
    private String releaseDate;

    public Movie(long id) {
        this.id = id;
    }

    public Movie(long id, String title, String imagePath, float rating, String overview, String releaseDate) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.rating = rating;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return movie poster image thumbnail
     */

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return original title
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return user rating(vote average)
     */
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * @return A plot synopsis
     */
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * @return release date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return id == movie.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", rating=" + rating +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
