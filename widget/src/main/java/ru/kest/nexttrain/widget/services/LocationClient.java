package ru.kest.nexttrain.widget.services;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;

/**
 * Created by KKharitonov on 06.01.2016.
 */
public class LocationClient  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final Location homeLocation = new Location("");
    private static final Location workLocation = new Location("");
    {
        homeLocation.setLatitude(55.8300989);
        homeLocation.setLongitude(37.2187062);

        workLocation.setLatitude(55.802753);
        workLocation.setLongitude(37.491259);
    }

    private GoogleApiClient googleApiClient;

    public LocationClient(Context context) {
        Log.d(LOG_TAG, "getLocationApi: ");
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private static int getDistanceToWork() {
        if (DataStorage.isSetLastLocation()) {
            return Math.round(workLocation.distanceTo(DataStorage.getLastLocation()));
        }
        return 0;
    }

    private static int getDistanceToHome() {
        if (DataStorage.isSetLastLocation()) {
            return Math.round(homeLocation.distanceTo(DataStorage.getLastLocation()));
        }
        return 0;
    }

    public static NearestStation getNearestStation() {
        if (DataStorage.isSetLastLocation()) {
            if (getDistanceToHome() < getDistanceToWork()) {
                return NearestStation.HOME;
            } else if (getDistanceToWork() < getDistanceToHome()) {
                return NearestStation.WORK;
            }
        }
        return null;
    }

    public void connect() {
        googleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG_TAG, "onConnected: " + googleApiClient);
        if (updateLastLocation()) {
            SchedulerUtil.sendUpdateWidget(googleApiClient.getContext());
        }
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

    private boolean updateLastLocation() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location != null){
            if (DataStorage.isSetLastLocation()) {
                if (workLocation.distanceTo(DataStorage.getLastLocation()) > 500) { // difference more then 500 meters
                    DataStorage.setLastLocation(location);
                    return true;
                }
            } else {
                DataStorage.setLastLocation(location);
                return true;
            }
        }
        Log.d(LOG_TAG, "lastLocation has not changed");
        return false;
    }

    public enum NearestStation {
        HOME,
        WORK;
    }
}
