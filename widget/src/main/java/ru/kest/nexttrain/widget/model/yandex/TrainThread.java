package ru.kest.nexttrain.widget.model.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by KKharitonov on 06.01.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainThread {

    private Date arrival;
    private Date departure;
    private float duration;
    private Station from;
    private Station to;

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public Station getFrom() {
        return from;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public Station getTo() {
        return to;
    }

    public void setTo(Station to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "TrainThread{" +
                "arrival=" + arrival +
                ", departure=" + departure +
                ", duration=" + duration +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
