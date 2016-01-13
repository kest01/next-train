package ru.kest.nexttrain.widget.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ru.kest.nexttrain.widget.R;
import ru.kest.nexttrain.widget.TrainsWidget;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.services.DataStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;
import static ru.kest.nexttrain.widget.util.Constants.DELETED_NOTIFICATION;
import static ru.kest.nexttrain.widget.util.Constants.NOTIFICATION_ID;

/**
 * Created by KKharitonov on 13.01.2016.
 */
public class NotificationUtil {

    public static void createOrUpdateNotification(Context context) {
        TrainThread thread = DataStorage.getNotificationTrain();
        Log.d(LOG_TAG, "createOrUpdateNotification: " + thread);
        if (thread == null) {
            return;
        }
        //Comparing dates
        long diff = Math.abs(thread.getDeparture().getTime() - System.currentTimeMillis());
        long diffMin = diff / (60 * 1000);

        DateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        String departTime = dateFormatter.format(thread.getDeparture());

        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Электричка через " + diffMin + " мин")
                        .setContentText(currentTime + " " + departTime + " " + thread.getFromName() + " - " + thread.getToName());

        Intent deleteIntent = new Intent(context, TrainsWidget.class);
        deleteIntent.setAction(DELETED_NOTIFICATION);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, 0);

        mBuilder.setDeleteIntent(pIntent);

        // отправляем
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
