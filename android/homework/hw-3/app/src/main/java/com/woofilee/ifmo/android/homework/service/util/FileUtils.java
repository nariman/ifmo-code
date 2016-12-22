package com.woofilee.ifmo.android.homework.service.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.woofilee.ifmo.android.homework.service.util.StorageUtils.get;

/**
 * Class provides a methods for reading and writing files on the internal storage.
 */
public class FileUtils {
    /**
     * Returns {@link FileInputStream} to read, and, if needed, creates full path to the file.
     *
     * @param filepath full path to the file
     * @param context  application context
     * @return {@link FileInputStream} instance to read
     * @throws FileNotFoundException if path to the file not created, and file cannot be available
     */
    public static FileInputStream getInputFile(String filepath,
                                               Context context) throws FileNotFoundException {
        return new FileInputStream(get(filepath, context));
    }

    /**
     * Returns {@link FileOutputStream} to write, and, if needed, creates full path to the file.
     *
     * @param filepath full path to the file
     * @param context  application context
     * @return {@link FileOutputStream} instance to write
     * @throws FileNotFoundException if path to the file not created, and file cannot be available
     */
    public static FileOutputStream getOutputFile(String filepath,
                                                 Context context) throws FileNotFoundException {
        return new FileOutputStream(get(filepath, context));
    }
}
