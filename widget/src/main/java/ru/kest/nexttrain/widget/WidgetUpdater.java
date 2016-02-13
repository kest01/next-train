package ru.kest.nexttrain.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import ru.kest.nexttrain.PopUpActivity;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.services.DataStorage;
import ru.kest.nexttrain.widget.services.LocationClient;
import ru.kest.nexttrain.widget.util.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;
import static ru.kest.nexttrain.widget.util.Constants.CREATE_NOTIFICATION;
import static ru.kest.nexttrain.widget.util.Constants.DETAILS;
import static ru.kest.nexttrain.widget.util.Constants.RECORD_HASH;

/**
 * Created by KKharitonov on 13.02.2016.
 */
public class WidgetUpdater {

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {

        List<TrainThread> trainThreads = null;
        Integer indexOfNextTrains = null;

        if (DataStorage.isSetTrainThreads() && DataStorage.isSetLastLocation()) {
            if (LocationClient.getNearestStation() == LocationClient.NearestStation.HOME) {
                trainThreads = DataStorage.getTrainsFromHomeToWork();
            } else {
                trainThreads = DataStorage.getTrainsFromWorkToHome();
            }
            indexOfNextTrains = indexOfNextTrains(trainThreads);
        }
        Log.d(LOG_TAG, "updateWidget: " + indexOfNextTrains + " : " + (trainThreads == null ? null : trainThreads.size()));

        // Помещаем данные в текстовые поля
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
        Resources res = context.getResources();

        for (int i = 1; i <= 2; i++) {
            int timeId = res.getIdentifier("time" + i, "id", context.getPackageName());
            int threadTitleId = res.getIdentifier("threadTitle" + i, "id", context.getPackageName());
            int layoutId = res.getIdentifier("ll" + i, "id", context.getPackageName());

            // Clear all field
            widgetView.setTextViewText(timeId, "");
            widgetView.setTextViewText(threadTitleId, "");

            if (indexOfNextTrains != null && (indexOfNextTrains + i) <= trainThreads.size()) {
                int recordIndex = indexOfNextTrains + i - 1;
                TrainThread thread = trainThreads.get(recordIndex);

                widgetView.setTextViewText(timeId,
                        DateUtil.getTime(thread.getDeparture()) + " - " + DateUtil.getTime(thread.getArrival()));
                widgetView.setTextViewText(threadTitleId, thread.getTitle());

                widgetView.setOnClickPendingIntent(layoutId, createPopUpDialogPendingIntent(context, thread));
            }

        }

        // Обновляем виджет
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

    private static PendingIntent createPopUpDialogPendingIntent(Context context, TrainThread thread) {
        Intent popUpIntent = new Intent(context, PopUpActivity.class);
        popUpIntent.setAction(CREATE_NOTIFICATION);
        popUpIntent.putExtra(DETAILS, DateUtil.getTime(thread.getDeparture()) + " " + thread.getTitle());
        popUpIntent.putExtra(RECORD_HASH, thread.hashCode());
        popUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return PendingIntent.getActivity(context, 0, popUpIntent, 0);
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
