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
    private final String TAG = this.getClass().getSimpleName();

    private static final int BUFFER_LENGTH = 8192;

    public static enum Protocol {
        HTTPS,
        HTTP
    }

    public static final String REDDIT_URL = "www.reddit.com";

    public static final Protocol DEFAULT_PROTOCOL = Protocol.HTTPS;
    public static final String DEFAULT_SUBREDDIT = "aww";

    /**
     * Listener with methods to be invoked when links load status changes.
     */
    private final OnLinksLoaderListener listener;

    /**
     * Protocol, that will be used when download.
     */
    private final String protocol;

    /**
     * Subreddit name, from to load a links.
     */
    private final String subreddit;

    /**
     * Indicates, that loading has started.
     */
    private boolean started = false;

    public RedditLinksLoader(OnLinksLoaderListener listener) {
        this(DEFAULT_PROTOCOL, DEFAULT_SUBREDDIT, listener);
    }

    public RedditLinksLoader(String subreddit, OnLinksLoaderListener listener) {
        this(DEFAULT_PROTOCOL, subreddit, listener);
    }

    public RedditLinksLoader(Protocol protocol, OnLinksLoaderListener listener) {
        this(protocol, DEFAULT_SUBREDDIT, listener);
    }

    public RedditLinksLoader(Protocol protocol, String subreddit, OnLinksLoaderListener listener) {
        this.subreddit = subreddit;
        this.listener = listener;

        if (protocol == Protocol.HTTP)
            this.protocol = "http";
        else {
            this.protocol = "https";
        }
    }

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

    public void load() {
        if (started) {
            Log.w(TAG, "Load already running...");
            return;
        }

        started = true;

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
                            protocol + "://" + REDDIT_URL + "/r/" + subreddit + ".json"
                    ).openConnection();

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
                    }

                    if (length != read) {
                        Log.w(TAG, "Received " + read + " bytes, but expected " + length);
                    } else {
                        Log.d(TAG, "Received " + read + " bytes");
                        reader = new JsonReader(
                                new InputStreamReader(new ByteArrayInputStream(out.toByteArray()))
                        );
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
}
