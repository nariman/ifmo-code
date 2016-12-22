package ru.ifmo.droid2016.rzddemo.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ru.ifmo.droid2016.rzddemo.model.TimetableEntry;
import ru.ifmo.droid2016.rzddemo.api.ApiResponseParser;
import ru.ifmo.droid2016.rzddemo.api.RZDApi;
import ru.ifmo.droid2016.rzddemo.api.RouteIdParser;
import ru.ifmo.droid2016.rzddemo.api.TimetableParser;
import ru.ifmo.droid2016.rzddemo.cache.DataSchemeVersion;
import ru.ifmo.droid2016.rzddemo.cache.TimetableCache;
import ru.ifmo.droid2016.rzddemo.utils.IOUtils;

import static ru.ifmo.droid2016.rzddemo.Constants.LOG_DATE_FORMAT;
import static ru.ifmo.droid2016.rzddemo.Constants.TAG;

/**
 * Загружает расписание по заданным параметрам.
 */
public class TimetableLoader extends AsyncTaskLoader<LoadResult<List<TimetableEntry>>> {

    @NonNull
    private final String fromStationId;
    @NonNull
    private final String fromStationName;
    @NonNull
    private final String toStationId;
    @NonNull
    private final String toStationName;
    @NonNull
    private final Calendar fromDate;
    @NonNull
    private final Calendar toDate;

    @NonNull
    private final TimetableCache cache;

    private LoadResult<List<TimetableEntry>> lastResult;

    public TimetableLoader(@NonNull Context context,
                           @NonNull String fromStationId,
                           @NonNull String fromStationName,
                           @NonNull String toStationId,
                           @NonNull String toStationName,
                           @NonNull Calendar fromDate,
                           @NonNull Calendar toDate,
                           @DataSchemeVersion int version) {
        super(context);
        this.fromStationId = fromStationId;
        this.fromStationName = fromStationName;
        this.toStationId = toStationId;
        this.toStationName = toStationName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        cache = new TimetableCache(context, version);
    }

    @Override
    protected void onStartLoading() {
        if (lastResult == null) {
            forceLoad();
        } else {
            deliverResult(lastResult);
        }
    }

    @Override
    public void deliverResult(LoadResult<List<TimetableEntry>> data) {
        lastResult = data;
        super.deliverResult(data);
    }

    @Override
    public LoadResult<List<TimetableEntry>> loadInBackground() {
        List<TimetableEntry> timetable = loadFromCache();

        if (timetable != null) {
            Log.d(TAG, "Loaded from cache: fromStationId=" + fromStationId
                    + ", toStationId=" + toStationId
                    + ", date=" + LOG_DATE_FORMAT.format(fromDate.getTime())
                    + ", data size=" + timetable.size());
            return new LoadResult<>(ResultType.OK, timetable);

        } else {
            LoadResult<List<TimetableEntry>> result = loadFromApi();
            if (result.resultType == ResultType.OK && result.data != null) {
                saveToCache(result.data);
            }
            return result;
        }
    }

    @Nullable
    private List<TimetableEntry> loadFromCache() {
        try {
            return cache.get(fromStationId, toStationId, fromDate);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Cache miss: " + e);
        }
        return null;
    }

    private void saveToCache(@NonNull List<TimetableEntry> data) {
        Log.d(TAG, "Write to cache: fromStationId=" + fromStationId
                + ", toStationId=" + toStationId
                + ", date=" + LOG_DATE_FORMAT.format(fromDate.getTime())
                + ", data size=" + data.size());
        cache.put(fromStationId, toStationId, fromDate, data);
    }

    private LoadResult<List<TimetableEntry>> loadFromApi() {
        ResultType resultType = ResultType.ERROR;
        List<TimetableEntry> data = null;

        try {
            if (!RZDApi.hasSession()) {
                RZDApi.executeRequest(RZDApi.createSessionRequest(), ApiResponseParser.NULL_PARSER);
            }
            final String routeId = RZDApi.executeRequest(
                    RZDApi.createRouteRequest(fromStationName, fromStationId, toStationName,
                            toStationId, fromDate, toDate),
                    new RouteIdParser());

            // RZD API works only if there is delay between requests.
            Thread.sleep(2000);

            data = RZDApi.executeRequest(
                    RZDApi.createTimetableRequest(fromStationName, fromStationId, toStationName,
                            toStationId, fromDate, toDate, routeId),
                    new TimetableParser(fromStationId, toStationId));
            resultType = ResultType.OK;
            Log.d(TAG, "timetable size: " + data.size());

        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get timetable: " + e, e);

        } catch (IOException e) {
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            Log.e(TAG, "Failed to get timetable: " + e, e);
        }

        return new LoadResult<>(resultType, data);
    }
}
