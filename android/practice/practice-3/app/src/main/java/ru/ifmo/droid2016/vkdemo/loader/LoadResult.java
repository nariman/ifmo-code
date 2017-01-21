package ru.ifmo.droid2016.vkdemo.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Результат загрузки данных.
 */

public class LoadResult<TData, TError> {

    /**
     * Чем закончилась загрузка? (ок или не ок)
     */
    @NonNull
    public final ResultType resultType;

    /**
     * Загруженные данные (в случае, если resultType == OK) или null.
     */
    @Nullable
    public final TData data;

    /**
     * Ошибка загрузки (в случае, если resultType == ERROR) или null.
     */
    @Nullable
    public final TError error;

    private LoadResult(@NonNull ResultType resultType,
                       @Nullable TData data,
                       @Nullable TError error) {
        this.resultType = resultType;
        this.data = data;
        this.error = error;
    }

    @NonNull
    public static <TData, TError> LoadResult<TData, TError> ok(@NonNull TData data) {
        return new LoadResult<>(ResultType.OK, data, null);
    }

    @NonNull
    public static <TData, TError> LoadResult<TData, TError> error(@NonNull TError error) {
        return new LoadResult<>(ResultType.ERROR, null, error);
    }

    @NonNull
    public static <TData, TError> LoadResult<TData, TError> noInternet() {
        return new LoadResult<>(ResultType.NO_INTERNET, null, null);
    }
}
