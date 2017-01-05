package br.com.mvbos.mml.data;

/**
 * Created by Marcus Becker on 04/01/2017.
 */
public class Movie {
    private long id;
    private String title;
    private String imagePath;

    public Movie(long id) {
        this.id = id;
    }

    public Movie(long id, String title, String imagePath) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                ", imagePath='" + imagePath + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
