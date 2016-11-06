package ru.ifmo.droid2016.tmdb.model;

import android.support.annotation.NonNull;

/**
 * Primary Movie Info from The Movie DB API
 */

public class Movie {
    /**
     * Localized movie's title
     */
    public final
    @NonNull
    String title;

    /**
     * Original movie's title
     */
    public final
    @NonNull
    String originalTitle;

    /**
     * Poster Path (2/3 aspect ratio image-poster)
     */
    public final
    @NonNull
    String poster;

    /**
     * Localized movie's description
     */
    public final
    @NonNull
    String overview;

    /**
     * Average movie's vote rating
     */
    public final
    @NonNull
    Double rating;

    public Movie(@NonNull String title,
                 @NonNull String originalTitle,
                 @NonNull String overview,
                 @NonNull String poster,
                 @NonNull Double rating) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.poster = "https://image.tmdb.org/t/p/w500" + poster;
        this.rating = rating;
    }
}
