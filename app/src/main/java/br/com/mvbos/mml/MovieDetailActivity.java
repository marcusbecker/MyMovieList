package br.com.mvbos.mml;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import br.com.mvbos.mml.adapter.ReviewAdapter;
import br.com.mvbos.mml.adapter.TrailerAdapter;
import br.com.mvbos.mml.base.MovieContract;
import br.com.mvbos.mml.base.MovieDbHelper;
import br.com.mvbos.mml.data.Movie;
import br.com.mvbos.mml.data.Review;
import br.com.mvbos.mml.data.Trailer;
import br.com.mvbos.mml.service.MovieDetailService;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    private static final int TRAILER_LOADER_ID = 32;

    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private boolean inBookMark;
    private Movie movieSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView title;
        TextView release;
        TextView vote;
        TextView overview;

        ImageView poster;


        title = (TextView) findViewById(R.id.tvTitle);
        release = (TextView) findViewById(R.id.tvRelease);
        vote = (TextView) findViewById(R.id.tvVote);
        overview = (TextView) findViewById(R.id.tvOverview);

        poster = (ImageView) findViewById(R.id.ivPoster);

        Intent intent = getIntent();
        if (intent.hasExtra(Movie.PARCELABLE_KEY)) {

            configureAdapter();

            movieSelected = intent.getParcelableExtra(Movie.PARCELABLE_KEY);

            MovieDbHelper dbHelper = new MovieDbHelper(this);
            inBookMark = dbHelper.isInBookMark(movieSelected);
            updateBookMarkButton();

            setTitle(String.format(getString(R.string.movie_title), getString(R.string.app_name), movieSelected.getTitle()));

            title.setText(movieSelected.getTitle());
            release.setText(movieSelected.getReleaseDate());
            vote.setText(String.format(Locale.ENGLISH, "%.1f / 10", movieSelected.getRating()));
            overview.setText(movieSelected.getOverview());

            String path = getString(R.string.image_url_thumb) + movieSelected.getImagePath();
            Picasso.with(MovieDetailActivity.this).load(path).into(poster);

            final LoaderManager loaderManager = getSupportLoaderManager();

            Bundle myBundle = new Bundle();
            myBundle.putLong(Movie.ID_KEY, movieSelected.getId());

            Loader<Movie> myLoader = loaderManager.getLoader(TRAILER_LOADER_ID);

            if (myLoader == null) {
                loaderManager.initLoader(TRAILER_LOADER_ID, myBundle, this);
            } else {
                loaderManager.restartLoader(TRAILER_LOADER_ID, myBundle, this);
            }
        }
    }

    private void configureAdapter() {
        trailerAdapter = new TrailerAdapter();
        trailerRecyclerView = (RecyclerView) findViewById(R.id.rvTrailer);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
        trailerRecyclerView.setAdapter(trailerAdapter);

        reviewAdapter = new ReviewAdapter();
        reviewRecyclerView = (RecyclerView) findViewById(R.id.rvReview);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
        reviewRecyclerView.setAdapter(reviewAdapter);

        trailerAdapter.setClickListItemListener(new TrailerAdapter.ClickListItemListener() {
            @Override
            public void onClickListItem(int clickedItemIndex) {
                Trailer trailer = movieSelected.getTrailers()[clickedItemIndex];
                String url = String.format(getString(R.string.you_tube), trailer.getKey());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        reviewAdapter.setClickListItemListener(new ReviewAdapter.ClickListItemListener() {
            @Override
            public void onClickListItem(int clickedItemIndex) {
                Review review = movieSelected.getReviews()[clickedItemIndex];
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, final Bundle args) {
        return new MovieDetailService(this, movieSelected, id, args);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        //TODO hidden progress view
        if (null == data) {
            //showErrorMessage();
        } else {
            trailerAdapter.setTrailers(data.getTrailers());
            reviewAdapter.setReviews(data.getReviews());
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
    }

    public void addBookMark(View view) {
        Uri uri;
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID, movieSelected.getId());

        if (inBookMark) {
            uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(String.valueOf(movieSelected.getId())).build();

            int res = getContentResolver().delete(uri, null, null);
            if (res == 0)
                uri = null;

        } else {
            values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movieSelected.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_IMAGE_PATH, movieSelected.getImagePath());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_RATING, movieSelected.getRating());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movieSelected.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE, movieSelected.getReleaseDate());

            uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        }

        if (uri != null) {
            String notification;
            if (inBookMark) {
                notification = getString(R.string.movie_removed);
            } else {
                notification = getString(R.string.movie_added);
            }

            inBookMark = !inBookMark;
            updateBookMarkButton();
            Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBookMarkButton() {
        ImageView bookMarkButton = (ImageView) findViewById(R.id.ivBookMark);
        if (inBookMark) {
            bookMarkButton.setImageResource(R.drawable.heart_full);
        } else {
            bookMarkButton.setImageResource(R.drawable.heart_half);
        }
    }
}
