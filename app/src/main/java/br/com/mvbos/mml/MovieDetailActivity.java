package br.com.mvbos.mml;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.mvbos.mml.data.Movie;

public class MovieDetailActivity extends AppCompatActivity {

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
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            movieSelected = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);

            title.setText(movieSelected.getTitle());
            release.setText(movieSelected.getReleaseDate());
            vote.setText(String.format("%.1f/10", movieSelected.getRating()));
            overview.setText(movieSelected.getOverview());

            String path = getString(R.string.image_url_thumb) + movieSelected.getImagePath();
            Picasso.with(MovieDetailActivity.this).load(path).into(poster);
        }
    }
}
