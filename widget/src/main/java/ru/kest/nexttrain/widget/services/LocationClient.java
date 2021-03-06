package ru.kest.nexttrain.widget.services;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import ru.kest.nexttrain.widget.services.data.DataProvider;
import ru.kest.nexttrain.widget.services.data.DataService;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import static ru.kest.nexttrain.widget.Constants.LOG_TAG;

/**
 * Created by KKharitonov on 06.01.2016.
 */
public class LocationClient  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient googleApiClient;

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
        if (updateLastLocation(DataService.getDataProvider(googleApiClient.getContext()))) {
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

    private boolean updateLastLocation(DataProvider dataProvider) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location != null){
            if (dataProvider.isSetLastLocation()) {
                if (location.distanceTo(dataProvider.getLastLocation()) > 500) { // difference more then 500 meters
                    dataProvider.setLastLocation(location);
                    return true;
                }
            } else {
                dataProvider.setLastLocation(location);
                return true;
            }
        }
        Log.d(LOG_TAG, "lastLocation has not changed");
        return false;
    }
}
