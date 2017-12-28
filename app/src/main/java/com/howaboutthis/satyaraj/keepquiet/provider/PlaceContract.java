package com.howaboutthis.satyaraj.keepquiet.provider;

import android.content.ContentResolver;
import android.net.Uri;

import android.provider.BaseColumns;

public class PlaceContract {


 public    static final String AUTHORITY = "com.howaboutthis.satyaraj.keepquiet";


   public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String PATH_PLACES = "places";

  public   static final class PlaceEntry implements BaseColumns {

   public      static final Uri CONTENT_URI =

           Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLACES);


      public static final String CONTENT_LIST_TYPE =


              ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PLACES;


      public static final String CONTENT_ITEM_TYPE =


              ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PLACES;


      public final static String _ID = BaseColumns._ID;

      public static final String TABLE_NAME = "places";

       public static final String COLUMN_PLACE_ID = "placeID";

    }

}