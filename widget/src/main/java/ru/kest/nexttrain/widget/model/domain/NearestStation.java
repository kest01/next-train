package ru.kest.nexttrain.widget.model.domain;

import android.location.Location;

/**
 * Created by KKharitonov on 14.02.2016.
 */
public enum NearestStation {
    HOME,
    WORK;

    public static NearestStation getNearestStation(Location lastLocation) {
        if (getDistanceToHome(lastLocation) <= getDistanceToWork(lastLocation)) {
            return HOME;
        } else {
            return WORK;
        }
    }

    private static final Location homeLocation = new Location("");
    private static final Location workLocation = new Location("");
    static {
        homeLocation.setLatitude(55.8300989);
        homeLocation.setLongitude(37.2187062);

        workLocation.setLatitude(55.802753);
        workLocation.setLongitude(37.491259);
    }

    private static int getDistanceToWork(Location lastLocation) {
        return Math.round(workLocation.distanceTo(lastLocation));
    }

    private static int getDistanceToHome(Location lastLocation) {
        return Math.round(homeLocation.distanceTo(lastLocation));
    }

}
