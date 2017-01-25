package br.com.mvbos.mml.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.mvbos.mml.base.MovieContract;
import br.com.mvbos.mml.base.MovieDbHelper;

/**
 * Created by Marcus Becker on 23/01/2017.
 */

public class MovieContentProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper movieDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);

        Cursor cursor;

        if (match == MOVIES) {
            final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
            cursor = db.query(
                    MovieContract.MovieEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);

        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        final String VND_ANDROID_CURSOR_DIR = "vnd.android.cursor.dir";

        switch (match) {
            case MOVIES:
                // directory
                return VND_ANDROID_CURSOR_DIR + "/" + MovieContract.AUTHORITY + "/" + MovieContract.PATH_MOVIES;
            case MOVIE_WITH_ID:
                // single item type
                return VND_ANDROID_CURSOR_DIR + "/" + MovieContract.AUTHORITY + "/" + MovieContract.PATH_MOVIES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        if (match == MOVIES) {
            final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
            long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
            db.close();

            if (id > 0) {
                returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                System.out.println(returnUri);
            } else {
                throw new SQLException("Fail to insert " + uri);
            }

        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int result;
        int match = sUriMatcher.match(uri);

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        if (match == MOVIE_WITH_ID) {
            result = db.delete(MovieContract.MovieEntry.TABLE_NAME, "_id=?", new String[]{uri.getPathSegments().get(1)});

        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (result > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
