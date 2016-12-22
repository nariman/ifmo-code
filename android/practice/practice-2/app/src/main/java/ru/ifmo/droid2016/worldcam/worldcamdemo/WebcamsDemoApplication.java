package ru.ifmo.droid2016.worldcam.worldcamdemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

/**
 * Created by dmitry.trunin on 03.10.2016.
 */

public class WebcamsDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        Fresco.initialize(this);
    }
}
