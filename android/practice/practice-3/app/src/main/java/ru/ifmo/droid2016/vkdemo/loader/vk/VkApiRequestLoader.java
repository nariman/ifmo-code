package ru.ifmo.droid2016.vkdemo.loader.vk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKApiModel;

import ru.ifmo.droid2016.vkdemo.loader.LoadResult;

/**
 * Базовый загрузчик дял выполнения типизированного запроса к VK API. Параметр TData указывет
 * на тип результат, который получается после разбора ответа от АПИ при помощи парсера.
 */

public class VkApiRequestLoader<TData extends VKApiModel>
        extends AsyncTaskLoader<LoadResult<TData, VKError>> {

    public static final int VK_LOADER_ERROR = -201;

    /**
     * Запрос, который надо выполнить.
     */
    @NonNull
    private final VKRequest request;

    /**
     * Класс результата, который нужно получить.
     */
    @NonNull
    private final Class<TData> vkModelClass;

    public VkApiRequestLoader(@NonNull Context context,
                              @NonNull VKRequest request,
                              @NonNull Class<TData> vkModelClass) {
        super(context);
        this.request = request;
        this.vkModelClass = vkModelClass;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<TData, VKError> loadInBackground() {
        // TODO: Task 3
        return LoadResult.error(vkError("Not implemented"));
    }

    private static VKError vkError(String message) {
        final VKError error = new VKError(VK_LOADER_ERROR);
        error.errorMessage = message;
        return error;
    }


    private static final String TAG = "VkLoader";
}
