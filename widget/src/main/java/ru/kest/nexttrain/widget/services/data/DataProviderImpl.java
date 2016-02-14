package ru.kest.nexttrain.widget.services.data;

import android.content.Context;
import android.location.Location;
import ru.kest.nexttrain.widget.model.domain.NearestStation;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.util.PreferencesUtil;

import java.util.List;

/**
 * Created by KKharitonov on 14.02.2016.
 */
public class DataProviderImpl implements DataProvider{

    private FieldDataStorage dataStorage;
    private Context context;

    protected DataProviderImpl(Context context) {
        this.context = context;
        this.dataStorage = PreferencesUtil.createDataStorage(context);
    }

    @Override
    public List<TrainThread> getTrainsFromHomeToWork() {
        return dataStorage.getTrainsFromHomeToWork();
    }

    @Override
    public void setTrainsFromHomeToWork(List<TrainThread> trainsFromHomeToWork) {
        PreferencesUtil.saveTrainsFromHomeToWork(context, trainsFromHomeToWork);
        dataStorage.setTrainsFromHomeToWork(trainsFromHomeToWork);
    }

    @Override
    public List<TrainThread> getTrainsFromWorkToHome() {
        return dataStorage.getTrainsFromWorkToHome();
    }

    @Override
    public void setTrainsFromWorkToHome(List<TrainThread> trainsFromWorkToHome) {
        PreferencesUtil.saveTrainsFromWorkToHome(context, trainsFromWorkToHome);
        dataStorage.setTrainsFromWorkToHome(trainsFromWorkToHome);
    }

    @Override
    public boolean isSetTrainThreads() {
        return getTrainsFromHomeToWork() != null && getTrainsFromWorkToHome() != null;
    }

    @Override
    public TrainThread getThreadByHash(int hash) {
        TrainThread result = getThreadByHash(getTrainsFromHomeToWork(), hash);
        if (result == null) {
            result = getThreadByHash(getTrainsFromWorkToHome(), hash);
        }
        return result;
    }

    @Override
    public Location getLastLocation() {
        return dataStorage.getLastLocation();
    }

    @Override
    public void setLastLocation(Location location) {
        PreferencesUtil.saveLastLocation(context, location);
        dataStorage.setLastLocation(location);
    }

    @Override
    public boolean isSetLastLocation() {
        return getLastLocation() != null;
    }

    @Override
    public TrainThread getNotificationTrain() {
        return dataStorage.getNotificationTrain();
    }

    @Override
    public void setNotificationTrain(TrainThread notificationTrain) {
        PreferencesUtil.saveNotificationTrain(context, notificationTrain);
        dataStorage.setNotificationTrain(notificationTrain);
    }

    @Override
    public boolean isSetNotificationTrain() {
        return getNotificationTrain() != null;
    }

    @Override
    public NearestStation getNearestStation() {
        if (isSetLastLocation()) {
            return NearestStation.getNearestStation(getLastLocation());
        }
        return null;
    }

    private TrainThread getThreadByHash(List<TrainThread> threads, int hash) {
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
