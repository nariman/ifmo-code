package com.woofilee.ifmo.android.homework.service.loader;

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
 * Class provides a methods for loading a links from a Reddit's subreddit.
 */
public final class RedditLinksLoader {
    private static final String TAG = RedditLinksLoader.class.getSimpleName();

    private static final int BUFFER_LENGTH = 8192;

    private static final int CONNECT_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 60;

    public enum Protocol {
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
         * Invokes after the links has been successfully loaded.
         *
         * @param result the downloaded image
         */
        void onComplete(JsonReader result);

        /**
         * Invokes if an error has occurred and the load did not complete.
         */
        void onError();
    }

    /**
     * Loads a links from a Reddit's subreddit.
     *
     * @param listener listener with methods to be invoked when links load status changes
     */
    public static void load(OnLinksLoaderListener listener) {
        load(DEFAULT_PROTOCOL, DEFAULT_SUBREDDIT, listener);
    }

    /**
     * Loads a links from a Reddit's subreddit.
     *
     * @param subreddit subreddit name, from to load a links
     * @param listener  listener with methods to be invoked when links load status changes
     */
    public static void load(String subreddit, OnLinksLoaderListener listener) {
        load(DEFAULT_PROTOCOL, subreddit, listener);
    }

    /**
     * Loads a links from a Reddit's subreddit.
     *
     * @param protocol protocol, that will be used when load
     * @param listener listener with methods to be invoked when links load status changes
     */
    public static void load(Protocol protocol, OnLinksLoaderListener listener) {
        load(protocol, DEFAULT_SUBREDDIT, listener);
    }

    /**
     * Loads a links from a Reddit's subreddit.
     *
     * @param protocol  protocol, that will be used when load
     * @param subreddit subreddit name, from to load a links
     * @param listener  listener with methods to be invoked when links load status changes
     */
    public static void load(final Protocol protocol,
                            final String subreddit,
                            final OnLinksLoaderListener listener) {
        new AsyncTask<Void, Integer, JsonReader>() {
            @Override
            protected void onPreExecute() {
                Log.d(TAG, "Starting load");
            }

            @Override
            protected void onCancelled() {
                Log.d(TAG, "Cancel load");
                listener.onError();
            }

            @Override
            protected JsonReader doInBackground(Void... params) {
                JsonReader reader = null;

                HttpURLConnection conn = null;
                BufferedInputStream is = null;
                ByteArrayOutputStream os = null;

                try {
                    conn = (HttpURLConnection) new URL(
                            (protocol == Protocol.HTTP) ? "http" : "https"
                                    + "://"
                                    + REDDIT_URL
                                    + "/r/"
                                    + subreddit
                                    + ".json"
                    ).openConnection();

                    Log.d(TAG, "URL: " + conn.getURL().toString());

                    conn.setConnectTimeout(CONNECT_TIMEOUT * 1000);
                    conn.setReadTimeout(READ_TIMEOUT * 1000);
                    conn.connect();

                    final int responseCode = conn.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new FileNotFoundException("Unexpected HTTP response: " + responseCode
                                + ", " + conn.getResponseMessage());
                    }

                    is = new BufferedInputStream(conn.getInputStream(), BUFFER_LENGTH);
                    os = new ByteArrayOutputStream();

                    byte bytes[] = new byte[BUFFER_LENGTH];
                    int count;
                    long read = 0;

                    while ((count = is.read(bytes)) != -1) {
                        read += count;
                        os.write(bytes, 0, count);
                    }

                    Log.d(TAG, "Received " + read + " bytes");

                    reader = new JsonReader(
                            new InputStreamReader(new ByteArrayInputStream(os.toByteArray()))
                    );
                } catch (Throwable e) {
                    Log.e(TAG, e.getMessage());

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

                System.gc(); // Collect it all! :)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private RedditLinksLoader() {
    }
}
