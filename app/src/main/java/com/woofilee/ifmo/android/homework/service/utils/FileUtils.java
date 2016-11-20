package com.woofilee.ifmo.android.homework.service.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Class provides a methods for reading and writing files on the File System.
 */
public final class FileUtils {
    /**
     * Return file to read, and, if needed, created path to file.
     *
     * @param filename  name of file
     * @param extension extension of file
     * @param context   application context
     * @return          file instance to read
     * @throws FileNotFoundException if path to the file not created, and file cannot be available
     */
    public static FileInputStream getInputFile(String filename,
                                               String extension,
                                               Context context) throws FileNotFoundException {
        File f = getFile(filename, extension, context);

        if (f == null) {
            throw new FileNotFoundException("Path to file not created");
        }

        return new FileInputStream(f);
    }

    /**
     * Return file to write, and, if needed, created path to file.
     *
     * @param filename  name of file
     * @param extension extension of file
     * @param context   application context
     * @return          file instance to write
     * @throws FileNotFoundException if path to the file not created, and file cannot be available
     */
    public static FileOutputStream getOutputFile(String filename,
                                                 String extension,
                                                 Context context) throws FileNotFoundException {
        File f = getFile(filename, extension, context);

        if (f == null) {
            throw new FileNotFoundException("Path to file not created");
        }

        return new FileOutputStream(f);
    }

    /**
     * Returns file instance, and, if needed, created path to file.
     *
     * @param filename  name of file
     * @param extension extension of file
     * @param context   application context
     * @return          file instance
     */
    private static File getFile(String filename, String extension, Context context) {
        File f = context.getFilesDir();
//        File f = context.getExternalFilesDir(null); // For external storage

        if (!f.exists()) {
            if (!f.mkdirs()) {
                return null; // I can't create path for this file :(
            }
        }

        return new File(f, filename + "." + extension);
    }

    private FileUtils() {
    }
}
