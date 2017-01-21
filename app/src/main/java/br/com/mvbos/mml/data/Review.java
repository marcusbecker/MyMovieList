package br.com.mvbos.mml.data;

/**
 * Created by Marcus Becker on 19/01/2017.
 */
public class Review {
    public static final int MAX_LENGHT = 150;
    private String reviewId;
    private String author;
    private String content;
    private String url;

    public Review(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        return reviewId.equals(review.reviewId);

    }

    @Override
    public int hashCode() {
        return reviewId.hashCode();
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId='" + reviewId + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getResumedContent() {
        if (getContent() != null && getContent().length() > MAX_LENGHT) {
            return getContent().substring(0, MAX_LENGHT).concat("...");
        } else {
            return getContent();
        }
    }
}
