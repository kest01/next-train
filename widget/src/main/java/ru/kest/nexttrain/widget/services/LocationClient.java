package ru.kest.nexttrain.widget.services;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;

/**
 * Created by KKharitonov on 06.01.2016.
 */
public class LocationClient  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;

    private static Location lastLocation;

    public LocationClient(Context context) {
        Log.d(LOG_TAG, "getLocationApi: ");
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void connect() {
        googleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG_TAG, "onConnected: " + googleApiClient);
        updateLastLocation();
        Log.d(LOG_TAG, "googleApiClient.disconnect()");
        googleApiClient.disconnect();
        googleApiClient = null;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed: " + connectionResult);
    }

    public static Location getLastLocation() {
        return lastLocation;
    }

    private void updateLastLocation() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if( location != null ){
            lastLocation = location;
        } else {
            Log.d(LOG_TAG, "lastLocation has not changed");
        }
    }
}
