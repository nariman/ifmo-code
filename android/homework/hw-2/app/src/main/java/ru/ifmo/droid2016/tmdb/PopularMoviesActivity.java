package ru.ifmo.droid2016.tmdb;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import ru.ifmo.droid2016.tmdb.adapter.PopularMoviesAdapter;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * The Movie DB top movies screen
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private static final String TAG = "ACTIVITY";

    RecyclerView recycler;
    PopularMoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        recycler = (RecyclerView) findViewById(R.id.recycler);

        FragmentManager fm = getFragmentManager();
        PopularMoviesAdapter.PopularMoviesAdapterBundle adapterBundle =
                (PopularMoviesAdapter.PopularMoviesAdapterBundle) fm.findFragmentByTag("adapter");

        final PopularMoviesActivity activity = this;

        if (adapterBundle == null) {
            adapter = new PopularMoviesAdapter(this, recycler) {
                @Override
                protected void onLoadPage(int page) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("page", page);
                    getLoaderManager().restartLoader(page, bundle, activity);
                }
            };

            fm.beginTransaction().add(adapter.getBundle(), "adapter").commit();
        } else {
            adapter = new PopularMoviesAdapter(this, recycler, adapterBundle) {
                @Override
                protected void onLoadPage(int page) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("page", page);
                    getLoaderManager().restartLoader(page, bundle, activity);
                }
            };
        }

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                ???
//            }
//        }, new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle bundle) {
        return new PopularMoviesLoader(this, bundle.getInt("page", 1));
    }

    @Override
    public void onLoadFinished(final Loader<LoadResult<List<Movie>>> loader,
                               LoadResult<List<Movie>> result) {
        adapter.onLoadedPage(result.data);

        if (result.resultType == ResultType.OK && result.data != null && result.data.isEmpty()) {
            if (result.data.isEmpty()) {
                Snackbar.make(
                        findViewById(R.id.layout),
                        R.string.no_more_movies_avaiable,
                        Snackbar.LENGTH_LONG
                ).show();
            }
        } else if (result.resultType != ResultType.OK) { // !
            Snackbar.make(
                    findViewById(R.id.layout),
                    (result.resultType == ResultType.NO_INTERNET) ? R.string.no_internet : R.string.unexpected_error,
                    Snackbar.LENGTH_LONG
            ).setAction(R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.onReloadPage();
                }
            }).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
    }
}
