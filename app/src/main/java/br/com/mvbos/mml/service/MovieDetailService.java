package br.com.mvbos.mml.service;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.URL;

import br.com.mvbos.mml.R;
import br.com.mvbos.mml.data.Movie;
import br.com.mvbos.mml.util.JSONUtils;
import br.com.mvbos.mml.util.NetworkUtils;

/**
 * Created by Marcus Becker on 21/01/2017.
 */

public class MovieDetailService extends AsyncTaskLoader<Movie> {

    private Movie cache;
    private long movieId;
    private final Bundle args;
    private final Movie movieSelected;

    public MovieDetailService(Context context, Movie movieSelected, int id, Bundle args) {
        super(context);
        this.args = args;
        this.movieSelected = movieSelected;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (args == null || !args.containsKey(Movie.ID_KEY)) {
            return;
        }

        movieId = args.getLong(Movie.ID_KEY, 0);

        //TODO show progress view

        if (cache != null && cache.getId() == movieId) {
            deliverResult(cache);
        } else {
            forceLoad();
        }
    }

    @Override
    public Movie loadInBackground() {

        String token = getContext().getString(R.string.token);
        String strUrl = String.format(getContext().getString(R.string.trailer_url), movieId);

        URL url = NetworkUtils.buildUrl(strUrl, token);

        try {
            String response = NetworkUtils.getHttpResponse(url);
            JSONUtils.populeMovieTrailer(movieSelected, response);

            strUrl = String.format(getContext().getString(R.string.reviews_url), movieId);
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
}
