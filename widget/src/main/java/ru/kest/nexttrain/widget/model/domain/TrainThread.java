package ru.kest.nexttrain.widget.model.domain;

import java.util.Date;

/**
 * Created by KKharitonov on 07.01.2016.
 */
public class TrainThread {

    private Date arrival;
    private Date departure;
    private String fromCode;
    private String toCode;

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

    public String getFromCode() {
        return fromCode;
    }

    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

    public String getToCode() {
        return toCode;
    }

    public void setToCode(String toCode) {
        this.toCode = toCode;
    }
}
