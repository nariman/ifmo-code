package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The Movie DB API methods
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    // TODO: Зарегистрироваться на https://www.themoviedb.org и получить свой собственный ключ
    private static final String API_KEY = "ee1c42c80c58d28bc54efc844b63d114";
    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");

    private TmdbApi() {}

    /**
     * Returns {@link android.net.Uri.Builder} with default request values
     */
    private static Uri.Builder getUriBuilder() {
        return BASE_URI
                .buildUpon()
                .appendQueryParameter("api_key", API_KEY);
    }

    /**
     * Returns {@link HttpURLConnection} for perform a "Popular Movies" request
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param page page to be loaded
     * @param lang language, in which to return the titles and descriptions of the movies
     */
    public static HttpURLConnection getPopularMovies(int page, String lang) throws IOException {
        Uri uri = getUriBuilder()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("language", lang)
                .build();

        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
