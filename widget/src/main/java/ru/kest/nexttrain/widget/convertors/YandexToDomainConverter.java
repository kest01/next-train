package ru.kest.nexttrain.widget.convertors;

import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.model.yandex.ScheduleResponse;

import java.util.*;

/**
 * Created by KKharitonov on 07.01.2016.
 */
public class YandexToDomainConverter {

    public static List<TrainThread> scheduleResponseToDomain(ScheduleResponse trainScheduleResponse) {
        List<TrainThread> result = new ArrayList<>();
        if (trainScheduleResponse != null && trainScheduleResponse.getThreads() != null && trainScheduleResponse.getThreads().size() > 0) {
            for (ScheduleResponse.TrainThread yandexThread : trainScheduleResponse.getThreads()) {
                if (yandexThread.getDeparture().before(new Date())) {
                    continue;
                }
                TrainThread domainThread = new TrainThread();
                domainThread.setArrival(yandexThread.getArrival());
                domainThread.setDeparture(yandexThread.getDeparture());
                domainThread.setTitle(yandexThread.getThread().getShortTitle());

                if (yandexThread.getThread().getShortTitle() != null) {
                    String[] stations = yandexThread.getThread().getShortTitle().split(" - ");
                    domainThread.setFrom(stations[0]);
                    domainThread.setTo(stations[1]);
                }
                result.add(domainThread);
            }
            Collections.sort(result, new Comparator<TrainThread>() {
                @Override
                public int compare(TrainThread lhs, TrainThread rhs) {
                    return lhs.getDeparture().compareTo(rhs.getDeparture());
                }
            });
        }
        return result;
    }
}
