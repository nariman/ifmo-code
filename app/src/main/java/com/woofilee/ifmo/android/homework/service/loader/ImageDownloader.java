package com.woofilee.ifmo.android.homework.service.loader;

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
 * Class provides a methods for downloading an image by URL.
 */
public final class ImageDownloader {
    private static final String TAG = ImageDownloader.class.getSimpleName();

    private static final int BUFFER_LENGTH = 8192;

    private static final int CONNECT_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 60;

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
         * Invoked every time the progress of the download changes.
         *
         * @param percent new status in %
         */
        void onProgressChange(int percent);

        /**
         * Invoked if an error has occurred and the download did not complete.
         */
        void onError();
    }

    /**
     * Loads an image by URL.
     *
     * @param url      url, where image to download is located
     * @param listener listener with methods to be invoked when image download status changes
     */
    public static void download(final String url, final OnImageLoaderListener listener) {
        new AsyncTask<Void, Integer, Bitmap>() {
            @Override
            protected void onPreExecute() {
                Log.d(TAG, "Starting download image");
            }

            @Override
            protected void onCancelled() {
                Log.d(TAG, "Cancel download image");
                listener.onError();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                listener.onProgressChange(values[0]);
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = null;

                HttpURLConnection conn = null;
                BufferedInputStream is = null;
                ByteArrayOutputStream os = null;

                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();

                    Log.d(TAG, "URL: " + conn.getURL().toString());
                    conn.setConnectTimeout(CONNECT_TIMEOUT * 1000);
                    conn.setReadTimeout(READ_TIMEOUT * 1000);

                    conn.connect();

                    final int responseCode = conn.getResponseCode();
                    final int length = conn.getContentLength();

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new FileNotFoundException("Unexpected HTTP response: " + responseCode
                                + ", " + conn.getResponseMessage());
                    }

                    if (length <= 0) {
                        throw new FileNotFoundException("Invalid content length: " + length);
                    }

                    is = new BufferedInputStream(conn.getInputStream(), BUFFER_LENGTH);
                    os = new ByteArrayOutputStream();

                    byte bytes[] = new byte[BUFFER_LENGTH];
                    int count;
                    long read = 0;

                    while ((count = is.read(bytes)) != -1) {
                        read += count;
                        os.write(bytes, 0, count);
                        publishProgress((int) ((read * 100) / length));
                    }

                    if (length == read) {
                        Log.d(TAG, "Received " + read + " bytes");
                        bitmap = BitmapFactory.decodeByteArray(os.toByteArray(), 0, os.size());
                    } else {
                        Log.w(TAG, "Received " + read + " bytes, but expected " + length);
                    }
                } catch (Exception e) {
                    if (!this.isCancelled()) {
                        this.cancel(true);
                    }
                } finally {
                    try {
                        if (conn != null) {
                            conn.disconnect();
                        }

                        if (is != null) {
                            is.close();
                        }

                        if (os != null) {
                            os.flush();
                            os.close();
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
                    Log.e(TAG, "Error while download an image");
                    listener.onError();
                } else {
                    listener.onComplete(result);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private ImageDownloader() {
    }
}
