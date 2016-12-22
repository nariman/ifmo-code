package com.woofilee.ifmo.android.homework.service.constant;

import java.util.Random;

/**
 * Class with list of available images for download.
 */
public final class ImagesURLConstants {
    private static final Random random = new Random();

    public static final String[] images = new String[]{
            "https://pbs.twimg.com/media/CvZwD7yWcAEMIek.jpg:large",
            "https://i.redd.it/63ohudc4s86x.jpg",
            "http://i.imgur.com/e3NmIZv.jpg",
            "http://i.imgur.com/a2bFjKz.jpg",
            "http://i.imgur.com/T3UVqzz.jpg"
    };

    public static String getRandomImageURL() {
        return images[random.nextInt(images.length)];
    }

    private ImagesURLConstants() {
    }
}
