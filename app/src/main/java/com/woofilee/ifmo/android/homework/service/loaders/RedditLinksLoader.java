package com.woofilee.ifmo.android.homework.service.loaders;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class provides a methods for loading
 * a links from a Reddit's subreddit.
 */
public class RedditLinksLoader {
    private static final String TAG = RedditLinksLoader.class.getSimpleName();

    private static final int BUFFER_LENGTH = 8192;

    public static enum Protocol {
        HTTPS,
        HTTP
    }

    public static final String REDDIT_URL = "www.reddit.com";

    public static final Protocol DEFAULT_PROTOCOL = Protocol.HTTPS;
    public static final String DEFAULT_SUBREDDIT = "aww";

    /**
     * Interface with methods to be invoked when links load status changes.
     */
    public interface OnLinksLoaderListener {
        /**
         * Invoked after the links has been successfully loaded.
         *
         * @param result the downloaded image
         */
        void onComplete(JsonReader result);

        /**
         * Invoked if an error has occurred and the load did not complete.
         */
        void onError();
    }

    /**
     * @param listener listener with methods to be invoked when links load status changes
     */
    public static void load(OnLinksLoaderListener listener) {
        load(DEFAULT_PROTOCOL, DEFAULT_SUBREDDIT, listener);
    }

    /**
     * @param subreddit subreddit name, from to load a links
     * @param listener  listener with methods to be invoked when links load status changes
     */
    public static void load(String subreddit, OnLinksLoaderListener listener) {
        load(DEFAULT_PROTOCOL, subreddit, listener);
    }

    /**
     * @param protocol protocol, that will be used when download
     * @param listener listener with methods to be invoked when links load status changes
     */
    public static void load(Protocol protocol, OnLinksLoaderListener listener) {
        load(protocol, DEFAULT_SUBREDDIT, listener);
    }

    /**
     * @param protocol  protocol, that will be used when download
     * @param subreddit subreddit name, from to load a links
     * @param listener  listener with methods to be invoked when links load status changes
     */
    public static void load(final Protocol protocol, final String subreddit, final OnLinksLoaderListener listener) {
        new AsyncTask<Void, Integer, JsonReader>() {
            @Override
            protected void onPreExecute() {
                Log.d(TAG, "Starting load");
            }

            @Override
            protected void onCancelled() {
                Log.d(TAG, "Cancel load");
            }

            @Override
            protected JsonReader doInBackground(Void... params) {
                JsonReader reader = null;

                HttpURLConnection connection = null;
                BufferedInputStream is = null;
                ByteArrayOutputStream out = null;

                try {
                    connection = (HttpURLConnection) new URL(
                            (protocol == Protocol.HTTP) ? "http" : "https"
                                    + "://"
                                    + REDDIT_URL
                                    + "/r/"
                                    + subreddit
                                    + ".json"
                    ).openConnection();

                    Log.d(TAG, "URL: " + connection.getURL().toString());

                    connection.connect();

                    final int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new FileNotFoundException("Unexpected HTTP response: " + responseCode
                                + ", " + connection.getResponseMessage());
                    }

                    is = new BufferedInputStream(connection.getInputStream(), BUFFER_LENGTH);
                    out = new ByteArrayOutputStream();

                    byte bytes[] = new byte[BUFFER_LENGTH];
                    int count;
                    long read = 0;

                    while ((count = is.read(bytes)) != -1) {
                        read += count;
                        out.write(bytes, 0, count);
                    }

                    Log.d(TAG, "Received " + read + " bytes");

                    reader = new JsonReader(
                            new InputStreamReader(new ByteArrayInputStream(out.toByteArray()))
                    );
                } catch (Throwable e) {
                    Log.e(TAG, e.getMessage());

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

                return reader;
            }

            @Override
            protected void onPostExecute(JsonReader result) {
                if (result == null) {
                    Log.e(TAG, "Error while loading a links");
                    listener.onError();
                } else {
                    listener.onComplete(result);
                }

//                System.gc();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private RedditLinksLoader() {
    }
}
