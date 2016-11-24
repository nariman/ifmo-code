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
    public static final int ACTION_TYPE_DOWNLOAD_STARTED = 2;
    public static final int ACTION_TYPE_DOWNLOAD_COMPLETE = 3;
    public static final int ACTION_TYPE_SERVICE_STOPPED = 4;

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(ACTION_TYPE_PARAM, 0);

        switch (type) {
            case ACTION_TYPE_SERVICE_STARTED:
                Log.d(TAG, "Broadcast \"Service Started\" received");
                onServiceStarted();
                break;
            case ACTION_TYPE_DOWNLOAD_STARTED:
                Log.d(TAG, "Broadcast \"Download Started\" received");
                onDownloadStarted();
                break;
            case ACTION_TYPE_DOWNLOAD_COMPLETE:
                Log.d(TAG, "Broadcast \"Download Complete\" received");
                onDownloadComplete();
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
     * Invokes, when image download started.
     */
    public abstract void onDownloadStarted();

    /**
     * Invokes, when image download complete.
     */
    public abstract void onDownloadComplete();

    /**
     * Invokes, when service stopped.
     */
    public abstract void onServiceStopped();
}
