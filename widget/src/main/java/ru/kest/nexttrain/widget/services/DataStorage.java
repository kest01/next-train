package ru.kest.nexttrain.widget.services;

import android.location.Location;
import ru.kest.nexttrain.widget.model.domain.TrainThread;

import java.util.List;

/**
 * Created by KKharitonov on 07.01.2016.
 */
public class DataStorage {

    private static List<TrainThread> trainsFromHomeToWork;
    private static List<TrainThread> trainsFromWorkToHome;
    private static Location lastLocation;

    public static List<TrainThread> getTrainsFromHomeToWork() {
        return trainsFromHomeToWork;
    }

    public static void setTrainsFromHomeToWork(List<TrainThread> trainsFromHomeToWork) {
        DataStorage.trainsFromHomeToWork = trainsFromHomeToWork;
    }

    public static boolean isSetTrainThreads() {
        return trainsFromHomeToWork != null && trainsFromHomeToWork.size() > 0
                && trainsFromWorkToHome != null && trainsFromWorkToHome.size() > 0;
    }

    public static List<TrainThread> getTrainsFromWorkToHome() {
        return trainsFromWorkToHome;
    }

    public static void setTrainsFromWorkToHome(List<TrainThread> trainsFromWorkToHome) {
        DataStorage.trainsFromWorkToHome = trainsFromWorkToHome;
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


}
