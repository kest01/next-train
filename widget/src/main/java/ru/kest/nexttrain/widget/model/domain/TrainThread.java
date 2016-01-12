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
    private String fromName;
    private String toName;
    private String title;

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

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TrainThread{" +
                "arrival=" + arrival +
                ", departure=" + departure +
                ", fromCode='" + fromCode + '\'' +
                ", toCode='" + toCode + '\'' +
                ", fromName='" + fromName + '\'' +
                ", toName='" + toName + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
