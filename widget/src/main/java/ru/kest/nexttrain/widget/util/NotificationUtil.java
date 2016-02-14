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
import ru.kest.nexttrain.widget.services.data.DataService;
import ru.kest.nexttrain.widget.services.data.TimeLimits;

import java.util.Date;

import static ru.kest.nexttrain.widget.Constants.*;

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
                        .setContentTitle("Электричка через " + TimeLimits.getTimeDiffInMinutes(thread.getDeparture()) + " мин")
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
}
