package ru.kest.nexttrain.widget;

import android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import ru.kest.nexttrain.widget.services.LocationClient;
import ru.kest.nexttrain.widget.services.WeatherRequestTask;
import ru.kest.nexttrain.widget.util.Constants;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import java.util.Arrays;

/**
 * Created by KKharitonov on 04.01.2016.
 */
public class TrainsWidget extends AppWidgetProvider {
    final static public String LOG_TAG = "nextTrainsLogs";

//    private GoogleApiClient googleApiClient;

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
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
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
            new LocationClient(context).connect();
            if (LocationClient.getLastLocation() != null) {
                new WeatherRequestTask(context).execute(LocationClient.getLastLocation());
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

        Location lastLocation = LocationClient.getLastLocation();
        if (lastLocation == null) {
            return "Location not found";
        } else {
            return lastLocation.getLongitude() + ", " + lastLocation.getLatitude();
        }
    }


}
