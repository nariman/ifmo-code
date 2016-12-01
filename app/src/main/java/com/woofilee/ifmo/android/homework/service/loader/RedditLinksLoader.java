package com.woofilee.ifmo.android.homework.service.loader;

import com.woofilee.ifmo.android.homework.service.loader.AsyncLoader.OnAsyncLoaderListener;

/**
 * Class provides a methods for loading a links from a Reddit's subreddit.
 */
public final class RedditLinksLoader {
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
     * Loads a links from a Reddit's subreddit.
     *
     * @param listener listener with methods to be invoked when links load status changes
     */
    public static void load(OnAsyncLoaderListener listener) {
        load(DEFAULT_PROTOCOL, DEFAULT_SUBREDDIT, listener);
    }

    /**
     * Loads a links from a Reddit's subreddit.
     *
     * @param subreddit subreddit name, from to load a links
     * @param listener  listener with methods to be invoked when links load status changes
     */
    public static void load(String subreddit, OnAsyncLoaderListener listener) {
        load(DEFAULT_PROTOCOL, subreddit, listener);
    }

    /**
     * Loads a links from a Reddit's subreddit.
     *
     * @param protocol protocol, that will be used when load
     * @param listener listener with methods to be invoked when links load status changes
     */
    public static void load(Protocol protocol, OnAsyncLoaderListener listener) {
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
                            final OnAsyncLoaderListener listener) {
        AsyncLoader.load(
                (protocol == Protocol.HTTP) ? "http" : "https"
                        + "://"
                        + REDDIT_URL
                        + "/r/"
                        + subreddit
                        + ".json",
                listener,
                BUFFER_LENGTH,
                CONNECT_TIMEOUT,
                READ_TIMEOUT
        );
    }

    private RedditLinksLoader() {
    }
}
