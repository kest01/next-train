package ru.kest.nexttrain.widget.services.data;

import android.location.Location;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.kest.nexttrain.widget.model.domain.TrainThread;

import java.util.List;

/**
 * Created by KKharitonov on 07.01.2016.
 */
@Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE) @AllArgsConstructor(suppressConstructorProperties = true)
public class FieldDataStorage {

    private List<TrainThread> trainsFromHomeToWork;
    private List<TrainThread> trainsFromWorkToHome;
    private Location lastLocation;
    private TrainThread notificationTrain;


}
