package ru.kest.nexttrain.widget.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.services.FieldDataStorage;

import java.util.List;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;

/**
 * Created by KKharitonov on 14.02.2016.
 */
public class PreferencesUtil {

    private static final String PREF_NAME = "NextTrainPref";

    private static final String TRAINS_FROM_HOME = "TRAINS_FROM_HOME";
    private static final String TRAINS_FROM_WORK = "TRAINS_FROM_WORK";
    private static final String NOTIFICATION_TRAIN = "NOTIFICATION_TRAIN";
    private static final String LOCATION_LATITUDE = "LOCATION_LATITUDE";
    private static final String LOCATION_LONGITUDE = "LOCATION_LONGITUDE";

    public static FieldDataStorage createDataStorage(Context context) {
        List<TrainThread> trainsFromHomeToWork = null;
        List<TrainThread> trainsFromWorkToHome = null;
        Location lastLocation = null;
        TrainThread notificationTrain = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String json = preferences.getString(TRAINS_FROM_HOME, null);
        Log.d(LOG_TAG, "JSON: " + json);
        if (json != null) {
            trainsFromHomeToWork = JsonUtil.stringToList(json, TrainThread.class);
        }

        json = preferences.getString(TRAINS_FROM_WORK, null);
        Log.d(LOG_TAG, "JSON: " + json);
        if (json != null) {
            trainsFromWorkToHome = JsonUtil.stringToList(json, TrainThread.class);
        }

        json = preferences.getString(NOTIFICATION_TRAIN, null);
        Log.d(LOG_TAG, "JSON: " + json);
        if (json != null) {
            notificationTrain = JsonUtil.stringToObject(json, TrainThread.class);
        }

        Double locationLatitude = getDouble(preferences, LOCATION_LATITUDE, null);
        Double locationLongitude = getDouble(preferences, LOCATION_LONGITUDE, null);
        Log.d(LOG_TAG, "location: {" + locationLatitude + "," + locationLongitude + "}");
        if (locationLatitude != null && locationLongitude != null) {
            lastLocation = new Location("");
            lastLocation.setLatitude(locationLatitude);
            lastLocation.setLongitude(locationLongitude);
        }

        return new FieldDataStorage(trainsFromHomeToWork, trainsFromWorkToHome, lastLocation, notificationTrain);
    }

    public static void saveLastLocation(Context context, Location location) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        putDouble(editor, LOCATION_LATITUDE, location.getLatitude());
        putDouble(editor, LOCATION_LONGITUDE, location.getLongitude());
        editor.apply();
    }

    public static void saveTrainsFromHomeToWork(Context context, List<TrainThread> trains) {
        saveTrains(context, TRAINS_FROM_HOME, trains);
    }

    public static void saveTrainsFromWorkToHome(Context context, List<TrainThread> trains) {
        saveTrains(context, TRAINS_FROM_WORK, trains);
    }


    public static void saveNotificationTrain(Context context, TrainThread train) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        String json = JsonUtil.objectToString(train);
        Log.d(LOG_TAG, "saveTrains:" + json);
        editor.putString(NOTIFICATION_TRAIN, json);
        editor.apply();
    }

    private static void saveTrains(Context context, String key, List<TrainThread> trains) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        String json = JsonUtil.objectToString(trains);
        Log.d(LOG_TAG, "saveTrains:(" + key + ") " + json);
        editor.putString(key, json);
        editor.apply();
    }

    private static SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private static Double getDouble(final SharedPreferences prefs, final String key, final Double defaultValue) {
        if (!prefs.contains(key)) {
            return defaultValue;
        }
        return Double.longBitsToDouble(prefs.getLong(key, 0));
    }
}
