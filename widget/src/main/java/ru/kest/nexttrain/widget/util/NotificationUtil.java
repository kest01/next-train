package ru.kest.nexttrain.widget.util;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ru.kest.nexttrain.widget.R;
import ru.kest.nexttrain.widget.TrainsWidget;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.services.DataService;

import java.util.Date;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;
import static ru.kest.nexttrain.widget.util.Constants.DELETED_NOTIFICATION;
import static ru.kest.nexttrain.widget.util.Constants.NOTIFICATION_ID;

/**
 * Created by KKharitonov on 13.01.2016.
 */
public class NotificationUtil {

    public static void createOrUpdateNotification(Context context) {
        TrainThread thread = DataService.getDataProvider(context).getNotificationTrain();
        Log.d(LOG_TAG, "createOrUpdateNotification: " + thread);
        if (thread == null) {
            return;
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Электричка через " + getDiffInMinutes(thread.getDeparture()) + " мин")
                        .setContentText(DateUtil.getTimeWithSeconds(new Date()) + " " + DateUtil.getTime(thread.getDeparture()) + " " + thread.getTitle());

        Intent deleteIntent = new Intent(context, TrainsWidget.class);
        deleteIntent.setAction(DELETED_NOTIFICATION);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, 0);

        mBuilder.setDeleteIntent(pIntent);

        // отправляем
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, mBuilder.build());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SchedulerUtil.scheduleUpdateNotification(context, alarmManager);
    }

    private static long getDiffInMinutes(Date time) {
        long diff = Math.abs(time.getTime() - System.currentTimeMillis());
        return diff / (60 * 1000);
    }
}
