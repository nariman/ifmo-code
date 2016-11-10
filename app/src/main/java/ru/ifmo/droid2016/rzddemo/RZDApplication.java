package ru.ifmo.droid2016.rzddemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by dmitry.trunin on 08.11.2016.
 */

public class RZDApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
