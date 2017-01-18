package br.com.mvbos.mml;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.mvbos.mml.data.Movie;
import br.com.mvbos.mml.service.MovieService;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ClickListItemListener, MovieService.AsyncTaskDelegate<Movie[]> {


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

        executeService(MovieService.POPULAR);
    }

    private void executeService(String order) {
        progressBar.setVisibility(View.VISIBLE);
        MovieService movieService = new MovieService(MainActivity.this, this);
        movieService.execute(order);
        updateTitle(order);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        progressBar.setVisibility(View.VISIBLE);

        if (item.getItemId() == R.id.action_popular) {
            executeService(MovieService.POPULAR);
            return true;

        } else if (item.getItemId() == R.id.action_top_rated) {
            executeService(MovieService.RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTitle(String optionSel) {
        String title;
        if (MovieService.POPULAR.equals(optionSel)) {
            title = getString(R.string.popular_movies);
        } else {
            title = getString(R.string.top_rated_movies);
        }

        setTitle(String.format(getString(R.string.movie_title), getString(R.string.app_name), title));
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
        i.putExtra(Movie.PARCELABLE_KEY, moviesArray[clickedItemIndex]);
        startActivity(i);
    }

    @Override
    public void processFinish(Movie[] output) {
        progressBar.setVisibility(View.INVISIBLE);

        moviesArray = output;

        if (moviesArray != null) {
            String imageURL = getResources().getString(R.string.image_url);
            movieAdapter.setImagesPath(imageURL, moviesArray);
            clearErrorMessage();

        } else {
            showErrorMessage();
        }
    }
}
