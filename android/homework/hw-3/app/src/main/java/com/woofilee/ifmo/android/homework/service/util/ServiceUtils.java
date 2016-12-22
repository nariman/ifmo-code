package com.woofilee.ifmo.android.homework.service.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Class provides a methods for service management.
 */
public final class ServiceUtils {
    /**
     * Checks, if service is running.
     *
     * @param serviceClass service class
     * @param context      application context
     * @return {@code true}, if service is running, otherwise {@code false}
     */
    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service
                : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
