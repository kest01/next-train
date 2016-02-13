package ru.kest.nexttrain.widget.model.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by KKharitonov on 06.01.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ScheduleResponse {

    private List<TrainThread> threads;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrainThread {

        private Date arrival;
        private Date departure;
        private float duration;

        private Thread thread;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Thread {

            private String shortTitle;

        }
    }

}
