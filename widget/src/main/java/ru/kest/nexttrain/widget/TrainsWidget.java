package ru.kest.nexttrain.widget;

import android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import ru.kest.nexttrain.widget.util.Constants;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import java.util.Arrays;

/**
 * Created by KKharitonov on 04.01.2016.
 */
public class TrainsWidget extends AppWidgetProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    final static public String LOG_TAG = "trainsLogs";

    private GoogleApiClient googleApiClient;
    private static Location lastLocation;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        SchedulerUtil.scheduleUpdateWidget(context, alarmManager);
        SchedulerUtil.scheduleUpdateLocation(context, alarmManager);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
        SchedulerUtil.sendUpdateLocation(context);
/*        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }*/
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
        if (googleApiClient != null && googleApiClient.isConnected()) {
            Log.d(LOG_TAG, "googleApiClient.disconnect()");
            googleApiClient.disconnect();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        SchedulerUtil.cancelScheduleUpdateWidget(context, alarmManager);
        SchedulerUtil.cancelScheduleUpdateLocation(context, alarmManager);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive: " + intent + " - " + this);
        if (intent.getAction().equalsIgnoreCase(Constants.UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateWidget(context, appWidgetManager, appWidgetID);
            }
        } else if (intent.getAction().equalsIgnoreCase(Constants.UPDATE_LOCATION)) {
            getLocationApi(context).connect();
            if (lastLocation != null) {
                new WeatherRequestTask(context).execute(lastLocation);
            }
        }
    }

    private void updateWidget(Context ctx, AppWidgetManager appWidgetManager, int widgetID) {
        String outputText = getTextMessage();

        Log.d(LOG_TAG, "Text: " + outputText);

        // Помещаем данные в текстовые поля
        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(), R.layout.widget);
        widgetView.setTextViewText(R.id.tv, outputText);

        // Обновляем виджет
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

    private String getTextMessage() {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        return sdf.format(new Date(System.currentTimeMillis()));

        if (lastLocation == null) {
            return "Location not found";
        } else {
            return lastLocation.getLongitude() + ", " + lastLocation.getLatitude();
        }
    }


    private GoogleApiClient getLocationApi(Context ctx) {
        if (googleApiClient == null) {
            Log.d(LOG_TAG, "getLocationApi: ");
            googleApiClient = new GoogleApiClient.Builder(ctx)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return googleApiClient;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG_TAG, "onConnected: " + googleApiClient);
        updateLastLocation();
        Log.d(LOG_TAG, "lastLocation: " + lastLocation);
        if (googleApiClient != null) {
            Log.d(LOG_TAG, "googleApiClient.disconnect()");
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    private void updateLastLocation() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if( location != null ){
            lastLocation = location;
        } else {
            Log.d(LOG_TAG, "lastLocation has not changed");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed: " + connectionResult);
    }


}
