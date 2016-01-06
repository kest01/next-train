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
                        context, createUpdateWidgetIntent(context)
                )
        );
    }

    public static void sendUpdateWidget(Context context) {
        Intent intent = createUpdateWidgetIntent(context);
        context.sendBroadcast(intent);
    }

    public static void sendUpdateLocation(Context context) {
        Intent intent = createUpdateLocationIntent(context);
        context.sendBroadcast(intent);
    }

    public static void cancelScheduleUpdateWidget(Context context, AlarmManager alarmManager) {
        alarmManager.cancel(
                createBroadcastPI(
                    context, createUpdateWidgetIntent(context)
                )
        );
    }

    public static void cancelScheduleUpdateLocation(Context context, AlarmManager alarmManager) {
        alarmManager.cancel(
                createBroadcastPI(
                    context, createUpdateLocationIntent(context)
                )
        );
    }

    public static void scheduleUpdateLocation(Context context, AlarmManager alarmManager) {
        alarmManager.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis(),
                60000,
                createBroadcastPI(
                        context, createUpdateLocationIntent(context)
                )
        );
    }

    private static Intent createUpdateWidgetIntent(Context context) {
        Intent intent = new Intent(context, TrainsWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        return intent;
    }

    private static Intent createUpdateLocationIntent(Context context) {
        Intent intent = new Intent(context, TrainsWidget.class);
        intent.setAction(UPDATE_LOCATION);
        return intent;
    }

    private static PendingIntent createBroadcastPI(Context context, Intent intent) {
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


}
