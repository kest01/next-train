package ru.kest.nexttrain.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import ru.kest.nexttrain.PopUpActivity;
import ru.kest.nexttrain.widget.model.domain.NearestStation;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.services.DataProvider;
import ru.kest.nexttrain.widget.services.DataService;
import ru.kest.nexttrain.widget.util.DateUtil;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;
import static ru.kest.nexttrain.widget.util.Constants.*;

/**
 * Created by KKharitonov on 13.02.2016.
 */
public class WidgetUpdater {

    private static final int ELEMENT_COUNT = 4;
    private static final String PACKAGE_NAME = "ru.kest.nexttrain.widget";

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AlarmManager alarmManager = SchedulerUtil.getAlarmManager(context);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }

        if (!DataService.getDataProvider(context).isSetTrainThreads()) {
            SchedulerUtil.sendTrainScheduleRequest(context, alarmManager);
        }
        SchedulerUtil.scheduleUpdateWidget(context, alarmManager);
        Toast.makeText(context, "updateWidget", Toast.LENGTH_SHORT).show();
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        RemoteViews widgetView = new RemoteViews(PACKAGE_NAME, R.layout.widget);
        clearAllWidgetFields(context, widgetView);

        DataProvider dataProvider = DataService.getDataProvider(context);

        if (dataProvider.isSetTrainThreads() && dataProvider.isSetLastLocation()) {
            List<TrainThread> trainThreads = getTrainsToDisplay(dataProvider);
            for (int i = 0; i < ELEMENT_COUNT && i < trainThreads.size(); i++) {
                updateThread(context, widgetView, i, trainThreads.get(i));
            }
            // Обновляем виджет
            appWidgetManager.updateAppWidget(widgetID, widgetView);
            Log.d(LOG_TAG, "updateWidget: successful");
        } else {
            Log.d(LOG_TAG, "updateWidget: nothing to show");
        }
    }

    private static void clearAllWidgetFields(Context context, RemoteViews widgetView) {
        Resources res = context.getResources();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            // Clear all field
            widgetView.setTextViewText(getElementId(res, "time", i), "");
            widgetView.setTextViewText(getElementId(res, "from", i), "");
            widgetView.setTextViewText(getElementId(res, "to", i), "");
        }
    }

    private static void updateThread(Context context, RemoteViews widgetView, int threadNum, TrainThread thread) {
        Resources res = context.getResources();

        widgetView.setTextViewText(getElementId(res, "time", threadNum), DateUtil.getTime(thread.getDeparture()) + " - " + DateUtil.getTime(thread.getArrival()));
        widgetView.setTextViewText(getElementId(res, "from", threadNum), thread.getFrom());
        widgetView.setTextViewText(getElementId(res, "to",   threadNum), thread.getTo());

        widgetView.setOnClickPendingIntent(getElementId(res, "ll", threadNum), createPopUpDialogPendingIntent(context, thread));
    }

    private static int getElementId(Resources res, String label, int threadNum) {
        return res.getIdentifier(label + threadNum, "id", PACKAGE_NAME);
    }

    private static PendingIntent createPopUpDialogPendingIntent(Context context, TrainThread thread) {
        Intent popUpIntent = new Intent(context, PopUpActivity.class);
        popUpIntent.setAction(CREATE_NOTIFICATION);
        popUpIntent.putExtra(DETAILS, DateUtil.getTime(thread.getDeparture()) + " " + thread.getTitle());
        popUpIntent.putExtra(RECORD_HASH, thread.hashCode());
        popUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return PendingIntent.getActivity(context, thread.hashCode(), popUpIntent, 0);
    }

    private static List<TrainThread> getTrainsToDisplay(DataProvider dataProvider) {
        List<TrainThread> trainThreads;
        if (dataProvider.getNearestStation() == NearestStation.HOME) {
            trainThreads = dataProvider.getTrainsFromHomeToWork();
        } else {
            trainThreads = dataProvider.getTrainsFromWorkToHome();
        }
        Integer indexOfNextTrains = indexOfNextTrains(trainThreads);
        if (indexOfNextTrains != null) {
            return trainThreads.subList(indexOfNextTrains, trainThreads.size());
        } else {
            return Collections.emptyList();
        }
    }

    @Nullable
    private static Integer indexOfNextTrains(List<TrainThread> trainThreads) {
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
