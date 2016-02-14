package ru.kest.nexttrain.widget.services.data;

import ru.kest.nexttrain.widget.model.domain.NearestStation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static ru.kest.nexttrain.widget.Constants.*;

/**
 * Created by KKharitonov on 14.02.2016.
 */
public class TimeLimits {

    static final Map<NearestStation, Map<String, Integer>> timeLimitsMap = new HashMap<>();
    static {
        Map<String, Integer> homeTimeLimits = new HashMap<>();
        homeTimeLimits.put(RED_STATUS, HOME_RED_STATUS);
        homeTimeLimits.put(YELLOW_STATUS, HOME_YELLOW_STATUS);
        homeTimeLimits.put(GREEN_STATUS, HOME_GREEN_STATUS);
//            put(WHITE_STATUS, HOME_WHITE_STATUS);
        homeTimeLimits.put(FIRST_CALL, HOME_FIRST_CALL);
        homeTimeLimits.put(LAST_CALL, HOME_LAST_CALL);

        Map<String, Integer> workTimeLimits = new HashMap<>();
        workTimeLimits.put(RED_STATUS, WORK_RED_STATUS);
        workTimeLimits.put(YELLOW_STATUS, WORK_YELLOW_STATUS);
        workTimeLimits.put(GREEN_STATUS, WORK_GREEN_STATUS);
//            put(WHITE_STATUS, WORK_WHITE_STATUS);
        workTimeLimits.put(FIRST_CALL, WORK_FIRST_CALL);
        workTimeLimits.put(LAST_CALL, WORK_LAST_CALL);

        timeLimitsMap.put(NearestStation.HOME, homeTimeLimits);
        timeLimitsMap.put(NearestStation.WORK, workTimeLimits);
    };

    private NearestStation nearestStation;

    public TimeLimits(DataProvider dataProvider) {
        this.nearestStation = dataProvider.getNearestStation();
    }

    public int getTimeLimit(String key) {
        return timeLimitsMap.get(nearestStation).get(key);
    }

    public static int getTimeDiffInMinutes(Date time) {
        long diffInMillies = System.currentTimeMillis() - time.getTime();
        return (int) TimeUnit.MINUTES.convert(Math.abs(diffInMillies), TimeUnit.MILLISECONDS);
    }
}
