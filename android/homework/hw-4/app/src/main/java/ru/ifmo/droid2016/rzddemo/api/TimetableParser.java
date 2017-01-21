package ru.ifmo.droid2016.rzddemo.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.ifmo.droid2016.rzddemo.model.TimetableEntry;
import ru.ifmo.droid2016.rzddemo.utils.TimeUtils;

import static ru.ifmo.droid2016.rzddemo.Constants.TAG;

/**
 * Created by dmitry.trunin on 08.11.2016.
 */

public class TimetableParser implements ApiResponseParser<List<TimetableEntry>> {

    @NonNull
    private final String departureStationId;
    @NonNull
    private final String arrivalStationId;

    public TimetableParser(@NonNull String departureStationId,
                           @NonNull String arrivalStationId) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
    }

    @Override
    @NonNull
    public List<TimetableEntry> parse(InputStream in, String charset)
            throws IOException, BadResponseException {
        final JsonReader reader = new JsonReader(new InputStreamReader(in, charset));
        return parseResponse(reader);
    }

    @NonNull
    private List<TimetableEntry> parseResponse(JsonReader reader)
            throws IOException, BadResponseException {
        String result = null;
        List<TimetableEntry> timetable = null;

        reader.beginObject();
        while (reader.hasNext()) {
            final String name = reader.nextName();
            if (name == null) {
                reader.skipValue();
                continue;
            }
            switch (name) {
                case "result": result = reader.nextString(); break;
                case "tp": timetable = parseTp(reader); break;
                default: reader.skipValue(); break;
            }
        }
        reader.endObject();

        if (!"OK".equals(result)) {
            throw new BadResponseException("Result is not OK: " + result);
        }
        if (timetable == null) {
            throw new BadResponseException("Empty result");
        }
        return timetable;
    }

    @Nullable
    private List<TimetableEntry> parseTp(JsonReader reader)
            throws IOException, BadResponseException {
        List<TimetableEntry> timetable = null;
        reader.beginArray();
        while (reader.hasNext()) {
            if (timetable == null) {
                timetable = parseTpElement(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endArray();
        return timetable;
    }

    @Nullable
    private List<TimetableEntry> parseTpElement(JsonReader reader)
            throws IOException, BadResponseException {
        List<TimetableEntry> timetable = null;
        reader.beginObject();
        while (reader.hasNext()) {
            final String name = reader.nextName();
            if (name == null) {
                reader.skipValue();
                continue;
            }
            switch (name) {
                case "list": timetable = parseList(reader); break;
                default: reader.skipValue(); break;
            }
        }
        reader.endObject();
        return timetable;
    }

    @NonNull
    private List<TimetableEntry> parseList(JsonReader reader)
            throws IOException, BadResponseException {
        List<TimetableEntry> timetable = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            TimetableEntry timetableEntry = parseTimetableEntry(reader);
            if (timetableEntry != null) {
                timetable.add(timetableEntry);
            }
        }
        reader.endArray();
        return timetable;
    }

    @Nullable
    private TimetableEntry parseTimetableEntry(JsonReader reader)
            throws IOException, BadResponseException {
        String departureStationName = null;
        Calendar departureTime = null;
        String arrivalStationName = null;
        Calendar arrivalTime = null;
        String trainRouteId = null;
        String trainName = null;
        String routeStartStationName = null;
        String routeEndStationName = null;

        // Components of departure/arrival times
        String date0 = null;
        String date1 = null;
        String time0 = null;
        String time1 = null;

        reader.beginObject();
        while (reader.hasNext()) {
            final String name = reader.nextName();
            if (name == null) {
                reader.skipValue();
                continue;
            }
            switch (name) {
                case "station0": departureStationName = reader.nextString(); break;
                case "station1": arrivalStationName = reader.nextString(); break;
                case "route0": routeStartStationName = reader.nextString(); break;
                case "route1": routeEndStationName = reader.nextString(); break;
                case "number": trainRouteId = reader.nextString(); break;
                case "brand": trainName = reader.nextString(); break;
                case "date0": date0 = reader.nextString(); break;
                case "date1": date1 = reader.nextString(); break;
                case "time0": time0 = reader.nextString(); break;
                case "time1": time1 = reader.nextString(); break;
                default: reader.skipValue(); break;
            }
        }
        reader.endObject();

        if (isAnyEmpty(departureStationName, arrivalStationName, trainRouteId,
                routeStartStationName, routeEndStationName, date0, date1, time0, time1)) {
            return null;
        }
        departureTime = parseMskTime(date0, time0);
        arrivalTime = parseMskTime(date1, time1);
        if (departureTime == null || arrivalTime == null) {
            return null;
        }

        return new TimetableEntry(departureStationId, departureStationName, departureTime,
                arrivalStationId, arrivalStationName, arrivalTime, trainRouteId, trainName,
                routeStartStationName, routeEndStationName);
    }

    private static boolean isAnyEmpty(String ... vals) {
        if (vals == null) {
            return false;
        }
        for (String val : vals) {
            if (TextUtils.isEmpty(val)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private static Calendar parseMskTime(String date, String time) {
        try {
            Date dateTime = dateFormat.parse(date + " " + time);
            Calendar calendar = Calendar.getInstance(TimeUtils.getMskTimeZone());
            calendar.setTime(dateTime);
            return calendar;

        } catch (ParseException e) {
            Log.e(TAG, "Failed to parse date=\"" + date + "\", time=\"" + time + "\": " + e, e);
        }
        return null;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    static {
        dateFormat.setTimeZone(TimeUtils.getMskTimeZone());

    }
}
