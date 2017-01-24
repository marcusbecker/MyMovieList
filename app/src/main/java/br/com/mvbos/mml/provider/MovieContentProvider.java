package br.com.mvbos.mml.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import br.com.mvbos.mml.base.MovieContract;
import br.com.mvbos.mml.base.MovieDbHelper;

/**
 * Created by Marcus Becker on 23/01/2017.
 */

public class MovieContentProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TASKS, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TASKS + "/#", MOVIES_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        if (match == MOVIES_WITH_ID) {
            MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());

            final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
            long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
            if (id > 0) {
                returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);

            } else {
                throw new SQLException("Fail to insert " + uri);
            }

        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
