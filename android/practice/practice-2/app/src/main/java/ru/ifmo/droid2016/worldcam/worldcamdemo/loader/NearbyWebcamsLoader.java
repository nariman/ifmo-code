package ru.ifmo.droid2016.worldcam.worldcamdemo.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import ru.ifmo.droid2016.worldcam.worldcamdemo.api.WebcamsApi;
import ru.ifmo.droid2016.worldcam.worldcamdemo.model.Webcam;

/**
 * Загрузчик списка камер в некотором радиусе от указанных координат.
 *
 * Выполняет запрос Webcams API /nearby: http://developers.webcams.travel/#webcams/list/nearby
 */
public class NearbyWebcamsLoader extends AsyncTaskLoader<LoadResult<List<Webcam>>> {

    private static final double DEFAULT_RADIUS_KM = 100.0;

    private final double latitude;
    private final double longitude;

    public NearbyWebcamsLoader(Context context, double latitude, double longitude) {
        super(context);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Webcam>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        List<Webcam> data = null;
        HttpURLConnection connection = null;

        try {
            connection = WebcamsApi.createNearbyRequest(latitude, longitude, DEFAULT_RADIUS_KM);
            Log.d(TAG, "Performing request: " + connection.getURL());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                InputStream in = stethoManager.interpretResponseStream(connection.getInputStream());
                data = WebcamsDomParser.parseWebcams(in);
                resultType = ResultType.OK;
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get webcams", e);

        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);

        } catch (Exception e) {
            Log.e(TAG, "Failed to get webcams: ", e);

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new LoadResult<>(resultType, data);
    }

    private static final String TAG = "Webcams";
}
