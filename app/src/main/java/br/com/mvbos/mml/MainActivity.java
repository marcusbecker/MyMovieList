package br.com.mvbos.mml;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import br.com.mvbos.mml.data.Movie;
import br.com.mvbos.mml.util.JSONUtils;
import br.com.mvbos.mml.util.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ClickListItemListener {

    private static final String RATED = "top_rated";
    private static final String POPULAR = "popular";

    private TextView errorMessage;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        errorMessage = (TextView) findViewById(R.id.tv_error_message);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);

        movieAdapter.setClickListItemListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new MovieTask().execute(POPULAR);

    }

    public class MovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            Movie[] movies = null;

            String param = strings[0];
            String token = getResources().getString(R.string.token);

            String order;
            if (POPULAR.equals(param)) {
                order = getResources().getString(R.string.popular_url);
            } else {
                order = getResources().getString(R.string.top_rated_url);
            }

            URL url = NetworkUtils.buildUrl(order, token, "");

            try {
                String response = NetworkUtils.getHttpResponse(url);
                movies = JSONUtils.toMovie(response);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return movies;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            progressBar.setVisibility(View.INVISIBLE);

            if (movies != null) {
                String imageURL = getResources().getString(R.string.image_url);

                movieAdapter.setImagesPath(imageURL, movies);

            } else {
                showErrorMessage();
            }

        }
    }

    private void clearErrorMessage() {
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClickListItem(int clickedItemIndex) {
        Intent i = new Intent(MainActivity.this, MovieDetailActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, "0");
        startActivity(i);
    }
}
