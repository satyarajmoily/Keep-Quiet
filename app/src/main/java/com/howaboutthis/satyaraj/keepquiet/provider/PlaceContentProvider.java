package com.howaboutthis.satyaraj.keepquiet.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.howaboutthis.satyaraj.keepquiet.provider.PlaceContract.PlaceEntry;


public class PlaceContentProvider extends ContentProvider {

    public static final int PLACES = 100;

    public static final int PLACE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private PlaceDbHelper mPlaceDbHelper;

     static {


        sUriMatcher.addURI(PlaceContract.AUTHORITY, PlaceContract.PATH_PLACES, PLACES);

        sUriMatcher.addURI(PlaceContract.AUTHORITY, PlaceContract.PATH_PLACES + "/#", PLACE_WITH_ID);


    }

    @Override

    public boolean onCreate() {

        Context context = getContext();

        mPlaceDbHelper = new PlaceDbHelper(context);

        return true;

    }


    @Override

    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase db = mPlaceDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri; // URI to be returned

        switch (match) {

            case PLACES:

                long id = db.insert(PlaceEntry.TABLE_NAME, null, values);

                if (id > 0) {

                    returnUri = ContentUris.withAppendedId(PlaceContract.PlaceEntry.CONTENT_URI, id);

                } else {

                    throw new android.database.SQLException("Failed to insert row into " + uri);

                }

                break;


            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }


        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);


        return returnUri;

    }

    @Override

    public Cursor query(@NonNull Uri uri, String[] projection, String selection,

                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mPlaceDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;


        switch (match) {

            case PLACES:

                retCursor = db.query(PlaceEntry.TABLE_NAME,

                        projection,

                        selection,

                        selectionArgs,

                        null,

                        null,

                        sortOrder);

                break;

            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }


        //noinspection ConstantConditions
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;

    }

    @Override

    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {


        final SQLiteDatabase db = mPlaceDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int placesDeleted; // starts as 0

        switch (match) {

            case PLACES:

                placesDeleted = db.delete(PlaceEntry.TABLE_NAME, selection, selectionArgs);
                Log.d(String.valueOf(this), placesDeleted + " Deleted");
                break;

            case PLACE_WITH_ID:

                String id = uri.getPathSegments().get(1);

                placesDeleted = db.delete(PlaceEntry.TABLE_NAME, "_id=?", new String[]{id});

                break;

            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (placesDeleted != 0) {

            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);

        }

        return placesDeleted;

    }

    @Override

    public int update(@NonNull Uri uri, ContentValues values, String selection,

                      String[] selectionArgs) {


        final SQLiteDatabase db = mPlaceDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int placesUpdated;


        switch (match) {

            case PLACE_WITH_ID:

                String id = uri.getPathSegments().get(1);

                placesUpdated = db.update(PlaceEntry.TABLE_NAME, values, "_id=?", new String[]{id});

                break;

            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (placesUpdated != 0) {


            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);

        }

        return placesUpdated;

    }


    @Override

    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");

    }

}