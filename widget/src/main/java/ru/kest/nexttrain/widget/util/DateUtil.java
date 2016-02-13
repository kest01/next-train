package ru.kest.nexttrain.widget.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KKharitonov on 14.02.2016.
 */
public class DateUtil {

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat timeWithSecondsFormatter = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public static String getTime(Date date) {
        return timeFormatter.format(date);
    }

    public static String getTimeWithSeconds(Date date) {
        return timeWithSecondsFormatter.format(date);
    }

    public static String getDay(Date date) {
        return dayFormatter.format(date);
    }

}
