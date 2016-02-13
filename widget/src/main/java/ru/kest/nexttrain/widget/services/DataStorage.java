package ru.kest.nexttrain.widget.services;

import android.location.Location;
import lombok.Getter;
import lombok.Setter;
import ru.kest.nexttrain.widget.model.domain.TrainThread;

import java.util.List;

/**
 * Created by KKharitonov on 07.01.2016.
 */
public class DataStorage {

    @Getter @Setter
    private static List<TrainThread> trainsFromHomeToWork;
    @Getter @Setter
    private static List<TrainThread> trainsFromWorkToHome;
    @Getter @Setter
    private static Location lastLocation;
    @Getter @Setter
    private static TrainThread notificationTrain;

    public static boolean isSetTrainThreads() {
        return trainsFromHomeToWork != null && trainsFromHomeToWork.size() > 0
                && trainsFromWorkToHome != null && trainsFromWorkToHome.size() > 0;
    }

    public static boolean isSetLastLocation() {
        return lastLocation != null;
    }

    public static boolean isSetNotificationTrain() {
        return notificationTrain != null;
    }

    public static TrainThread getThreadByHash(int hash) {
        TrainThread result = getThreadByHash(trainsFromHomeToWork, hash);
        if (result == null) {
            result = getThreadByHash(trainsFromWorkToHome, hash);
        }
        return result;
    }
    private static TrainThread getThreadByHash(List<TrainThread> threads, int hash) {
        if (threads != null) {
            for (TrainThread thread : threads) {
                if (thread.hashCode() == hash) {
                    return thread;
                }
            }
        }
        return null;
    }
}
