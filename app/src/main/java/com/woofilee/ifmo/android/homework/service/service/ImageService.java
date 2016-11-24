package com.woofilee.ifmo.android.homework.service.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.woofilee.ifmo.android.homework.service.activity.MainActivity;
import com.woofilee.ifmo.android.homework.service.constant.ImagesURLConstants;
import com.woofilee.ifmo.android.homework.service.loader.ImageDownloader;
import com.woofilee.ifmo.android.homework.service.receiver.ImageReceiver;
import com.woofilee.ifmo.android.homework.service.util.ImageUtils;

import static com.woofilee.ifmo.android.homework.service.R.mipmap.ic_launcher;

import static com.woofilee.ifmo.android.homework.service.R.string.image_download;
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

    NotificationManager notificationManager;
    BroadcastReceiver receiver;

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

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getAction() != null &&
                        intent.getAction().equals(android.content.Intent.ACTION_BATTERY_CHANGED)) {
                    onDownload();
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED));
        sendBroadcast(new Intent(ImageReceiver.BROADCAST_ACTION).putExtra(
                ImageReceiver.ACTION_TYPE_PARAM,
                ImageReceiver.ACTION_TYPE_SERVICE_STARTED
        ));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null &&
                intent.getAction() != null &&
                intent.getAction().equals(ACTION_DOWNLOAD)) {
            onDownload();
        }

        return START_STICKY; // We want to live, we want to listen all changes of battery status, not only static
    }

    public void onDownload() {
        if (isLoading) {
            Log.d(TAG, "Something is downloading already, skipping");
            return;
        }

        notificationCounter++;
        isLoading = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Notification.Builder notificationBuilder =
                        new Notification.Builder(getApplicationContext());
                final PendingIntent notificationIntent = PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        new Intent(getApplicationContext(), MainActivity.class),
                        0
                );

                notificationBuilder.setContentTitle(getString(image_download))
                        .setContentIntent(notificationIntent)
                        .setContentText(getString(preparing_to_download))
                        .setSmallIcon(ic_launcher)
                        .setProgress(100, 0, true);

                notificationManager.notify(notificationCounter, notificationBuilder.build());

                // TODO: IDEA: Reddit links load

                notificationBuilder.setContentText(getString(download_in_progress));
                notificationManager.notify(notificationCounter, notificationBuilder.build());

                ImageDownloader.download(
                        ImagesURLConstants.getRandomImageURL(),
                        new ImageDownloader.OnImageLoaderListener() {
                            @Override
                            public void onComplete(final Bitmap result) {
                                notificationBuilder.setContentText(getString(saving));
                                notificationBuilder.setProgress(100, 0, true);
                                notificationManager.notify(notificationCounter,
                                        notificationBuilder.build());

                                new AsyncTask<Void, Void, Void>() { // Async image saving to the file
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        try {
                                            if (!ImageUtils.writeImage(
                                                    result,
                                                    IMAGE_FILEPATH,
                                                    getApplicationContext()
                                            )) {
                                                throw new IllegalStateException(
                                                        "Image saving error!");
                                            }

                                            sendBroadcast(new Intent(
                                                    ImageReceiver.BROADCAST_ACTION).putExtra(
                                                    ImageReceiver.ACTION_TYPE_PARAM,
                                                    ImageReceiver.ACTION_TYPE_DOWNLOADING_FINISHED
                                            ));
                                            notificationBuilder.setContentText(getString(
                                                    download_complete));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            notificationBuilder.setContentText(getString(
                                                    saving_error));
                                        }

                                        notificationBuilder.setProgress(0, 0, false);
                                        notificationManager.notify(notificationCounter,
                                                notificationBuilder.build());
                                        isLoading = false;

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        System.gc();
                                    }
                                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }

                            @Override
                            public void onError() {
                                notificationBuilder.setContentText(getString(download_error));
                                notificationBuilder.setProgress(0, 0, false);
                                notificationManager.notify(notificationCounter,
                                        notificationBuilder.build());
                                isLoading = false;
                            }

                            @Override
                            public void onProgressChange(int percent) {
                                notificationBuilder.setProgress(100, percent, false);
                                notificationManager.notify(notificationCounter,
                                        notificationBuilder.build());
                            }
                        }
                );
            }
        }).run();
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
