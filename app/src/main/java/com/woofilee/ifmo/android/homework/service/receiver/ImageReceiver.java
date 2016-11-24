package com.woofilee.ifmo.android.homework.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receiver class for the Image Service broadcasts.
 */
public abstract class ImageReceiver extends BroadcastReceiver {
    public static final String TAG = ImageReceiver.class.getSimpleName();

    public static final String BROADCAST_ACTION =
            "com.woofilee.ifmo.android.homework.service.update";

    public static final String ACTION_TYPE_PARAM = "type";

    public static final int ACTION_TYPE_SERVICE_STARTED = 1;
    public static final int ACTION_TYPE_DOWNLOADING_FINISHED = 2;
    public static final int ACTION_TYPE_SERVICE_STOPPED = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(ACTION_TYPE_PARAM, 0);

        switch (type) {
            case ACTION_TYPE_SERVICE_STARTED:
                Log.d(TAG, "Broadcast \"Service Started\" received");
                onServiceStarted();
                break;
            case ACTION_TYPE_DOWNLOADING_FINISHED:
                Log.d(TAG, "Broadcast \"Finish Loading\" received");
                onFinishLoading();
                break;
            case ACTION_TYPE_SERVICE_STOPPED:
                Log.d(TAG, "Broadcast \"Service Stopped\" received");
                onServiceStopped();
                break;
            default:
                Log.w(TAG, "Unknown broadcast action type received, skipping");
        }
    }

    /**
     * Invokes, when service started.
     */
    public abstract void onServiceStarted();

    /**
     * Invokes, when image download complete.
     */
    public abstract void onFinishLoading();

    /**
     * Invokes, when service stopped.
     */
    public abstract void onServiceStopped();
}
