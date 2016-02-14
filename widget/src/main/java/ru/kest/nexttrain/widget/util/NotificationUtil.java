package ru.kest.nexttrain.widget.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ru.kest.nexttrain.widget.R;
import ru.kest.nexttrain.widget.TrainsWidget;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.services.data.DataService;
import ru.kest.nexttrain.widget.services.data.TimeLimits;
import ru.kest.nexttrain.widget.ui.UIUpdater;

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

        int remainMinutes = TimeLimits.getTimeDiffInMinutes(thread.getDeparture());
        String remainText = UIUpdater.getRemainText(remainMinutes);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, createNotification(context, remainText, thread));

        checkAndPlaySound(context, remainMinutes);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SchedulerUtil.scheduleUpdateNotification(context, alarmManager);
    }

    private static Notification createNotification(Context context, String remainText, TrainThread thread) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Электричка через " + remainText)
                        .setTicker(remainText)
                        .setContentText(DateUtil.getTimeWithSeconds(new Date()) + " " + DateUtil.getTime(thread.getDeparture()) + " " + thread.getTitle());

        mBuilder.setDeleteIntent(getDeletePendingIntent(context));
        return mBuilder.build();
    }

    private static void checkAndPlaySound(Context context, int remainMinutes) {
        TimeLimits tl = new TimeLimits(DataService.getDataProvider(context));
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (tl.getTimeLimit(FIRST_CALL) == remainMinutes) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, soundUri);
            r.play();
            vibrator.vibrate(new long[] {0, 500, 200, 500}, -1);
        } else if (tl.getTimeLimit(LAST_CALL) == remainMinutes) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, soundUri);
            r.play();
            vibrator.vibrate(new long[] {0, 500, 200, 500, 200, 500}, -1);
        }

    }

    private static PendingIntent getDeletePendingIntent(Context context) {
        Intent deleteIntent = new Intent(context, TrainsWidget.class);
        deleteIntent.setAction(DELETED_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, deleteIntent, 0);
    }
}
