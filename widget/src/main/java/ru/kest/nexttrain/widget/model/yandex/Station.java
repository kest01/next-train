package ru.kest.nexttrain.widget.model.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by KKharitonov on 06.01.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Station{" +
                "code='" + code + '\'' +
                '}';
    }
}
