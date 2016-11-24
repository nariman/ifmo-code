package com.woofilee.ifmo.android.homework.service.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class provides a methods for reading and writing images on the internal storage.
 */
public final class ImageUtils {
    /**
     * Reads the image from the internal storage.
     *
     * @param filepath full path to the image file
     * @param context  application context
     * @return loaded image
     * @throws FileNotFoundException if path to the file not created, and file cannot be available
     */
    public static Bitmap readImage(String filepath,
                                   Context context) throws FileNotFoundException {
        FileInputStream fis = FileUtils.getInputFile(filepath, context);
        Bitmap bitmap = BitmapFactory.decodeStream(fis);

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * Writes the image to the internal storage.
     *
     * @param bitmap   image to save
     * @param filepath full path to the image file
     * @param context  application context
     * @throws FileNotFoundException if path to the file not created, and file cannot be available
     */
    public static boolean writeImage(Bitmap bitmap,
                                     String filepath,
                                     Context context) throws FileNotFoundException {
        FileOutputStream fos = FileUtils.getOutputFile(filepath, context);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
