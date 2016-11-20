package com.woofilee.ifmo.android.homework.service.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.woofilee.ifmo.android.homework.service.R;
import com.woofilee.ifmo.android.homework.service.receivers.RedditServiceReceiver;
import com.woofilee.ifmo.android.homework.service.services.RedditService;
import com.woofilee.ifmo.android.homework.service.utils.ImageUtils;
import com.woofilee.ifmo.android.homework.service.utils.ServiceUtils;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private Intent serviceIntent;
    private RedditServiceReceiver receiver;

    private ImageView imageView;
    private ProgressBar progressBar;
    private Button buttonStart;
    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(getApplicationContext(), RedditService.class);

        imageView = (ImageView) findViewById(R.id.image_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        buttonStart = (Button) findViewById(R.id.button_start);
        buttonStop = (Button) findViewById(R.id.button_stop);

        if (ServiceUtils.isServiceRunning(RedditService.class, getApplicationContext())) {
            buttonStop.setEnabled(true);
        } else {
            buttonStart.setEnabled(true);
        }

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setEnabled(false);
                startService(serviceIntent);
                buttonStop.setEnabled(true);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                stopService(serviceIntent);
                buttonStart.setEnabled(true);
            }
        });

        receiver = new RedditServiceReceiver() {
            @Override
            public void onFinishLoading() {
                try {
                    imageView.setImageBitmap(ImageUtils.loadImage(
                            RedditService.IMAGE_FILENAME,
                            RedditService.IMAGE_EXTENSION,
                            getApplicationContext()
                    ));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(RedditServiceReceiver.BROADCAST_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
