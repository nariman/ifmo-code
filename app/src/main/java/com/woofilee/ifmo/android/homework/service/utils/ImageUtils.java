package com.woofilee.ifmo.android.homework.service.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class provides a methods for loading and saving images on the File System.
 */
public final class ImageUtils {
    /**
     * Saves the image to the internal storage.
     *
     * @param bitmap    image to save
     * @param filename  name of file
     * @param extension extension of file
     * @param context   context of file
     *
     * @throws FileNotFoundException if path to the file not created, and file cannot be available
     */
    public static void saveImage(Bitmap bitmap,
                                    String filename,
                                    String extension,
                                    Context context) throws FileNotFoundException {
        FileOutputStream fos = FileUtils.getOutputFile(filename, extension, context);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the image from the internal storage.
     *
     * @param filename  name of file
     * @param extension extension of file
     * @param context   context of file

     * @throws FileNotFoundException if path to the file not created, and file cannot be available
     */
    public static Bitmap loadImage(String filename,
                                   String extension,
                                   Context context) throws FileNotFoundException {
        FileInputStream fis = FileUtils.getInputFile(filename, extension, context);
        Bitmap bitmap = BitmapFactory.decodeStream(fis);

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
