package ru.ifmo.droid2016.tmdb.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private static final String TAG = "LOADER";

    private int page;
    private String locale;

    private LoadResult<List<Movie>> result;

    public PopularMoviesLoader(Context context) {
        this(context, 1);
    }

    public PopularMoviesLoader(Context context, int page) {
        super(context);

        this.page = page;
        this.locale = Locale.getDefault().getLanguage();
        this.result = null;
    }

    @Override
    protected void onStartLoading() {
        if (result != null && result.resultType == ResultType.OK) {
            deliverResult(result);
        } else {
            forceLoad();
        }
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;
        HttpURLConnection connection = null;

        try {
            connection = TmdbApi.getPopularMovies(page, locale);
            connection.setConnectTimeout(15000); // 15 sec
            connection.setReadTimeout(15000); // 15 sec

            Log.d(TAG, "Performing request: " + connection.getURL());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                resultType = ResultType.OK;
                data = Parser.parse(
                        stethoManager.interpretResponseStream(connection.getInputStream()));
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);

            if (!IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.NO_INTERNET;
                Log.w(TAG, "Failed to get popular movies: internet connection is not available", e);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get popular movies: unexpected error", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        this.result = new LoadResult<>(resultType, data);
        return result;
    }

    private static class Parser {
        private Parser() {
        }

        private static JSONObject parseJSON(InputStream in) throws IOException, JSONException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            in = new BufferedInputStream(in);

            byte[] buffer = new byte[8192];
            int readSize;

            while ((readSize = in.read(buffer)) >= 0) {
                baos.write(buffer, 0, readSize);
            }

            in.close();

            String content = baos.toString("UTF-8");
            baos.close();

            return new JSONObject(content);
        }

        @NonNull
        static List<Movie> parse(InputStream in) throws IOException, JSONException {
            JSONArray res = parseJSON(in).getJSONArray("results");
            List<Movie> movies = new ArrayList<>();

            for (int i = 0; i < res.length(); i++) {
                JSONObject movie = res.getJSONObject(i);
                movies.add(new Movie(
                        movie.getString("title"),
                        movie.getString("original_title"),
                        movie.getString("overview"),
                        movie.getString("poster_path"),
                        movie.getDouble("vote_average")
                ));
            }

            return movies;
        }
    }
}
