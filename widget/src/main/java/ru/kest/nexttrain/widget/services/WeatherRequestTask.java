package ru.kest.nexttrain.widget.services;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.kest.nexttrain.widget.util.SchedulerUtil;

import static ru.kest.nexttrain.widget.TrainsWidget.LOG_TAG;

/**
 * Created by KKharitonov on 05.01.2016.
 */
public class WeatherRequestTask extends AsyncTask<Location, Void, String> {

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&APPID=6b984753e53b6ca0b41f667771fa3927";

    private Context context;

    public WeatherRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Location... params) {

        try {
            RestTemplate restTemplate = new RestTemplate();
//            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String response = restTemplate.getForObject(URL, String.class, params[0].getLatitude(), params[0].getLongitude());
            Log.i(LOG_TAG, "Response: " + response);
            return response;
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
}
