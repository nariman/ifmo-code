package com.woofilee.ifmo.android.homework.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class RedditServiceReceiver extends BroadcastReceiver {
    public static final String TAG = RedditServiceReceiver.class.getSimpleName();

    public static final String BROADCAST_ACTION =
            "com.woofilee.ifmo.android.homework.service.reddit";

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

    public abstract void onFinishLoading();
}
