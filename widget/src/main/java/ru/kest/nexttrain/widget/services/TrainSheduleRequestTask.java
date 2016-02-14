package ru.kest.nexttrain.widget.services;

import android.app.AlarmManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.Getter;
import lombok.Setter;
import ru.kest.nexttrain.widget.convertors.YandexToDomainConverter;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.model.yandex.ScheduleResponse;
import ru.kest.nexttrain.widget.util.DateUtil;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;

/**
 * Created by KKharitonov on 05.01.2016.
 */
public class TrainSheduleRequestTask extends AsyncTask<Void, Void, String> {

    private static final String URL_TEMPLATE = "https://api.rasp.yandex.net/v1.0/search/?apikey=4616c13e-bcc2-49e3-b88a-5a1437ea7a40&format=json&from=%s&to=%s&lang=ru&date=%s";
    private static final String HOME_STATION_CODE = "s9601770";
    private static final String WORK_STATION_CODE = "s9601251";

    private static final String SUCCESS_RESPONSE = "OK";

    private Context context;

    private ObjectMapper mapper = getJsonMapper();

    @Getter @Setter
    private static boolean executed = false;

    public TrainSheduleRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.d(LOG_TAG, "TrainSheduleRequestTask.doInBackground()");
        try {
            List<TrainThread> fromHomeTrains = loadTrainSchedule(true);
            if (!fromHomeTrains.isEmpty()) {
                DataStorage.setTrainsFromHomeToWork(fromHomeTrains);
                Log.d(LOG_TAG, "fromHomeTrains: " + fromHomeTrains);
            }
            List<TrainThread> fromWorkTrains = loadTrainSchedule(false);
            if (!fromWorkTrains.isEmpty()) {
                DataStorage.setTrainsFromWorkToHome(fromWorkTrains);
                Log.d(LOG_TAG, "fromWorkTrains: " + fromWorkTrains);
            }
            return SUCCESS_RESPONSE;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        Log.i(LOG_TAG, "onPostExecute()");
        int timeToNextExecute;
        if (SUCCESS_RESPONSE.equals(response)) {
            Log.i(LOG_TAG, "Train schedules successfully updated: " + DataStorage.getTrainsFromHomeToWork().size() + "  " + DataStorage.getTrainsFromWorkToHome().size());
            SchedulerUtil.sendUpdateWidget(context);
            timeToNextExecute = 2 * 60; // 2 hours
        } else {
            timeToNextExecute = 5; // 2 hours
            Log.w(LOG_TAG, "unsuccess result code: " + response + ". reschedule retrieve data in 5 minute");
        }
        SchedulerUtil.scheduleTrainScheduleRequest(context, (AlarmManager) context.getSystemService(Context.ALARM_SERVICE),timeToNextExecute);
    }

    private List<TrainThread> loadTrainSchedule(boolean fromHome) throws IOException {
        String content = getUrlContent(createURL(fromHome));
//        Log.d(LOG_TAG, "service response: " + content);

        ScheduleResponse response = mapper.readValue(content, ScheduleResponse.class);

        return YandexToDomainConverter.scheduleResponseToDomain(response);
    }

    private String getUrlContent(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();
        Log.i(LOG_TAG, "Getting content for url " + url);
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } finally {
            urlConnection.disconnect();
        }

        return result.toString();
    }

    private URL createURL(boolean fromHome) throws MalformedURLException {
        String url = String.format(
                URL_TEMPLATE,
                fromHome ? HOME_STATION_CODE : WORK_STATION_CODE,
                fromHome ? WORK_STATION_CODE : HOME_STATION_CODE,
                DateUtil.getDay(new Date())
        );
        return new URL(url);
    }

    private static ObjectMapper getJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return mapper;
    }
}

