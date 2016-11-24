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
import android.widget.ProgressBar;

import com.woofilee.ifmo.android.homework.service.receiver.ImageReceiver;
import com.woofilee.ifmo.android.homework.service.service.ImageService;
import com.woofilee.ifmo.android.homework.service.util.FileUtils;
import com.woofilee.ifmo.android.homework.service.util.ServiceUtils;

import static com.woofilee.ifmo.android.homework.service.R.layout.activity_main;

import static com.woofilee.ifmo.android.homework.service.R.mipmap.ic_launcher;

import static com.woofilee.ifmo.android.homework.service.R.id.coordinator;
import static com.woofilee.ifmo.android.homework.service.R.id.blur_image_view;
import static com.woofilee.ifmo.android.homework.service.R.id.image_view;
import static com.woofilee.ifmo.android.homework.service.R.id.progress;
import static com.woofilee.ifmo.android.homework.service.R.id.button_refresh;
import static com.woofilee.ifmo.android.homework.service.R.id.button_start;
import static com.woofilee.ifmo.android.homework.service.R.id.button_stop;

import static com.woofilee.ifmo.android.homework.service.R.string.service_started;
import static com.woofilee.ifmo.android.homework.service.R.string.image_updated;
import static com.woofilee.ifmo.android.homework.service.R.string.service_stopped;
import static com.woofilee.ifmo.android.homework.service.R.string.no_available_image;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Main screen
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private static final int IMAGE_SAMPLE_SIZE = 1024;
    private static final float BLUR_IMAGE_SCALE_FACTOR = 0.25f;
    private static final float BLUR_IMAGE_RADIUS_FACTOR = 7.5f;

    private ImageReceiver receiver;

    private CoordinatorLayout coordinatorLayout;

    private ImageView imageView;
    private ImageView blurImageView;
    private ProgressBar progressBar;
    private Button buttonRefresh;
    private Button buttonStart;
    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(coordinator);
        imageView = (ImageView) findViewById(image_view);
        blurImageView = (ImageView) findViewById(blur_image_view);
        progressBar = (ProgressBar) findViewById(progress);
        buttonRefresh = (Button) findViewById(button_refresh);
        buttonStart = (Button) findViewById(button_start);
        buttonStop = (Button) findViewById(button_stop);

        if (ServiceUtils.isServiceRunning(ImageService.class, getApplicationContext())) {
            buttonRefresh.setVisibility(View.VISIBLE);
            buttonStop.setVisibility(View.VISIBLE);
        } else {
            buttonStart.setVisibility(View.VISIBLE);
        }

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                startService(new Intent(getApplicationContext(), ImageService.class).setAction(
                        ImageService.ACTION_DOWNLOAD));
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                startService(new Intent(getApplicationContext(), ImageService.class));
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                stopService(new Intent(getApplicationContext(), ImageService.class));
            }
        });

        receiver = new ImageReceiver() {
            @Override
            public void onServiceStarted() {
                buttonStart.setVisibility(View.GONE);

                buttonRefresh.setVisibility(View.VISIBLE);
                buttonStop.setVisibility(View.VISIBLE);
                buttonStop.setEnabled(true);

                Snackbar.make(coordinatorLayout, service_started, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFinishLoading() {
                updateImage();
                buttonRefresh.setEnabled(true);
                Snackbar.make(coordinatorLayout, image_updated, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onServiceStopped() {
                buttonRefresh.setVisibility(View.GONE);
                buttonStop.setVisibility(View.GONE);

                buttonStart.setVisibility(View.VISIBLE);
                buttonStart.setEnabled(true);

                Snackbar.make(coordinatorLayout, service_stopped, Snackbar.LENGTH_LONG).show();
            }
        };

        registerReceiver(receiver, new IntentFilter(ImageReceiver.BROADCAST_ACTION));
        updateImage();
    }

    private void updateImage() {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) { // Async image reading from file + rendering blur image
                Bitmap bitmap;

                try {
                    FileInputStream bis = FileUtils.getInputFile(
                            ImageService.IMAGE_FILEPATH,
                            getApplicationContext()
                    );

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    BitmapFactory.decodeStream(bis, null, options);

                    options.inSampleSize = calculateSampleSize(
                            options, IMAGE_SAMPLE_SIZE, IMAGE_SAMPLE_SIZE);
                    options.inJustDecodeBounds = false;

                    bis = FileUtils.getInputFile(
                            ImageService.IMAGE_FILEPATH,
                            getApplicationContext()
                    );

                    bitmap = BitmapFactory.decodeStream(bis, null, options);

                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    bitmap = BitmapFactory.decodeResource(getResources(), ic_launcher);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(coordinatorLayout, no_available_image,
                                    Snackbar.LENGTH_INDEFINITE).show();
                        }
                    });
                }

                RenderScript rs = RenderScript.create(getApplicationContext());

                final Bitmap blurBitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        Math.round(bitmap.getWidth() * BLUR_IMAGE_SCALE_FACTOR),
                        Math.round(bitmap.getHeight() * BLUR_IMAGE_SCALE_FACTOR),
                        false
                );

                final Allocation input = Allocation.createFromBitmap(rs, blurBitmap);
                final Allocation output = Allocation.createTyped(rs, input.getType());

                final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(
                        rs,
                        Element.U8_4(rs)
                );

                script.setRadius(BLUR_IMAGE_RADIUS_FACTOR);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(blurBitmap);

                final Bitmap finalBitmap = bitmap;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalBitmap);
                        blurImageView.setImageBitmap(blurBitmap);
                        progressBar.setVisibility(View.GONE);
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
