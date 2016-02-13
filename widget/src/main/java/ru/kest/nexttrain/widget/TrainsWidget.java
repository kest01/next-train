package ru.kest.nexttrain.widget;

import android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.services.DataStorage;
import ru.kest.nexttrain.widget.services.LocationClient;
import ru.kest.nexttrain.widget.services.TrainSheduleRequestTask;
import ru.kest.nexttrain.widget.util.NotificationUtil;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import java.util.Arrays;

import static ru.kest.nexttrain.widget.util.Constants.*;

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

        SchedulerUtil.sendUpdateLocation(context);
        SchedulerUtil.sendTrainScheduleRequest(context, alarmManager);
        SchedulerUtil.scheduleUpdateWidget(context, alarmManager);
        SchedulerUtil.scheduleUpdateLocation(context, alarmManager);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        updateWidgets(context, appWidgetManager, appWidgetIds);
    }

    private void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            WidgetUpdater.updateWidget(context, appWidgetManager, id);
        }

        NotificationUtil.createOrUpdateNotification(context);
        if (!DataStorage.isSetTrainThreads()) {
            SchedulerUtil.sendTrainScheduleRequest(context, (AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
        }
        SchedulerUtil.scheduleUpdateWidget(context, (AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
        Toast.makeText(context, "updateWidget", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SchedulerUtil.cancelScheduleUpdateWidget(context, alarmManager);
        SchedulerUtil.cancelScheduleUpdateLocation(context, alarmManager);
        SchedulerUtil.cancelScheduleTrainScheduleRequest(context, alarmManager);
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
        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            updateWidgets(context, appWidgetManager, ids);
        } else if (intent.getAction().equalsIgnoreCase(UPDATE_LOCATION)) {
            new LocationClient(context).connect();
        } else if (intent.getAction().equalsIgnoreCase(TRAIN_SCHEDULE_REQUEST)) {
            new TrainSheduleRequestTask(context).execute();
        } else if (intent.getAction().equalsIgnoreCase(DELETED_NOTIFICATION)) {
            Toast.makeText(context, "Notification has been deleted", Toast.LENGTH_LONG).show();
            DataStorage.setNotificationTrain(null);

        } else if (intent.getAction().equalsIgnoreCase(CREATE_NOTIFICATION)) {
//            Toast.makeText(context, ":: " + intent.toString(), Toast.LENGTH_LONG).show();
            if (DataStorage.isSetTrainThreads()) {
                int threadHash = intent.getIntExtra(RECORD_HASH, 0);
                TrainThread thread = DataStorage.getThreadByHash(threadHash);
                if (thread != null) {
                    DataStorage.setNotificationTrain(thread);
                    NotificationUtil.createOrUpdateNotification(context);
                }
            }
        }
    }

}
