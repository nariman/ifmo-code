package com.woofilee.ifmo.android.homework.service.util;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class provides a methods for internal storage management.
 */
public final class StorageUtils {
    /**
     * Returns {@link File} instance by abstract path, and, if needed, created parents path.
     *
     * @param path    full path
     * @param context application context
     * @return {@link File} instance
     * @throws FileNotFoundException if attempt to create parents path fails
     */
    public static File get(String path,
                           Context context) throws FileNotFoundException {
        File current = new File(context.getFilesDir(), path);
        File parent = current.getParentFile();

        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                throw new FileNotFoundException("Attempt to create parents path fails");
            }
        }

        return current;
    }

    /**
     * Deletes file or folder by abstract path. If this path points to folder, then this folder must
     * be empty.
     *
     * @param path    full path
     * @param context application context
     * @return {@code true}, if file or folder is deleted (or not exists), otherwise {@code false}
     * @throws FileNotFoundException
     */
    public static boolean delete(String path,
                                 Context context) throws FileNotFoundException {
        File file = new File(context.getFilesDir(), path);
        return !file.exists() || file.delete();
    }

    private StorageUtils() {
    }
}
