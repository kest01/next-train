package ru.kest.nexttrain.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import ru.kest.nexttrain.PopUpActivity;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.services.DataStorage;
import ru.kest.nexttrain.widget.services.LocationClient;
import ru.kest.nexttrain.widget.services.TrainSheduleRequestTask;
import ru.kest.nexttrain.widget.util.NotificationUtil;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

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
        SchedulerUtil.scheduleUpdateWidget(context, alarmManager);
        SchedulerUtil.scheduleUpdateLocation(context, alarmManager);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
//        SchedulerUtil.sendUpdateLocation(context);
        updateWidgets(context, appWidgetManager, appWidgetIds);
    }

    private void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }

        NotificationUtil.createOrUpdateNotification(context);
        if (!DataStorage.isSetTrainThreads()) {
            SchedulerUtil.sendUpdateLocation(context);
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
            if (DataStorage.isSetLastLocation()) {
                new TrainSheduleRequestTask(context).execute();
            }
        } else if (intent.getAction().equalsIgnoreCase(DELETED_NOTIFICATION)) {
            Toast.makeText(context, "Notification has been deleted", Toast.LENGTH_LONG).show();
            DataStorage.setNotificationTrain(null);

        } else if (intent.getAction().equalsIgnoreCase(CREATE_NOTIFICATION)) {
//            Toast.makeText(context, ":: " + intent.toString(), Toast.LENGTH_LONG).show();
            if (DataStorage.isSetTrainThreads()) {
                boolean homeToWork = intent.getBooleanExtra(HOME_TO_WORK, true);
                int recordId = intent.getIntExtra(RECORD_ID, 0);
                List<TrainThread> trainThreads = homeToWork ? DataStorage.getTrainsFromHomeToWork() : DataStorage.getTrainsFromWorkToHome();
                TrainThread thread = trainThreads.get(recordId);
                DataStorage.setNotificationTrain(thread);

                NotificationUtil.createOrUpdateNotification(context);
            }
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {

        List<TrainThread> trainThreads = null;
        Integer indexOfNextTrains = null;
        boolean homeToWork = true;

        if (DataStorage.isSetTrainThreads() && DataStorage.isSetLastLocation()) {
            if (LocationClient.getNearestStation() == LocationClient.NearestStation.HOME) {
                trainThreads = DataStorage.getTrainsFromHomeToWork();
                homeToWork = true;
            } else {
                trainThreads = DataStorage.getTrainsFromWorkToHome();
                homeToWork = false;
            }
            indexOfNextTrains = indexOfNextTrains(trainThreads);
        }
        Log.d(LOG_TAG, "updateWidget: " + indexOfNextTrains + " : " + trainThreads);

        // Помещаем данные в текстовые поля
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
        Resources res = context.getResources();

        for (int i = 1; i <= 2; i++) {
            int fromId = res.getIdentifier("from" + i, "id", context.getPackageName());
            int toId = res.getIdentifier("to" + i, "id", context.getPackageName());
            int departId = res.getIdentifier("depart" + i, "id", context.getPackageName());
            int layoutId = res.getIdentifier("ll" + i, "id", context.getPackageName());

            // Clear all field
            widgetView.setTextViewText(fromId, "");
            widgetView.setTextViewText(toId, "");
            widgetView.setTextViewText(departId, "");

            if (indexOfNextTrains != null && (indexOfNextTrains + i) <= trainThreads.size()) {
                int recordIndex = indexOfNextTrains + i - 1;
                TrainThread thread = trainThreads.get(recordIndex);
//                Log.d(LOG_TAG, "TrainThread: " + thread);
                widgetView.setTextViewText(fromId, thread.getFromName());
                widgetView.setTextViewText(toId, thread.getToName());

                DateFormat dateFormatter = new SimpleDateFormat("HH:mm");
                String departTime = dateFormatter.format(thread.getDeparture());
                String arrivalTime = dateFormatter.format(thread.getArrival());

                widgetView.setTextViewText(departId, departTime + " - " + arrivalTime);

//                Intent onClickIntent = new Intent(context, TrainsWidget.class);
                Intent popUpIntent = new Intent(context, PopUpActivity.class);
                popUpIntent.setAction(CREATE_NOTIFICATION);
                popUpIntent.putExtra(HOME_TO_WORK, homeToWork);
                popUpIntent.putExtra(RECORD_ID, recordIndex);
                popUpIntent.putExtra(DETAILS, departTime + " " + thread.getFromName() + " - " + thread.getToName());
                popUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                int requestCode = recordIndex + (homeToWork ? 1000 : 0);

                PendingIntent pIntent = PendingIntent.getActivity(context, requestCode, popUpIntent, 0);
                widgetView.setOnClickPendingIntent(layoutId, pIntent);
            }

        }

        // Обновляем виджет
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

    @Nullable
    private Integer indexOfNextTrains(List<TrainThread> trainThreads) {
        Date now = new Date();
        ListIterator<TrainThread> iterator = trainThreads.listIterator();
        while (iterator.hasNext()) {
            TrainThread thread = iterator.next();
            if (thread.getDeparture().compareTo(now) > 0) {
                return iterator.previousIndex();
            }
        }
        return null;
    }

}
