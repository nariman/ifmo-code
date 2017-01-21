package ru.ifmo.droid2016.worldcam.worldcamdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import ru.ifmo.droid2016.worldcam.worldcamdemo.loader.LoadResult;
import ru.ifmo.droid2016.worldcam.worldcamdemo.loader.NearbyWebcamsLoader;
import ru.ifmo.droid2016.worldcam.worldcamdemo.model.Webcam;

/**
 * Показывает список камер в выбранных координатах. Координаты передаются в extra параметрах
 * активности.
 */
public class NearbyWebcamsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Webcam>>> {

    public static final String EXTRA_LATITUDE = "lat";
    public static final String EXTRA_LONGITUDE = "lng";

    ProgressBar progress;
    RecyclerView recycler;

    WebcamsRecyclerAdaper adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_webcams_activity);

        progress = (ProgressBar) findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        progress.setVisibility(View.VISIBLE);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Pass all extra params directly to loader
        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public Loader<LoadResult<List<Webcam>>> onCreateLoader(int id, Bundle args) {
        final double latitude;
        final double longitude;

        if (args != null && args.containsKey(EXTRA_LATITUDE) && args.containsKey(EXTRA_LONGITUDE)) {
            latitude = args.getDouble(EXTRA_LATITUDE);
            longitude = args.getDouble(EXTRA_LONGITUDE);
        } else {
            // default location near SPb center
            latitude = 59.930;
            longitude = 30.372;
        }
        Log.d(TAG, "onCreateLoader: latitude=" + latitude + " longitude=" + longitude);
        return new NearbyWebcamsLoader(this, latitude, longitude);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Webcam>>> loader,
                               LoadResult<List<Webcam>> result) {
        Log.d(TAG, "onLoadFinished: " + result);

        adapter = new WebcamsRecyclerAdaper(this, result.data);
        recycler.setAdapter(adapter);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Webcam>>> loader) {
    }

    private static final String TAG = "Webcams";

}
