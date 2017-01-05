package br.com.mvbos.mml;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
    private Movie[] moviesArray;

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

        new MovieTask().execute(POPULAR);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_popular) {
            updateTitle(POPULAR);
            new MovieTask().execute(POPULAR);
            return true;

        } else if (item.getItemId() == R.id.action_top_rated) {
            updateTitle(RATED);
            new MovieTask().execute(RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTitle(String optionSel) {
        String title = getString(R.string.app_name) + ": ";
        if (POPULAR.equals(optionSel)) {
            title += getString(R.string.popular_movies);
        } else {
            title += getString(R.string.top_rated_movies);
        }

        setTitle(title);
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
            progressBar.setVisibility(View.INVISIBLE);

            moviesArray = movies;

            if (moviesArray != null) {
                String imageURL = getResources().getString(R.string.image_url);

                movieAdapter.setImagesPath(imageURL, moviesArray);

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
        if (moviesArray == null) {
            return;
        }

        Intent i = new Intent(MainActivity.this, MovieDetailActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, moviesArray[clickedItemIndex]);
        startActivity(i);
    }
}
