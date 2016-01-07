package ru.kest.nexttrain.widget.services;

import android.location.Location;
import ru.kest.nexttrain.widget.model.domain.TrainThread;

import java.util.List;

/**
 * Created by KKharitonov on 07.01.2016.
 */
public class DataStorage {

    private static List<TrainThread> trainThreads;
    private static Location lastLocation;

    private static final Location homeLocation = new Location("");
    private static final Location workLocation = new Location("");
    {
        homeLocation.setLatitude(55.8300989);
        homeLocation.setLongitude(37.2187062);

        workLocation.setLatitude(55.802753);
        workLocation.setLongitude(37.491259);
    }

    public static List<TrainThread> getTrainThreads() {
        return trainThreads;
    }

    public static void setTrainThreads(List<TrainThread> trainThreads) {
        DataStorage.trainThreads = trainThreads;
    }

    public static Location getLastLocation() {
        return lastLocation;
    }

    public static boolean isSetLastLocation() {
        return lastLocation != null;
    }

    public static void setLastLocation(Location lastLocation) {
        DataStorage.lastLocation = lastLocation;
    }

    public static int getDistanceToWork() {
        if (lastLocation != null) {
            return (int) workLocation.distanceTo(lastLocation);
        }
        return 0;
    }

    public static int getDistanceToHome() {
        if (lastLocation != null) {
            return (int) homeLocation.distanceTo(lastLocation);
        }
        return 0;
    }


}
