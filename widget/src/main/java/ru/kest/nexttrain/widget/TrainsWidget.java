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
import ru.kest.nexttrain.widget.services.*;
import ru.kest.nexttrain.widget.services.data.DataProvider;
import ru.kest.nexttrain.widget.services.data.DataService;
import ru.kest.nexttrain.widget.util.NotificationUtil;
import ru.kest.nexttrain.widget.util.SchedulerUtil;
import ru.kest.nexttrain.widget.ui.WidgetUtil;

import java.util.Arrays;

import static ru.kest.nexttrain.widget.Constants.*;

/**
 * Created by KKharitonov on 04.01.2016.
 */
public class TrainsWidget extends AppWidgetProvider {


//    private GoogleApiClient googleApiClient;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
        AlarmManager alarmManager = SchedulerUtil.getAlarmManager(context);

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

        WidgetUtil.updateWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));

        AlarmManager alarmManager = SchedulerUtil.getAlarmManager(context);
        SchedulerUtil.cancelScheduleUpdateWidget(context, alarmManager);
        SchedulerUtil.cancelScheduleUpdateLocation(context, alarmManager);
        SchedulerUtil.cancelScheduleTrainScheduleRequest(context, alarmManager);
        SchedulerUtil.cancelScheduleUpdateNotification(context, alarmManager);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        DataProvider dataProvider = DataService.getDataProvider(context);

        Log.d(LOG_TAG, "onReceive: " + intent + " - " + this);
        switch (intent.getAction()) {
            case UPDATE_ALL_WIDGETS:
                ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
                WidgetUtil.updateWidgets(context, appWidgetManager, ids);
                break;
            case UPDATE_LOCATION:
                new LocationClient(context).connect();
                break;
            case TRAIN_SCHEDULE_REQUEST:
                if (TrainSheduleRequestTask.getExecuted().compareAndSet(false, true)) {
                    new TrainSheduleRequestTask(context).execute();
                }
                break;
            case CREATE_NOTIFICATION:
                if (dataProvider.isSetTrainThreads()) {
                    int threadHash = intent.getIntExtra(RECORD_HASH, 0);
                    TrainThread thread = dataProvider.getThreadByHash(threadHash);
                    if (thread != null) {
                        dataProvider.setNotificationTrain(thread);
                        NotificationUtil.createOrUpdateNotification(context);
                    }
                }
                break;
            case UPDATE_NOTIFICATION:
                NotificationUtil.createOrUpdateNotification(context);
                break;
            case DELETED_NOTIFICATION:
                Toast.makeText(context, "Notification has been deleted", Toast.LENGTH_LONG).show();
                dataProvider.setNotificationTrain(null);
                SchedulerUtil.cancelScheduleUpdateNotification(context, SchedulerUtil.getAlarmManager(context));
                break;
        }
    }


}
