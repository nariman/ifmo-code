package com.woofilee.ifmo.android.homework.service.loader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class provides a methods for async loading resources by URL.
 */
public class AsyncLoader {
    private static final String TAG = AsyncLoader.class.getSimpleName();

    private static final int BUFFER_LENGTH = 8192;

    private static final int CONNECT_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 60;

    /**
     * Interface with methods to be invoked when loading status changes.
     */
    public interface OnAsyncLoaderListener {
        /**
         * Invoked after the loading has been successfully downloaded.
         *
         * @param result the downloaded resource in bytes
         */
        void onComplete(ByteArrayOutputStream result);

        /**
         * Invoked every time the progress of the loading changes.
         *
         * @param percent new status in %
         */
        void onProgressChange(int percent);

        /**
         * Invoked if an error has occurred and the loading did not complete.
         */
        void onError();
    }

    /**
     * Loads a resource by URL.
     *
     * @param url            url, where resource to load is located
     * @param listener       listener with methods to be invoked when loading status changes
     */
    public static void load(final String url, final OnAsyncLoaderListener listener) {
        load(url, listener, BUFFER_LENGTH, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    /**
     * Loads a resource by URL.
     *
     * @param url            url, where resource to load is located
     * @param listener       listener with methods to be invoked when loading status changes
     * @param bufferLength   size of the buffer while fetching resource data
     * @param connectTimeout connection timeout
     * @param readTimeout    reading timeout
     */
    public static void load(final String url,
                            final OnAsyncLoaderListener listener,
                            final int bufferLength,
                            final int connectTimeout,
                            final int readTimeout) {
        new AsyncTask<Void, Integer, ByteArrayOutputStream>() {
            @Override
            protected void onPreExecute() {
                Log.d(TAG, "Start loading");
            }

            @Override
            protected void onCancelled() {
                Log.d(TAG, "Cancel loading");
                listener.onError();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                listener.onProgressChange(values[0]);
            }

            @Override
            protected ByteArrayOutputStream doInBackground(Void... params) {
                HttpURLConnection conn = null;
                BufferedInputStream is = null;
                ByteArrayOutputStream os = null;

                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();

                    Log.d(TAG, "URL: " + conn.getURL().toString());
                    conn.setConnectTimeout(connectTimeout * 1000);
                    conn.setReadTimeout(readTimeout * 1000);

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

                    is = new BufferedInputStream(conn.getInputStream(), bufferLength);
                    os = new ByteArrayOutputStream();

                    byte bytes[] = new byte[bufferLength];
                    int count;
                    long read = 0;

                    while ((count = is.read(bytes)) != -1) {
                        read += count;
                        os.write(bytes, 0, count);
                        publishProgress((int) ((read * 100) / length));
                    }

                    if (length == read) {
                        Log.d(TAG, "Received " + read + " bytes");
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

                return os;
            }

            @Override
            protected void onPostExecute(ByteArrayOutputStream result) {
                if (result == null) {
                    Log.e(TAG, "Error while loading a resource");
                    listener.onError();
                } else {
                    listener.onComplete(result);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
