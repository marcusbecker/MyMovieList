package br.com.mvbos.mml.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.mvbos.mml.data.Movie;
import br.com.mvbos.mml.data.Trailer;

/**
 * Parse JSON resposes to Java Objects
 * Created by Marcus Becker on 04/01/2017.
 */
public class JSONUtils {
    public static Movie[] toMovies(String response) {
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

    /*
    "id": 328111,
        "results": [{
            "id": "571cdc48c3a3684e620018b8",
            "iso_639_1": "en",
            "iso_3166_1": "US",
            "key": "i-80SGWfEjM",
            "name": "Official Teaser Trailer",
            "site": "YouTube",
            "size": 1080,
            "type": "Trailer"
    *
    * */
    public static Movie toMovieTrailer(String response) {
        Movie movie = null;

        try {
            JSONObject object = new JSONObject(response);
            long id = object.getLong("id");
            movie = new Movie(id);

            JSONArray arr = object.getJSONArray("results");

            if (arr != null) {
                Trailer[] trailers = new Trailer[arr.length()];
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject json = arr.getJSONObject(i);

                    String trailerId = json.getString("id");
                    String key = json.getString("key");
                    String name = json.getString("name");
                    String site = json.getString("site");

                    trailers[i] = new Trailer(trailerId);
                    trailers[i].setKey(key);
                    trailers[i].setName(name);
                    trailers[i].setSite(site);
                }

                movie.setTrailers(trailers);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }
}
