package br.com.mvbos.mml.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.mvbos.mml.data.Movie;

/**
 * Created by Marcus Becker on 04/01/2017.
 */
public class JSONUtils {
    public static Movie[] toMovie(String response) {
        Movie[] movies = null;
        try {
            JSONObject object = new JSONObject(response);
            JSONArray arr = object.getJSONArray("results");

            if (arr == null) {
                return null;
            }

            movies = new Movie[arr.length()];

            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonMovie = arr.getJSONObject(i);
                long id = jsonMovie.getLong("id");
                String title = jsonMovie.getString("title");
                String imagePath = jsonMovie.getString("poster_path");
                float rating = (float) jsonMovie.getDouble("vote_average");
                String overview = jsonMovie.getString("overview");
                String releaseDate = jsonMovie.getString("release_date");

                movies[i] = new Movie(id, title, imagePath, rating, overview, releaseDate);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
