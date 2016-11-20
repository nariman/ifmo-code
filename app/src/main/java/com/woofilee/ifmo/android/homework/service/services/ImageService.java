package com.woofilee.ifmo.android.homework.service.services;

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

import com.woofilee.ifmo.android.homework.service.R;
import com.woofilee.ifmo.android.homework.service.activity.MainActivity;
import com.woofilee.ifmo.android.homework.service.constants.ImagesURLConstants;
import com.woofilee.ifmo.android.homework.service.loaders.ImageDownloader;
import com.woofilee.ifmo.android.homework.service.receivers.ImageServiceReceiver;
import com.woofilee.ifmo.android.homework.service.utils.ImageUtils;

import java.io.FileNotFoundException;

/**
 * Service class, that downloads images by the battery state change system event.
 */
public final class ImageService extends Service {
    private static final String TAG = ImageService.class.getSimpleName();

    public static final String IMAGE_FILENAME = "reddit";
    public static final String IMAGE_EXTENSION = "png";

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
                Log.d(TAG, "Broadcast battery state changed received");

                if (isLoading) {
                    Log.d(TAG, "Something loading already, skipping broadcast");
                    return;
                }

                notificationCounter++;
                isLoading = true;

                final Notification.Builder notificationBuilder =
                        new Notification.Builder(getApplicationContext());
                final PendingIntent notificationIntent = PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        new Intent(getApplicationContext(), MainActivity.class),
                        0
                );

                notificationBuilder.setContentTitle(getString(R.string.image_download))
                        .setContentIntent(notificationIntent)
                        .setContentText(getString(R.string.preparing_to_download))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setProgress(100, 0, true);

                notificationManager.notify(notificationCounter, notificationBuilder.build());

                // TODO: Reddit links download

                notificationBuilder.setContentText(getString(R.string.download_in_progress));
                notificationManager.notify(notificationCounter, notificationBuilder.build());

                ImageDownloader.download(
                        ImagesURLConstants.getRandomImageURL(),
                        new ImageDownloader.OnImageLoaderListener() {
                            @Override
                            public void onComplete(final Bitmap result) {
                                notificationBuilder.setContentText(getString(R.string.saving));
                                notificationBuilder.setProgress(100, 0, true);
                                notificationManager.notify(notificationCounter,
                                        notificationBuilder.build());

                                new AsyncTask<Void, Void, Void>() { // Async image saving to file
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        try {
                                            ImageUtils.saveImage(result, IMAGE_FILENAME,
                                                    IMAGE_EXTENSION, getApplicationContext());

                                            Intent intent = new Intent(
                                                    ImageServiceReceiver.BROADCAST_ACTION);
                                            intent.putExtra(
                                                    ImageServiceReceiver.ACTION_TYPE_PARAM,
                                                    ImageServiceReceiver.ACTION_TYPE_FINISH_LOADING
                                            );

                                            sendBroadcast(intent);
                                            notificationBuilder.setContentText(getString(
                                                    R.string.download_complete));
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                            notificationBuilder.setContentText(getString(
                                                    R.string.download_error));
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
                                isLoading = false;
                                notificationBuilder.setContentText(getString(R.string.download_error));
                                notificationBuilder.setProgress(0, 0, false);
                                notificationManager.notify(notificationCounter,
                                        notificationBuilder.build());
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
