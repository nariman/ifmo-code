package com.woofilee.ifmo.android.homework.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receiver class for the Image Service broadcasts.
 */
public abstract class ImageServiceReceiver extends BroadcastReceiver {
    public static final String TAG = ImageServiceReceiver.class.getSimpleName();

    public static final String BROADCAST_ACTION =
            "com.woofilee.ifmo.android.homework.service.image";

    public static final String ACTION_TYPE_PARAM = "type";

    public static final int ACTION_TYPE_FINISH_LOADING = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(ACTION_TYPE_PARAM, 0);

        switch (type) {
            case ACTION_TYPE_FINISH_LOADING:
                Log.d(TAG, "Broadcast finish loading received");
                onFinishLoading();
                break;
            default:
                Log.w(TAG, "Unknown broadcast action type received, skipping");
        }
    }

    /**
     * Invokes, when image download complete.
     */
    public abstract void onFinishLoading();
}
