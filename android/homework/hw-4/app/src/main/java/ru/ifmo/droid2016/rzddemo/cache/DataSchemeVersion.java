package ru.ifmo.droid2016.rzddemo.cache;

import android.support.annotation.IntDef;

import ru.ifmo.droid2016.rzddemo.model.TimetableEntry;

/**
 * Версии модели данных, поддерживаемые кэшем расписаний.
 */
@IntDef(value = {
        DataSchemeVersion.V1,
        DataSchemeVersion.V2
})
public @interface DataSchemeVersion {
    /**
     * В первой версии данных есть все поля класса {@link TimetableEntry},
     * кроме названия поезда (поле trainName).
     */
    int V1 = 1;

    /**
     * Во второй версии добавлена поддержка названия поезда - поля trainName.
     */
    int V2 = 2;
}
