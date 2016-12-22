package ru.ifmo.droid2016.rzddemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ru.ifmo.droid2016.rzddemo.cache.DataSchemeVersion;
import ru.ifmo.droid2016.rzddemo.loader.LoadResult;
import ru.ifmo.droid2016.rzddemo.loader.ResultType;
import ru.ifmo.droid2016.rzddemo.loader.TimetableLoader;
import ru.ifmo.droid2016.rzddemo.model.TimetableEntry;
import ru.ifmo.droid2016.rzddemo.utils.RecylcerDividersDecorator;
import ru.ifmo.droid2016.rzddemo.utils.TimeUtils;

public class TimetableActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<LoadResult<List<TimetableEntry>>>,
        View.OnClickListener {

    private static final String EXTRA_FROM_STATION_ID = "from_station_id";
    private static final String EXTRA_TO_STATION_ID = "to_station_id";
    private static final String EXTRA_FROM_STATION_NAME = "from_station_name";
    private static final String EXTRA_TO_STATION_NAME = "to_station_name";
    private static final String EXTRA_VERSION = "version";

    private static final String ARG_DATE = "date";

    private static final String KEY_CURRENT_DATE = "current_date";

    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;
    private View previousButton;
    private View nextButton;
    private TextView dateView;

    // Currently selected date, starting from today
    private Calendar currentDate;
    private boolean isPreviousDateEnabled;

    private java.text.DateFormat dateFormat;

    @Nullable
    private TimetableRecyclerAdapter adapter;

    public static Intent createIntent(@NonNull Context context,
                                      @NonNull String fromStationId,
                                      @NonNull String fromStationName,
                                      @NonNull String toStationId,
                                      @NonNull String toStationName,
                                      @DataSchemeVersion int version) {
        final Intent intent = new Intent(context, TimetableActivity.class);
        intent.putExtra(EXTRA_FROM_STATION_NAME, fromStationName);
        intent.putExtra(EXTRA_TO_STATION_NAME, toStationName);
        intent.putExtra(EXTRA_FROM_STATION_ID, fromStationId);
        intent.putExtra(EXTRA_TO_STATION_ID, toStationId);
        intent.putExtra(EXTRA_VERSION, version);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateFormat = DateFormat.getLongDateFormat(this);
        setContentView(R.layout.activity_timetable);

        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);
        previousButton = findViewById(R.id.btn_previous_day);
        nextButton = findViewById(R.id.btn_next_day);
        dateView = (TextView) findViewById(R.id.date_text);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.grey_500)));

        if (savedInstanceState != null) {
            currentDate = (Calendar) savedInstanceState.getSerializable(KEY_CURRENT_DATE);
        }
        if (currentDate == null) {
            currentDate = TimeUtils.getCurrentTime(TimeUtils.getMskTimeZone());
        }
        isPreviousDateEnabled =
                currentDate.after(TimeUtils.getTomorrow0(TimeUtils.getMskTimeZone()));
        restartLoader(false);
    }

    private void restartLoader(boolean force) {
        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        displayDate();
        disableButtons();

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, currentDate);
        args.putAll(getIntent().getExtras());
        if (force) {
            getSupportLoaderManager().restartLoader(0, args, this);
        } else {
            getSupportLoaderManager().initLoader(0, args, this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_CURRENT_DATE, currentDate);
    }

    @Override
    public Loader<LoadResult<List<TimetableEntry>>> onCreateLoader(int id, Bundle args) {
        Calendar fromDate = (Calendar) args.getSerializable(ARG_DATE);
        if (fromDate == null) {
            fromDate = TimeUtils.getCurrentTime(TimeUtils.getMskTimeZone());
        }
        Calendar toDate = TimeUtils.getNextDay(fromDate);
        final String fromStationId =
                args.getString(EXTRA_FROM_STATION_ID, Constants.DEMO_FROM_STATION_ID);
        final String toStationId =
                args.getString(EXTRA_TO_STATION_ID, Constants.DEMO_TO_STATION_ID);
        final String fromStationName =
                args.getString(EXTRA_FROM_STATION_NAME, Constants.DEMO_FROM_STATION_NAME);
        final String toStationName =
                args.getString(EXTRA_TO_STATION_NAME, Constants.DEMO_TO_STATION_NAME);
        final @DataSchemeVersion int version = args.getInt(EXTRA_VERSION, DataSchemeVersion.V1);

        return new TimetableLoader(this, fromStationId, fromStationName, toStationId,
                toStationName, fromDate, toDate, version);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<TimetableEntry>>> loader,
                               LoadResult<List<TimetableEntry>> result) {
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                displayNonEmptyData(result.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(result.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<TimetableEntry>>> loader) {
        displayEmptyData();
    }

    @Override
    public void onClick(View view) {
        if (view == previousButton) {
            currentDate.add(Calendar.DAY_OF_MONTH, -1);
        } else if (view == nextButton) {
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        isPreviousDateEnabled =
                currentDate.after(TimeUtils.getTomorrow0(TimeUtils.getMskTimeZone()));
        restartLoader(true);
    }

    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.no_trains);
        enableButton();
    }

    private void displayNonEmptyData(List<TimetableEntry> data) {
        if (adapter == null) {
            adapter = new TimetableRecyclerAdapter(this, data);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(data);
        }
        progressView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        enableButton();
    }

    private void displayError(ResultType resultType) {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {
            messageResId = R.string.no_inernet;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
        enableButton();
    }

    private void disableButtons() {
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
    }

    private void enableButton() {
        final boolean previousIsEnabled = currentDate != null && isPreviousDateEnabled;
        previousButton.setEnabled(previousIsEnabled);
        nextButton.setEnabled(true);
    }

    private void displayDate() {
        dateView.setText(dateFormat.format(currentDate.getTime()));
        previousButton.setVisibility(isPreviousDateEnabled ? View.VISIBLE : View.INVISIBLE);
    }
}
