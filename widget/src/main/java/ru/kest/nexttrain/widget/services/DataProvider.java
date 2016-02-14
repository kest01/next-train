package ru.kest.nexttrain.widget.services;

import android.location.Location;
import ru.kest.nexttrain.widget.model.domain.NearestStation;
import ru.kest.nexttrain.widget.model.domain.TrainThread;

import java.util.List;

/**
 * Created by KKharitonov on 14.02.2016.
 */
public interface DataProvider {

    List<TrainThread> getTrainsFromHomeToWork();
    void setTrainsFromHomeToWork(List<TrainThread> trainsFromHomeToWork);

    List<TrainThread> getTrainsFromWorkToHome();
    void setTrainsFromWorkToHome(List<TrainThread> trainsFromWorkToHome);

    boolean isSetTrainThreads();
    TrainThread getThreadByHash(int hash);

    Location getLastLocation();
    void setLastLocation(Location location);
    boolean isSetLastLocation();

    TrainThread getNotificationTrain();
    void setNotificationTrain(TrainThread notificationTrain);
    boolean isSetNotificationTrain();

    NearestStation getNearestStation();
}
