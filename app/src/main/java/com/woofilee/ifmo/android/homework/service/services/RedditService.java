package com.woofilee.ifmo.android.homework.service.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.woofilee.ifmo.android.homework.service.R;
import com.woofilee.ifmo.android.homework.service.loaders.ImageDownloader;
import com.woofilee.ifmo.android.homework.service.receivers.RedditServiceReceiver;
import com.woofilee.ifmo.android.homework.service.utils.ImageUtils;

import java.io.FileNotFoundException;

import static android.R.attr.id;

public class RedditService extends Service {
    private static final String TAG = RedditService.class.getSimpleName();

    public static final String IMAGE_FILENAME = "reddit";
    public static final String IMAGE_EXTENSION = "png";

    NotificationManager notificationManager;
    BroadcastReceiver receiver;

    boolean isLoading;

    public RedditService() {
        isLoading = false;

    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Starting service");
        super.onCreate();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Broadcast battery state changed received");

                if (isLoading) {
                    Log.d(TAG, "Something loading already, skipping broadcast");
                    return;
                }

                isLoading = true;

                notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

                final Notification.Builder builder = new Notification.Builder(getApplicationContext());
                builder.setContentTitle("Picture Download")
                        .setContentText("Preparing to download...")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setProgress(100, 0, true);

                notificationManager.notify(0, builder.build());

                builder.setContentText("Download in progress...");
                notificationManager.notify(0, builder.build());

                ImageDownloader.download(
                        "https://i.reddituploads.com/ede98e2d860b43d885732abcf8f74036?fit=max&h=1536&w=1536&s=0dc5faee8bb8042f764e52d4815222dd",
                        new ImageDownloader.OnImageLoaderListener() {
                            @Override
                            public void onComplete(Bitmap result) {
                                try {
                                    ImageUtils.saveImage(
                                            result,
                                            IMAGE_FILENAME,
                                            IMAGE_EXTENSION,
                                            getApplicationContext()
                                    );

                                    Intent intent = new Intent(RedditServiceReceiver.BROADCAST_ACTION);
                                    intent.putExtra(
                                            RedditServiceReceiver.ACTION_TYPE_PARAM,
                                            RedditServiceReceiver.ACTION_TYPE_FINISH_LOADING
                                    );

                                    sendBroadcast(intent);
                                    builder.setContentText("Download complete");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    builder.setContentText("Error downloading image");
                                }

                                builder.setProgress(0, 0, false);
                                notificationManager.notify(id, builder.build());
                                isLoading = false;
                            }

                            @Override
                            public void onError() {
                                isLoading = false;
                                builder.setContentText("Error downloading image");
                                builder.setProgress(0, 0, false);
                                notificationManager.notify(id, builder.build());
                            }

                            @Override
                            public void onProgressChange(int percent) {
                                builder.setProgress(100, percent, false);
                                notificationManager.notify(id, builder.build());
                            }
                        }
                );
            }
        };

        registerReceiver(receiver, new IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Stopping service");
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
