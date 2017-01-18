package br.com.mvbos.mml.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie object
 * Created by Marcus Becker on 04/01/2017.
 */
public class Movie implements Parcelable {
    public static final String PARCELABLE_KEY = "parcel_movie";
    public static final java.lang.String ID_KEY = "id_movie";

    private long id;
    private String title;
    private String imagePath;

    private float rating;
    private String overview;
    private String releaseDate;

    private Trailer[] trailers;

    public Movie(long id) {
        this.id = id;
    }

    private Movie(Parcel parcel) {
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.imagePath = parcel.readString();
        this.rating = parcel.readFloat();
        this.overview = parcel.readString();
        this.releaseDate = parcel.readString();
    }

    public Movie(long id, String title, String imagePath, float rating, String overview, String releaseDate) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.rating = rating;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


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


    public Trailer[] getTrailers() {
        return trailers;
    }

    public void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(imagePath);
        parcel.writeFloat(rating);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}
