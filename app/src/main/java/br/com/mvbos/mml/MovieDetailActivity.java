package br.com.mvbos.mml;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import br.com.mvbos.mml.data.Movie;
import br.com.mvbos.mml.util.JSONUtils;
import br.com.mvbos.mml.util.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    private static final int TRAILER_LOADER_ID = 32;

    private Movie movieSelected;
    private TextView title;
    private ImageView poster;
    private TextView release;
    private TextView vote;
    private TextView overview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        title = (TextView) findViewById(R.id.tvTitle);
        release = (TextView) findViewById(R.id.tvRelease);
        vote = (TextView) findViewById(R.id.tvVote);
        overview = (TextView) findViewById(R.id.tvOverview);

        poster = (ImageView) findViewById(R.id.ivPoster);

        Intent intent = getIntent();
        if (intent.hasExtra(Movie.PARCELABLE_KEY)) {
            movieSelected = intent.getParcelableExtra(Movie.PARCELABLE_KEY);

            setTitle(String.format(getString(R.string.movie_title), getString(R.string.app_name), movieSelected.getTitle()));

            title.setText(movieSelected.getTitle());
            release.setText(movieSelected.getReleaseDate());
            vote.setText(String.format(Locale.ENGLISH, "%.1f/10", movieSelected.getRating()));
            overview.setText(movieSelected.getOverview());

            String path = getString(R.string.image_url_thumb) + movieSelected.getImagePath();
            Picasso.with(MovieDetailActivity.this).load(path).into(poster);


            final LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(TRAILER_LOADER_ID, null, this);

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

    @Override
    public Loader<Movie> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            private Movie cache;
            private long id;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null || !args.containsKey(Movie.ID_KEY)) {
                    return;
                }

                id = args.getLong(Movie.ID_KEY, 0);

                //TODO show progress view

                if (cache != null && cache.getId() == id) {
                    deliverResult(cache);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Movie loadInBackground() {

                String token = getString(R.string.token);
                String strUrl = String.format(getString(R.string.trailer_url), id);

                URL url = NetworkUtils.buildUrl(strUrl, token);

                try {
                    String response = NetworkUtils.getHttpResponse(url);
                    JSONUtils.populeMovieTrailer(movieSelected, response);


                    strUrl = String.format(getString(R.string.reviews_url), id);
                    url = NetworkUtils.buildUrl(strUrl, token);

                    response = NetworkUtils.getHttpResponse(url);
                    JSONUtils.populeMovieReviews(movieSelected, response);

                    return movieSelected;

                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            public void deliverResult(Movie data) {
                cache = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        //TODO hidden progress view
        if (null == data) {
            //showErrorMessage();
        } else {
            //mSearchResultsTextView.setText(data);
            //showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
    }

}
