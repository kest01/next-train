package ru.kest.nexttrain.widget.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import ru.kest.nexttrain.widget.convertors.YandexToDomainConverter;
import ru.kest.nexttrain.widget.model.domain.TrainThread;
import ru.kest.nexttrain.widget.model.yandex.TrainScheduleResponse;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;

/**
 * Created by KKharitonov on 05.01.2016.
 */
public class TrainSheduleRequestTask extends AsyncTask<Void, Void, String> {

    private static final String URL = "https://api.rasp.yandex.net/v1.0/search/?apikey={ключ}&format=json&from=c146&to=c213&lang=ru&page=1&date=2015-09-02";


    private Context context;

    public TrainSheduleRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.d(LOG_TAG, "TrainSheduleRequestTask.doInBackground()");
        try {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            TrainScheduleResponse response = mapper.readValue(MOCK_JSON, TrainScheduleResponse.class);
            Log.i(LOG_TAG, response.toString());

            if (response != null && response.getThreads() != null && response.getThreads().size() > 0) {
                List<TrainThread> trainThreads = YandexToDomainConverter.scheduleResponseToDomain(response);
                DataStorage.setTrainsFromHomeToWork(trainThreads);
                DataStorage.setTrainsFromWorkToHome(trainThreads);
                Log.d(LOG_TAG, "TrainThreads" + trainThreads);
            }

            SchedulerUtil.sendUpdateWidget(context);
            return "ok";
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i(LOG_TAG, "onPostExecute()");
        SchedulerUtil.sendUpdateWidget(context);
    }


    private static final String MOCK_JSON = "{\n" +
            "  \"pagination\": {\n" +
            "    \"has_next\": false,\n" +
            "    \"per_page\": 100,\n" +
            "    \"page_count\": 1,\n" +
            "    \"total\": 9,\n" +
            "    \"page\": 1\n" +
            "  },\n" +
            "  \"threads\": [\n" +
            "    {\n" +
            "      \"arrival\": \"2016-01-15 15:15:00\",\n" +
            "      \"departure\": \"2016-01-15 14:15:00\",\n" +
            "      \"duration\": 8100.0,\n" +
            "      \"arrival_terminal\": \"D\",\n" +
            "      \"arrival_platform\": null,\n" +
            "      \"from\": {\n" +
            "        \"code\": \"s9600396\",\n" +
            "        \"station_type\": \"аэропорт\",\n" +
            "        \"title\": \"Симферополь\",\n" +
            "        \"popular_title\": \"\",\n" +
            "        \"short_title\": \"\",\n" +
            "        \"transport_type\": \"plane\",\n" +
            "        \"type\": \"station\"\n" +
            "      },\n" +
            "      \"thread\": {\n" +
            "        \"carrier\": {\n" +
            "          \"title\": \"Аэрофлот\",\n" +
            "          \"code\": 26,\n" +
            "          \"codes\": {\n" +
            "            \"icao\": null,\n" +
            "            \"sirena\": \"СУ\",\n" +
            "            \"iata\": \"SU\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"transport_type\": \"plane\",\n" +
            "        \"uid\": \"SU-1827A_c26_agent\",\n" +
            "        \"title\": \"Симферополь - Москва\",\n" +
            "        \"vehicle\": \"Airbus А320\",\n" +
            "        \"number\": \"SU 1827\",\n" +
            "        \"short_title\": \"Симферополь - Москва\",\n" +
            "        \"express_type\": null\n" +
            "      },\n" +
            "      \"departure_platform\": null,\n" +
            "      \"stops\": \"\",\n" +
            "      \"to\": {\n" +
            "        \"code\": \"s9600213\",\n" +
            "        \"station_type\": \"аэропорт\",\n" +
            "        \"title\": \"Шереметьево\",\n" +
            "        \"popular_title\": \"\",\n" +
            "        \"short_title\": \"\",\n" +
            "        \"transport_type\": \"plane\",\n" +
            "        \"type\": \"station\"\n" +
            "      },\n" +
            "      \"departure_terminal\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"arrival\": \"2016-01-15 16:15:00\",\n" +
            "      \"departure\": \"2016-01-15 15:15:00\",\n" +
            "      \"duration\": 8100.0,\n" +
            "      \"arrival_terminal\": \"D\",\n" +
            "      \"arrival_platform\": null,\n" +
            "      \"from\": {\n" +
            "        \"code\": \"s9600396\",\n" +
            "        \"station_type\": \"аэропорт\",\n" +
            "        \"title\": \"Симферополь\",\n" +
            "        \"popular_title\": \"\",\n" +
            "        \"short_title\": \"\",\n" +
            "        \"transport_type\": \"plane\",\n" +
            "        \"type\": \"station\"\n" +
            "      },\n" +
            "      \"thread\": {\n" +
            "        \"carrier\": {\n" +
            "          \"title\": \"Аэрофлот\",\n" +
            "          \"code\": 26,\n" +
            "          \"codes\": {\n" +
            "            \"icao\": null,\n" +
            "            \"sirena\": \"СУ\",\n" +
            "            \"iata\": \"SU\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"transport_type\": \"plane\",\n" +
            "        \"uid\": \"SU-1827A_c26_agent\",\n" +
            "        \"title\": \"Симферополь - Москва\",\n" +
            "        \"vehicle\": \"Airbus А320\",\n" +
            "        \"number\": \"SU 1827\",\n" +
            "        \"short_title\": \"Симферополь - Москва\",\n" +
            "        \"express_type\": null\n" +
            "      },\n" +
            "      \"departure_platform\": null,\n" +
            "      \"stops\": \"\",\n" +
            "      \"to\": {\n" +
            "        \"code\": \"s9600213\",\n" +
            "        \"station_type\": \"аэропорт\",\n" +
            "        \"title\": \"Шереметьево\",\n" +
            "        \"popular_title\": \"\",\n" +
            "        \"short_title\": \"\",\n" +
            "        \"transport_type\": \"plane\",\n" +
            "        \"type\": \"station\"\n" +
            "      },\n" +
            "      \"departure_terminal\": null\n" +
            "    }\n" +
            "  ],\n" +
            "  \"search\": {\n" +
            "    \"date\": \"2015-09-02\",\n" +
            "    \"to\": {\n" +
            "      \"code\": \"c213\",\n" +
            "      \"type\": \"settlement\",\n" +
            "      \"popular_title\": \"Москва\",\n" +
            "      \"short_title\": \"Москва\",\n" +
            "      \"title\": \"Москва\"\n" +
            "    },\n" +
            "    \"from\": {\n" +
            "      \"code\": \"c146\",\n" +
            "      \"type\": \"settlement\",\n" +
            "      \"popular_title\": \"Симферополь\",\n" +
            "      \"short_title\": \"Симферополь\",\n" +
            "      \"title\": \"Симферополь\"\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
