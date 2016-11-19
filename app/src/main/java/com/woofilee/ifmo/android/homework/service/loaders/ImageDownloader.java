package com.woofilee.ifmo.android.homework.service.loaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class provides a methods for downloading
 * an image by URL.
 */
public class ImageDownloader {
    private final String TAG = this.getClass().getSimpleName();

    private static final int BUFFER_LENGTH = 8192;

    /**
     * Listener with methods to be invoked when image download status changes.
     */
    private final OnImageLoaderListener listener;

    /**
     * Indicates, that loading has started.
     */
    private boolean started = false;

    public ImageDownloader(OnImageLoaderListener listener) {
        this.listener = listener;
    }

    /**
     * Interface with methods to be invoked when image download status changes.
     */
    public interface OnImageLoaderListener {
        /**
         * Invoked after the image has been successfully downloaded.
         *
         * @param result the downloaded image
         */
        void onComplete(Bitmap result);

        /**
         * Invoked if an error has occurred and the download did not complete.
         */
        void onError();

        /**
         * Invoked every time the progress of the download changes.
         *
         * @param percent new status in %
         */
        void onProgressChange(int percent);
    }

    public void download(final String url) {
        if (started) {
            Log.w(TAG, "Download already running...");
            return;
        }

        started = true;

        new AsyncTask<Void, Integer, Bitmap>() {
            @Override
            protected void onPreExecute() {
                Log.d(TAG, "Starting download");
            }

            @Override
            protected void onCancelled() {
                Log.d(TAG, "Cancel download");
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                listener.onProgressChange(values[0]);
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = null;

                HttpURLConnection connection = null;
                BufferedInputStream is = null;
                ByteArrayOutputStream out = null;

                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();

                    connection.connect();

                    final int responseCode = connection.getResponseCode();
                    final int length = connection.getContentLength();

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new FileNotFoundException("Unexpected HTTP response: " + responseCode
                                + ", " + connection.getResponseMessage());
                    }

                    if (length <= 0) {
                        throw new FileNotFoundException("Invalid content length: " + length);
                    }

                    is = new BufferedInputStream(connection.getInputStream(), BUFFER_LENGTH);
                    out = new ByteArrayOutputStream();

                    byte bytes[] = new byte[BUFFER_LENGTH];
                    int count;
                    long read = 0;

                    while ((count = is.read(bytes)) != -1) {
                        read += count;
                        out.write(bytes, 0, count);
                        publishProgress((int) ((read * 100) / length));
                    }

                    if (length != read) {
                        Log.w(TAG, "Received " + read + " bytes, but expected " + length);
                    } else {
                        Log.d(TAG, "Received " + read + " bytes");
                        bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
                    }
                } catch (Throwable e) {
                    if (!this.isCancelled()) {
                        this.cancel(true);
                    }
                } finally {
                    try {
                        if (connection != null) {
                            connection.disconnect();
                        }

                        if (is != null) {
                            is.close();
                        }

                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (result == null) {
                    Log.e(TAG, "Error while downloading an image");
                    listener.onError();
                } else {
                    listener.onComplete(result);
                }

//                System.gc();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
