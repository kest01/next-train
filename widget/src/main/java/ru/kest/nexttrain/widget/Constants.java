package ru.kest.nexttrain.widget;

/**
 * Created by KKharitonov on 06.01.2016.
 */
public interface Constants {

    // Time limits
    int HOME_RED_STATUS = 15;
    int HOME_YELLOW_STATUS = 25;
    int HOME_GREEN_STATUS = 40;
//    int HOME_WHITE_STATUS = 40;
    int HOME_FIRST_CALL = 12;
    int HOME_LAST_CALL = 6;

    int WORK_RED_STATUS = 20;
    int WORK_YELLOW_STATUS = 25;
    int WORK_GREEN_STATUS = 35;
//    int WORK_WHITE_STATUS = 50;
    int WORK_FIRST_CALL = 25;
    int WORK_LAST_CALL = 20;

    // static preferences
    int ELEMENT_COUNT = 4;

    // General
    final static public String LOG_TAG = "nextTrainsLogs";
    public static final String PACKAGE_NAME = "ru.kest.nexttrain.widget";

    // Actions
    String UPDATE_ALL_WIDGETS = "UPDATE_ALL_WIDGETS";
    String UPDATE_LOCATION = "UPDATE_LOCATION";
    String TRAIN_SCHEDULE_REQUEST = "TRAIN_SCHEDULE_REQUEST";
    String CREATE_NOTIFICATION = "CREATE_NOTIFICATION";
    String UPDATE_NOTIFICATION = "UPDATE_NOTIFICATION";
    String DELETED_NOTIFICATION = "DELETED_NOTIFICATION";

    // Extras
    String RECORD_HASH = "RECORD_HASH";
    String DETAILS = "DETAILS";

    // Notification
    int NOTIFICATION_ID = 1;

    // Time limit labels
    String RED_STATUS = "RED_STATUS";
    String YELLOW_STATUS = "YELLOW_STATUS";
    String GREEN_STATUS = "GREEN_STATUS";
//    String WHITE_STATUS = "WHITE_STATUS";
    String FIRST_CALL = "FIRST_CALL";
    String LAST_CALL = "LAST_CALL";
}
