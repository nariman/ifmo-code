package com.woofilee.ifmo.android.homework.service.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Class provides a methods for service management.
 */
public final class ServiceUtils {
    /**
     * Checks, if service is running already.
     *
     * @param serviceClass service class
     * @param context      application context
     * @return             true, if service is running, otherwise false
     */
    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
