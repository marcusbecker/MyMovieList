package br.com.mvbos.mml.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.mvbos.mml.data.Movie;

/**
 * Created by Marcus Becker on 21/01/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MovieContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MovieContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_IMAGE_PATH, movie.getImagePath());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_RATING, movie.getRating());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE, movie.getReleaseDate());

        try {
            return db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            db.close();
        }

        return -1;
    }

    public boolean isInBookMark(Movie movie) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID};

        String selection = MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(movie.getId())};

        String sortOrder = null;

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                "1"
        );

        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    public Movie[] selectMovies() {

        String[] projection = {
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_NAME_TITLE,
                MovieContract.MovieEntry.COLUMN_NAME_IMAGE_PATH,
                MovieContract.MovieEntry.COLUMN_NAME_RATING,
                MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_NAME_RELEASE
        };

        SQLiteDatabase db = getReadableDatabase();

        String selection = null; //MovieContract.MovieEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {};

        String sortOrder = null; //MovieContract.MovieEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        //final boolean b = cursor.moveToFirst();
        short index = 0;
        Movie[] result = new Movie[cursor.getCount()];

        while (cursor.moveToNext()) {
            //long id = cursor.getLong(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry._ID));
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_TITLE));
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_IMAGE_PATH));
            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_RATING));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW));
            String release = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_RELEASE));

            Movie movie = new Movie(itemId);
            movie.setTitle(title);
            movie.setImagePath(imagePath);
            movie.setRating(rating);
            movie.setOverview(overview);
            movie.setReleaseDate(release);

            result[index++] = movie;
        }

        cursor.close();

        return result;
    }

    public int deleteMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(movie.getId())};

        try {
            return db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return -1;
    }
}
