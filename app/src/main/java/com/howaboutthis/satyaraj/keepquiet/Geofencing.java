package com.howaboutthis.satyaraj.keepquiet;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;
import java.util.List;

public class Geofencing implements ResultCallback {

    private List<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private GoogleApiClient mGoogleApiClient;
    private GeofenceBroadcastReceiver mGeoRev = new GeofenceBroadcastReceiver();
    private Context mContext;
    private static final String TAG = Geofencing.class.getSimpleName();
    private static final float GEOFENCE_RADIUS = 50;
    private static final long GEOFENCE_TIMEOUT = 24 * 60 * 60 * 1000;

    Geofencing(Context context, GoogleApiClient client){
        mContext = context;
        mGoogleApiClient = client;
        mGeofencePendingIntent = null;
        mGeofenceList = new ArrayList<>();
    }

    void registerAllGeofences(){
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()||
                mGeofenceList == null || mGeofenceList.size() == 0)
            return;
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);


        }catch (SecurityException securityException){
          Log.i(TAG,securityException.getMessage());
        }


    }

    void unRegisterAllGeofences(){
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
           return;
        try{
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    getGeofencePendingIntent()
            ).setResultCallback(this);

            mGeoRev.setRingerMode(mContext, AudioManager.RINGER_MODE_NORMAL);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancelAll();
            }
        }catch (SecurityException securityException){
            Log.e(TAG, securityException.getMessage());
        }

    }
    void updateGeofencesList(PlaceBuffer places){
        mGeofenceList = new ArrayList<>();
        if (places == null || places.getCount()==0) return;
        for (Place place: places){
            String placeUID = place.getId();
            double placeLat = place.getLatLng().latitude;
            double placeLng = place.getLatLng().longitude;

            Geofence geofence = new Geofence.Builder()
                    .setRequestId(placeUID)
                    .setExpirationDuration(GEOFENCE_TIMEOUT)
                    .setCircularRegion(placeLat,placeLng,GEOFENCE_RADIUS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER| Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            mGeofenceList.add(geofence);
        }
    }

    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(){
        if (mGeofencePendingIntent != null){
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(mContext, GeofenceBroadcastReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    @Override
    public void onResult(@NonNull Result result) {
        Log.e(TAG, String.format("Error adding/removing geofence: %s", result.getStatus().toString()));
    }
}
