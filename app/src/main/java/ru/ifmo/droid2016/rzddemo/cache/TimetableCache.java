package ru.ifmo.droid2016.rzddemo.cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.rzddemo.model.TimetableEntry;
import ru.ifmo.droid2016.rzddemo.utils.TimeUtils;

import static ru.ifmo.droid2016.rzddemo.Constants.LOG_DATE_FORMAT;

/**
 * Кэш расписания поездов.
 * <p>
 * Ключом является комбинация трех значений:
 * ID станции отправления, ID станции прибытия, дата в москомском часовом поясе
 * <p>
 * Единицей хранения является список поездов - {@link TimetableEntry}.
 */

public class TimetableCache {
    @NonNull
    private final Context context;

    /**
     * Версия модели данных, с которой работает кэш.
     */
    @DataSchemeVersion
    private final int version;

    /**
     * Database handler with methods for timetable table
     */
    private final TimetableDatabaseHandler databaseHandler;

    private final SimpleDateFormat schemaDate;
    private final SimpleDateFormat schemaDateTime;

    /**
     * Создает экземпляр кэша с указанной версией модели данных.
     * <p>
     * Может вызываться на лююбом (в том числе UI потоке). Может быть создано несколько инстансов
     * {@link TimetableCache} -- все они должны потокобезопасно работать с одним физическим кэшом.
     */
    @AnyThread
    public TimetableCache(@NonNull Context context,
                          @DataSchemeVersion int version) {
        this.context = context.getApplicationContext();
        this.version = version;

        this.databaseHandler = new TimetableDatabaseHandler(this.context, this.version);
        this.schemaDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        this.schemaDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.CANADA);
    }

    /**
     * Берет из кэша расписание - список всех поездов, следующих по указанному маршруту с
     * отправлением в указанную дату.
     *
     * @param fromStationId ID станции отправления
     * @param toStationId   ID станции прибытия
     * @param dateMsk       дата в московском часовом поясе
     * @return - список {@link TimetableEntry}
     * @throws FileNotFoundException - если в кэше отсуствуют запрашиваемые данные.
     */
    @WorkerThread
    @NonNull
    public List<TimetableEntry> get(@NonNull String fromStationId,
                                    @NonNull String toStationId,
                                    @NonNull Calendar dateMsk)
            throws FileNotFoundException {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();

        Cursor cursor = db.query(
                TimetableContract.Table.NAME,
                ((version == DataSchemeVersion.V2)
                        ?
                        new String[]{
                                TimetableContract.Columns.DEPARTURE_STATION_ID,
                                TimetableContract.Columns.DEPARTURE_STATION_NAME,
                                TimetableContract.Columns.DEPARTURE_TIME,
                                TimetableContract.Columns.ARRIVAL_STATION_ID,
                                TimetableContract.Columns.ARRIVAL_STATION_NAME,
                                TimetableContract.Columns.ARRIVAL_TIME,
                                TimetableContract.Columns.TRAIN_ROUTE_ID,
                                TimetableContract.Columns.TRAIN_NAME,
                                TimetableContract.Columns.ROUTE_START_STATION_NAME,
                                TimetableContract.Columns.ROUTE_END_STATION_NAME
                        }
                        :
                        new String[]{
                                TimetableContract.Columns.DEPARTURE_STATION_ID,
                                TimetableContract.Columns.DEPARTURE_STATION_NAME,
                                TimetableContract.Columns.DEPARTURE_TIME,
                                TimetableContract.Columns.ARRIVAL_STATION_ID,
                                TimetableContract.Columns.ARRIVAL_STATION_NAME,
                                TimetableContract.Columns.ARRIVAL_TIME,
                                TimetableContract.Columns.TRAIN_ROUTE_ID,
                                TimetableContract.Columns.ROUTE_START_STATION_NAME,
                                TimetableContract.Columns.ROUTE_END_STATION_NAME
                        }
                ),
                TimetableContract.Columns.DATE + "=? AND "
                        + TimetableContract.Columns.DEPARTURE_STATION_ID + "=? AND "
                        + TimetableContract.Columns.ARRIVAL_STATION_ID + "=?",
                new String[]{
                        schemaDate.format(dateMsk.getTime()),
                        fromStationId,
                        toStationId
                },
                null, // group
                null, // having
                null, // order
                null  // limit
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                List<TimetableEntry> timetable = new ArrayList<>();

                do {
                    TimetableEntry entry = null;

                    try {
                        int cnt = 0;

                        entry = new TimetableEntry(
                                cursor.getString(cnt++),
                                cursor.getString(cnt++),
                                parseCalendar(cursor.getString(cnt++)),
                                cursor.getString(cnt++),
                                cursor.getString(cnt++),
                                parseCalendar(cursor.getString(cnt++)),
                                cursor.getString(cnt++),
                                ((version == DataSchemeVersion.V2) ? cursor.getString(cnt++) : null),
                                cursor.getString(cnt++),
                                cursor.getString(cnt)
                        );
                    } catch (ParseException e) {
                        e.printStackTrace();
                        continue; // skip entry
                    }

                    timetable.add(entry);
                } while (cursor.moveToNext());

                return timetable;
            }

            throw new FileNotFoundException("No data in timetable cache for: fromStationId="
                    + fromStationId + ", toStationId=" + toStationId
                    + ", dateMsk=" + LOG_DATE_FORMAT.format(dateMsk.getTime()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            db.close();
        }
    }

    @WorkerThread
    public void put(@NonNull String fromStationId,
                    @NonNull String toStationId,
                    @NonNull Calendar dateMsk,
                    @NonNull List<TimetableEntry> timetable) {
        SQLiteDatabase db = databaseHandler.getWritableDatabase();

        SQLiteStatement prepared = db.compileStatement(
                "INSERT INTO " + TimetableContract.Table.NAME + " ("
                        + TimetableContract.Columns.DATE + ","
                        + TimetableContract.Columns.DEPARTURE_STATION_ID + ","
                        + TimetableContract.Columns.DEPARTURE_STATION_NAME + ","
                        + TimetableContract.Columns.DEPARTURE_TIME + ","
                        + TimetableContract.Columns.ARRIVAL_STATION_ID + ","
                        + TimetableContract.Columns.ARRIVAL_STATION_NAME + ","
                        + TimetableContract.Columns.ARRIVAL_TIME + ","
                        + TimetableContract.Columns.TRAIN_ROUTE_ID + ","
                        + ((version == DataSchemeVersion.V2)
                        ? TimetableContract.Columns.TRAIN_NAME + "," : "")
                        + TimetableContract.Columns.ROUTE_START_STATION_NAME + ","
                        + TimetableContract.Columns.ROUTE_END_STATION_NAME
                        + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
                        + ((version == DataSchemeVersion.V2) ? ", ?" : "")
                        + ")"
        );

        db.beginTransaction();

        try {
            for (TimetableEntry entry : timetable) {
                int key = 1;

                prepared.bindString(key++, schemaDate.format(dateMsk.getTime()));
                prepared.bindString(key++, entry.departureStationId);
                prepared.bindString(key++, entry.departureStationName);
                prepared.bindString(key++, schemaDateTime.format(entry.departureTime.getTime()));
                prepared.bindString(key++, entry.arrivalStationId);
                prepared.bindString(key++, entry.arrivalStationName);
                prepared.bindString(key++, schemaDateTime.format(entry.arrivalTime.getTime()));
                prepared.bindString(key++, entry.trainRouteId);

                if (version == DataSchemeVersion.V2) {
                    if (entry.trainName == null) {
                        prepared.bindNull(key++);
                    } else {
                        prepared.bindString(key++, entry.trainName);
                    }
                }

                prepared.bindString(key++, entry.routeStartStationName);
                prepared.bindString(key, entry.routeEndStationName);

                prepared.executeInsert();
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
    }

    private Calendar parseCalendar(String iso) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeUtils.getMskTimeZone());
        calendar.setTime(schemaDateTime.parse(iso));
        return calendar;
    }

    // Table schema

    private static final class TimetableContract {
        /**
         * Table database table columns
         */
        interface Columns {
            /**
             * Table entry ID
             */
            String ID = "id";

            /**
             * Table entry date
             */
            String DATE = "date";

            String DEPARTURE_STATION_ID = "departure_station_id";
            String DEPARTURE_STATION_NAME = "departure_station_name";
            String DEPARTURE_TIME = "departure_time";
            String ARRIVAL_STATION_ID = "arrival_station_id";
            String ARRIVAL_STATION_NAME = "arrival_station_name";
            String ARRIVAL_TIME = "arrival_time";
            String TRAIN_ROUTE_ID = "train_route_id";
            String TRAIN_NAME = "train_name";
            String ROUTE_START_STATION_NAME = "route_start_station_name";
            String ROUTE_END_STATION_NAME = "route_end_station_name";
        }

        /**
         * Timetable database table definition
         */
        static final class Table implements Columns {

            /**
             * Table name
             */
            static final String NAME = "timetable";
        }

        private TimetableContract() {
        }
    }

    // Table database handler

    private static class TimetableDatabaseHandler extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "rzd.timetable.db";

        @DataSchemeVersion
        private int version;

        TimetableDatabaseHandler(Context context, @DataSchemeVersion int version) {
            super(context, DATABASE_NAME, null, version);
            this.version = version;
        }

        private void createTable(SQLiteDatabase db, @DataSchemeVersion int version) {
            db.execSQL(
                    "CREATE TABLE " + TimetableContract.Table.NAME + " ("
                            + TimetableContract.Columns.ID + " INTEGER PRIMARY KEY" + ","
                            + TimetableContract.Columns.DATE + " TEXT" + ","
                            + TimetableContract.Columns.DEPARTURE_STATION_ID + " TEXT" + ","
                            + TimetableContract.Columns.DEPARTURE_STATION_NAME + " TEXT" + ","
                            + TimetableContract.Columns.DEPARTURE_TIME + " TEXT" + ","
                            + TimetableContract.Columns.ARRIVAL_STATION_ID + " TEXT" + ","
                            + TimetableContract.Columns.ARRIVAL_STATION_NAME + " TEXT" + ","
                            + TimetableContract.Columns.ARRIVAL_TIME + " TEXT" + ","
                            + TimetableContract.Columns.TRAIN_ROUTE_ID + " TEXT" + ","
                            + ((version == DataSchemeVersion.V2)
                            ? TimetableContract.Columns.TRAIN_NAME + " TEXT" + "," : "")
                            + TimetableContract.Columns.ROUTE_START_STATION_NAME + " TEXT" + ","
                            + TimetableContract.Columns.ROUTE_END_STATION_NAME + " TEXT"
                            + ")"
            );
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db, version);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(
                    "ALTER TABLE " + TimetableContract.Table.NAME + " "
                            + "ADD COLUMN " + TimetableContract.Columns.TRAIN_NAME + " TEXT");
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String DOWNGRADE_TABLE_NAME = TimetableContract.Table.NAME + "_downgrade";

            db.execSQL(
                    "ALTER TABLE " + TimetableContract.Table.NAME + " " +
                            "RENAME TO " + DOWNGRADE_TABLE_NAME
            );

            createTable(db, DataSchemeVersion.V1);

            String columns = ""
                    + TimetableContract.Columns.ID + ","
                    + TimetableContract.Columns.DATE + ","
                    + TimetableContract.Columns.DEPARTURE_STATION_ID + ","
                    + TimetableContract.Columns.DEPARTURE_STATION_NAME + ","
                    + TimetableContract.Columns.DEPARTURE_TIME + ","
                    + TimetableContract.Columns.ARRIVAL_STATION_ID + ","
                    + TimetableContract.Columns.ARRIVAL_STATION_NAME + ","
                    + TimetableContract.Columns.ARRIVAL_TIME + ","
                    + TimetableContract.Columns.TRAIN_ROUTE_ID + ","
                    + TimetableContract.Columns.ROUTE_START_STATION_NAME + ","
                    + TimetableContract.Columns.ROUTE_END_STATION_NAME;

            db.execSQL(
                    "INSERT INTO " + TimetableContract.Table.NAME + " (" + columns + ") " +
                            "SELECT " + columns + " " +
                            "FROM " + DOWNGRADE_TABLE_NAME
            );

            db.execSQL("DROP TABLE " + DOWNGRADE_TABLE_NAME);
        }
    }
}
