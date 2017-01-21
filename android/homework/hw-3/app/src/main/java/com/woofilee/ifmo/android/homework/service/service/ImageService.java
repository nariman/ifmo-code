package com.woofilee.ifmo.android.homework.service.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.woofilee.ifmo.android.homework.service.activity.MainActivity;
import com.woofilee.ifmo.android.homework.service.constant.ImagesURLConstants;
import com.woofilee.ifmo.android.homework.service.loader.AsyncLoader;
import com.woofilee.ifmo.android.homework.service.receiver.ImageReceiver;
import com.woofilee.ifmo.android.homework.service.util.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import static com.woofilee.ifmo.android.homework.service.R.mipmap.ic_launcher;

import static com.woofilee.ifmo.android.homework.service.R.string.image_downloader_service;
import static com.woofilee.ifmo.android.homework.service.R.string.preparing_to_download;
import static com.woofilee.ifmo.android.homework.service.R.string.download_in_progress;
import static com.woofilee.ifmo.android.homework.service.R.string.saving;
import static com.woofilee.ifmo.android.homework.service.R.string.saving_error;
import static com.woofilee.ifmo.android.homework.service.R.string.download_complete;
import static com.woofilee.ifmo.android.homework.service.R.string.download_error;

/**
 * Service class, that downloads images by the battery state change system event.
 */
public final class ImageService extends Service {
    private static final String TAG = ImageService.class.getSimpleName();

    public static final String IMAGE_FILEPATH = "reddit.png";

    public static final String ACTION_DOWNLOAD =
            "com.woofilee.ifmo.android.homework.service.download";

    private NotificationManager notificationManager;
    private BroadcastReceiver receiver;

    int notificationCounter;
    boolean isLoading;

    public ImageService() {
        notificationCounter = 0;
        isLoading = false;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Starting service");
        super.onCreate();

        sendBroadcast(new Intent(ImageReceiver.BROADCAST_ACTION).putExtra(
                ImageReceiver.ACTION_TYPE_PARAM,
                ImageReceiver.ACTION_TYPE_SERVICE_STARTED
        ));

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onDownload();
            }
        };
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null &&
                intent.getAction() != null &&
                intent.getAction().equals(ACTION_DOWNLOAD)) {
            onDownload();
        }

        return START_STICKY; // We want to live, to listen all changes of battery status, not only static
    }

    public void onDownload() {
        if (isLoading) {
            Log.d(TAG, "Something is downloading already, skipping");
            return;
        }

        isLoading = true;
        notificationCounter++;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final Notification.Builder notificationBuilder =
                        new Notification.Builder(getApplicationContext())
                                .setContentTitle(getString(image_downloader_service))
                                .setSmallIcon(ic_launcher)
                                .setContentIntent(
                                        PendingIntent.getActivity(
                                                getApplicationContext(),
                                                0,
                                                new Intent(
                                                        getApplicationContext(),
                                                        MainActivity.class
                                                ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                                                0
                                        )
                                );

                sendBroadcast(new Intent(
                        ImageReceiver.BROADCAST_ACTION).putExtra(
                        ImageReceiver.ACTION_TYPE_PARAM,
                        ImageReceiver.ACTION_TYPE_DOWNLOAD_STARTED
                ));

                notificationManager.notify(
                        notificationCounter,
                        notificationBuilder
                                .setContentText(getString(preparing_to_download))
                                .setProgress(100, 0, true)
                                .build()
                );

                // TODO: IDEA: Reddit links load

                notificationManager.notify(
                        notificationCounter,
                        notificationBuilder
                                .setContentText(getString(download_in_progress))
                                .build()
                );

                AsyncLoader.load(
                        ImagesURLConstants.getRandomImageURL(),
                        new AsyncLoader.OnAsyncLoaderListener() {
                            @Override
                            public void onComplete(final ByteArrayOutputStream result) {
                                notificationManager.notify(
                                        notificationCounter,
                                        notificationBuilder
                                                .setContentText(getString(saving))
                                                .setProgress(100, 0, true)
                                                .build()
                                );

                                // Async image saving to the file
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        try {
                                            FileOutputStream file = FileUtils.getOutputFile(
                                                    IMAGE_FILEPATH,
                                                    getApplicationContext()
                                            );

                                            result.writeTo(file);
                                            result.close();

                                            sendBroadcast(new Intent(
                                                    ImageReceiver.BROADCAST_ACTION).putExtra(
                                                    ImageReceiver.ACTION_TYPE_PARAM,
                                                    ImageReceiver.ACTION_TYPE_DOWNLOAD_COMPLETE
                                            ));

                                            notificationBuilder.setContentText(
                                                    getString(download_complete));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            notificationBuilder.setContentText(
                                                    getString(saving_error));
                                        }

                                        notificationManager.notify(
                                                notificationCounter,
                                                notificationBuilder
                                                        .setProgress(0, 0, false)
                                                        .build()
                                        );

                                        isLoading = false;
                                        return null;
                                    }
                                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }

                            @Override
                            public void onProgressChange(int percent) {
                                notificationManager.notify(
                                        notificationCounter,
                                        notificationBuilder
                                                .setProgress(100, percent, false)
                                                .build()
                                );
                            }

                            @Override
                            public void onError() {
                                notificationManager.notify(
                                        notificationCounter,
                                        notificationBuilder
                                                .setContentText(getString(download_error))
                                                .setProgress(0, 0, false)
                                                .build()
                                );

                                isLoading = false;
                            }
                        }
                );

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Stopping service");
        super.onDestroy();

        unregisterReceiver(receiver);
        sendBroadcast(new Intent(ImageReceiver.BROADCAST_ACTION).putExtra(
                ImageReceiver.ACTION_TYPE_PARAM,
                ImageReceiver.ACTION_TYPE_SERVICE_STOPPED
        ));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
