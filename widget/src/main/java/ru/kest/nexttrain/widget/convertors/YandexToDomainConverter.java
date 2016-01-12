package ru.kest.nexttrain.widget.convertors;

import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.model.yandex.TrainScheduleResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by KKharitonov on 07.01.2016.
 */
public class YandexToDomainConverter {

    public static List<TrainThread> scheduleResponseToDomain(TrainScheduleResponse trainScheduleResponse) {
        List<TrainThread> result = new ArrayList<>();
        if (trainScheduleResponse != null && trainScheduleResponse.getThreads() != null && trainScheduleResponse.getThreads().size() > 0) {
            for (ru.kest.nexttrain.widget.model.yandex.TrainThread threadYandex : trainScheduleResponse.getThreads()) {
                TrainThread threadDomain = new TrainThread();
                threadDomain.setArrival(threadYandex.getArrival());
                threadDomain.setDeparture(threadYandex.getDeparture());
                threadDomain.setFromCode(threadYandex.getFrom().getCode());
                threadDomain.setFromName(threadYandex.getFrom().getTitle());
                threadDomain.setToCode(threadYandex.getTo().getCode());
                threadDomain.setToName(threadYandex.getTo().getTitle());
                threadDomain.setTitle(threadYandex.getThread().getTitle());
                result.add(threadDomain);
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
