package ru.ifmo.droid2016.vkdemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

public class VkDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        Fresco.initialize(this);
        // TODO: Task 1 - добавить код инициализации Vk SDK
    }
}
