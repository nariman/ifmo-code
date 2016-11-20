package com.woofilee.ifmo.android.homework.service.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.woofilee.ifmo.android.homework.service.R;
import com.woofilee.ifmo.android.homework.service.receivers.ImageServiceReceiver;
import com.woofilee.ifmo.android.homework.service.services.ImageService;
import com.woofilee.ifmo.android.homework.service.utils.FileUtils;
import com.woofilee.ifmo.android.homework.service.utils.ServiceUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Main screen
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private static final int IMAGE_SAMPLE_SIZE = 1024;
    private static final float BLUR_IMAGE_SCALE = 0.25f;
    private static final float BLUR_IMAGE_RADIUS = 7.5f;

    private Intent serviceIntent;
    private ImageServiceReceiver receiver;

    private CoordinatorLayout coordinatorLayout;

    private ImageView imageView;
    private ImageView blurImageView;
    private Button buttonStart;
    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(getApplicationContext(), ImageService.class);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);

        imageView = (ImageView) findViewById(R.id.image_view);
        blurImageView = (ImageView) findViewById(R.id.blur_image_view);
        buttonStart = (Button) findViewById(R.id.button_start);
        buttonStop = (Button) findViewById(R.id.button_stop);

        if (ServiceUtils.isServiceRunning(ImageService.class, getApplicationContext())) {
            buttonStop.setVisibility(View.VISIBLE);
        } else {
            buttonStart.setVisibility(View.VISIBLE);
        }

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setVisibility(View.GONE);
                startService(serviceIntent);
                buttonStop.setVisibility(View.VISIBLE);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setVisibility(View.GONE);
                stopService(serviceIntent);
                buttonStart.setVisibility(View.VISIBLE);
            }
        });

        receiver = new ImageServiceReceiver() {
            @Override
            public void onFinishLoading() {
                updateImage();
                Snackbar.make(coordinatorLayout, R.string.reloaded, Snackbar.LENGTH_LONG).show();
            }
        };

        registerReceiver(receiver, new IntentFilter(ImageServiceReceiver.BROADCAST_ACTION));
        updateImage();
    }

    private void updateImage() {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) { // Async image reading from file + rendering blur image
                Bitmap bitmap;

                try {
                    FileInputStream fis = FileUtils.getInputFile(ImageService.IMAGE_FILENAME,
                            ImageService.IMAGE_EXTENSION, getApplicationContext());

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    BitmapFactory.decodeStream(fis, null, options);

                    options.inSampleSize = calculateSampleSize(
                            options, IMAGE_SAMPLE_SIZE, IMAGE_SAMPLE_SIZE);
                    options.inJustDecodeBounds = false;

                    fis = FileUtils.getInputFile(ImageService.IMAGE_FILENAME,
                            ImageService.IMAGE_EXTENSION, getApplicationContext());

                    bitmap = BitmapFactory.decodeStream(fis, null, options);

                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(coordinatorLayout, R.string.no_available_image,
                                    Snackbar.LENGTH_INDEFINITE).show();
                        }
                    });
                }

                RenderScript rs = RenderScript.create(getApplicationContext());

                final Bitmap blurBitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        Math.round(bitmap.getWidth() * BLUR_IMAGE_SCALE),
                        Math.round(bitmap.getHeight() * BLUR_IMAGE_SCALE),
                        false
                );

                final Allocation input = Allocation.createFromBitmap(rs, blurBitmap);
                final Allocation output = Allocation.createTyped(rs, input.getType());

                final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(
                        rs,
                        Element.U8_4(rs)
                );

                script.setRadius(BLUR_IMAGE_RADIUS);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(blurBitmap);

                final Bitmap finalBitmap = bitmap;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalBitmap);
                        blurImageView.setImageBitmap(blurBitmap);
                    }
                });

                return null;
            }

            private int calculateSampleSize(BitmapFactory.Options options,
                                            int reqWidth,
                                            int reqHeight) {
                final int height = options.outHeight;
                final int width = options.outWidth;
                int sampleSize = 1;

                if (height > reqHeight || width > reqWidth) {
                    final int halfHeight = height / 2;
                    final int halfWidth = width / 2;

                    while ((halfHeight / sampleSize) >= reqHeight
                            && (halfWidth / sampleSize) >= reqWidth) {
                        sampleSize *= 2;
                    }
                }

                return sampleSize;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                System.gc();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
