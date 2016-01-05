package ru.kest.nexttrain.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by KKharitonov on 04.01.2016.
 */
public class TrainsWidget extends AppWidgetProvider {
    final static private String LOG_TAG = "trainsLogs";

    private final String UPDATE_ALL_WIDGETS = "update_all_widgets";
    private final String UPDATE_LOCATION = "update_location";

    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private Context context;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
        this.context = context;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        scheduleUpdateLocation(context, alarmManager);
        scheduleUpdateWidget(context, alarmManager);
    }

    private void scheduleUpdateWidget(Context context, AlarmManager alarmManager) {
        Intent intent = new Intent(context, TrainsWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+3000, 60000, pIntent);
    }

    private void scheduleUpdateLocation(Context context, AlarmManager alarmManager) {
        Intent intent = new Intent(context, TrainsWidget.class);
        intent.setAction(UPDATE_LOCATION);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pIntent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
        Intent intent = new Intent(context, TrainsWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive: " + intent);
        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateWidget(context, appWidgetManager, appWidgetID);
            }
        } else if (intent.getAction().equalsIgnoreCase(UPDATE_LOCATION)) {

            getLocationApi(context).connect();
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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
/*
        if (lastLocation == null) {
            return "Location not found";
        } else {
            return lastLocation.getLongitude() + ", " + lastLocation.getLatitude();
        }
*/
    }


    private GoogleApiClient getLocationApi(Context ctx) {
        if (googleApiClient == null) {
            Log.d(LOG_TAG, "getLocationApi: ");
            googleApiClient = new GoogleApiClient.Builder(ctx)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.i(LOG_TAG, "ConnectionCallbacks.onConnected(): ");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.i(LOG_TAG, "ConnectionCallbacks.onConnectionSuspended(): ");
                        }

                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.e(LOG_TAG, "ConnectionCallbacks.OnConnectionFailed(): ");
                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        }
        return googleApiClient;
    }
/*
//    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG_TAG, "onConnected: " + bundle);
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.d(LOG_TAG, "lastLocation: " + lastLocation);
        if (googleApiClient != null) {
            Log.d(LOG_TAG, "googleApiClient.disconnect()");
            googleApiClient.disconnect();
        }
    }

//    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended: " + i);
    }

//    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed: " + connectionResult);
    }*/


}
