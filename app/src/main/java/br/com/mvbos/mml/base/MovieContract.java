package br.com.mvbos.mml.base;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Marcus Becker on 21/01/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "br.com.mvbos.mml";
    public static final Uri BASE_CONTENTURI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";


    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                    MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " INTEGER UNIQUE," +
                    MovieContract.MovieEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_IMAGE_PATH + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_RATING + REAL_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_RELEASE + TEXT_TYPE + " )";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;

    private MovieContract() {
    }

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENTURI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movieentry";
        public static final String COLUMN_NAME_MOVIE_ID = "movieId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMAGE_PATH = "imagePath";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE = "releaseDate";
    }

}
