package ru.kest.nexttrain.widget.model.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by KKharitonov on 07.01.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Thread {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Thread{" +
                "title='" + title + '\'' +
                '}';
    }
}
