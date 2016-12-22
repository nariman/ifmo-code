package ru.ifmo.droid2016.worldcam.worldcamdemo.model;

import android.support.annotation.NonNull;

/**
 * Информация об обной вебкамере
 */
public class Webcam {

    /**
     * ID камеры для использования в Webcams API.
     */
    @NonNull
    public final String id;

    /**
     * Заголовок с описанием камеры.
     */
    @NonNull
    public final String title;

    /**
     * URL статической картинки с камеры.
     */
    @NonNull
    public final String imageUrl;

    public Webcam(@NonNull String id,
                  @NonNull String title,
                  @NonNull String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Webcam(id=" + id
                + ", title=\"" + title
                + "\", imageUrl=\"" + imageUrl
                + "\")";
    }
}
