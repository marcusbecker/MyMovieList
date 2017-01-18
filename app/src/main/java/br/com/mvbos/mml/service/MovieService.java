package br.com.mvbos.mml.service;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;

import br.com.mvbos.mml.R;
import br.com.mvbos.mml.data.Movie;
import br.com.mvbos.mml.util.JSONUtils;
import br.com.mvbos.mml.util.NetworkUtils;

/**
 * Created by Marcus Becker on 09/01/2017.
 */

public class MovieService extends AsyncTask<String, Void, Movie[]> {

    public static final String RATED = "top_rated";
    public static final String POPULAR = "popular";

    public interface AsyncTaskDelegate<T> {
        void processFinish(T output);
    }

    private final Context context;
    private final AsyncTaskDelegate delegate;


    public MovieService(Context context, AsyncTaskDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Movie[] doInBackground(String... strings) {
        if (strings.length == 0) {
            return null;
        }

        Movie[] movies = null;

        String param = strings[0];

        String token = context.getString(R.string.token);

        String order;
        if (POPULAR.equals(param)) {
            order = context.getString(R.string.popular_url);
        } else {
            order = context.getString(R.string.top_rated_url);
        }

        URL url = NetworkUtils.buildUrl(order, token);

        try {
            String response = NetworkUtils.getHttpResponse(url);
            movies = JSONUtils.toMovies(response);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        if (delegate != null) {
            delegate.processFinish(movies);
        }
    }
}
