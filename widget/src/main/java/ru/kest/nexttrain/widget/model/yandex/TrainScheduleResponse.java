package ru.kest.nexttrain.widget.model.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

/**
 * Created by KKharitonov on 06.01.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainScheduleResponse {

    private Pagination pagination;

    private List<TrainThread> threads;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<TrainThread> getThreads() {
        return threads;
    }

    public void setThreads(List<TrainThread> threads) {
        this.threads = threads;
    }

    @Override
    public String toString() {
        return "TrainScheduleResponse{" +
                "pagination=" + pagination +
                ", threads=" + (threads == null ? "null" : Arrays.toString(threads.toArray())) +
                '}';
    }
}
