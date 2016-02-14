package ru.kest.nexttrain.widget.services;

import android.content.Context;

/**
 * Created by KKharitonov on 14.02.2016.
 */
public class DataService {

    private static DataProvider dataProvider = null;


    public static DataProvider getDataProvider(Context context) {
        if (dataProvider == null) {
            dataProvider = new DataProviderImpl(context);
        }
        return dataProvider;
    }
}
