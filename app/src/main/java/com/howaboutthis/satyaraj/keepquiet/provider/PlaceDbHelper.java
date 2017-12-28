package com.howaboutthis.satyaraj.keepquiet.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.howaboutthis.satyaraj.keepquiet.provider.PlaceContract.PlaceEntry;

public class PlaceDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "keepquiet.db";


    private static final int DATABASE_VERSION = 1;


    public PlaceDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override

    public void onCreate(SQLiteDatabase sqLiteDatabase) {



        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + PlaceEntry.TABLE_NAME + " (" +

                PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                PlaceEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +

                "UNIQUE (" + PlaceEntry.COLUMN_PLACE_ID + ") ON CONFLICT REPLACE" +

                "); ";


        sqLiteDatabase.execSQL(SQL_CREATE_PLACES_TABLE);

    }


    @Override

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlaceEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

}