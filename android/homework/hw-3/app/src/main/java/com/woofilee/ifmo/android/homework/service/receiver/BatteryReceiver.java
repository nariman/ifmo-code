package com.woofilee.ifmo.android.homework.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.woofilee.ifmo.android.homework.service.service.ImageService;

/**
 * Receiver class for the Battery state change broadcasts (offline).
 */
public class BatteryReceiver extends BroadcastReceiver {
    public static final String TAG = BatteryReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast battery state changed received");
        context.startService(new Intent(context, ImageService.class).setAction(
                ImageService.ACTION_DOWNLOAD));
    }
}
