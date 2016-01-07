package ru.kest.nexttrain.widget.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import ru.kest.nexttrain.widget.TrainsWidget;

import static ru.kest.nexttrain.widget.util.Constants.*;

/**
 * Created by KKharitonov on 06.01.2016.
 */
public class SchedulerUtil {


    public static void scheduleUpdateWidget(Context context, AlarmManager alarmManager) {
        alarmManager.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis()+3000,
                60000,
                createBroadcastPI(
                        context, createIntent(context, UPDATE_ALL_WIDGETS)
                )
        );
    }

    public static void scheduleUpdateLocation(Context context, AlarmManager alarmManager) {
        alarmManager.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis(),
                60000,
                createBroadcastPI(
                        context, createIntent(context, UPDATE_LOCATION)
                )
        );
    }

    public static void sendUpdateWidget(Context context) {
        Intent intent = createIntent(context, UPDATE_ALL_WIDGETS);
        context.sendBroadcast(intent);
    }

    public static void sendUpdateLocation(Context context) {
        Intent intent = createIntent(context, UPDATE_LOCATION);
        context.sendBroadcast(intent);
    }

    public static void sendTrainScheduleRequest(Context context) {
        Intent intent = createIntent(context, TRAIN_SCHEDULE_REQUEST);
        context.sendBroadcast(intent);
    }

    public static void cancelScheduleUpdateWidget(Context context, AlarmManager alarmManager) {
        alarmManager.cancel(
                createBroadcastPI(
                    context, createIntent(context, UPDATE_ALL_WIDGETS)
                )
        );
    }

    public static void cancelScheduleUpdateLocation(Context context, AlarmManager alarmManager) {
        alarmManager.cancel(
                createBroadcastPI(
                    context, createIntent(context, UPDATE_LOCATION)
                )
        );
    }

    private static Intent createIntent(Context context, String action) {
        Intent intent = new Intent(context, TrainsWidget.class);
        intent.setAction(action);
        return intent;
    }

    private static PendingIntent createBroadcastPI(Context context, Intent intent) {
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


}
