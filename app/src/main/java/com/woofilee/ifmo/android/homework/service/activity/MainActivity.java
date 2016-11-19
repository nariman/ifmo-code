package com.woofilee.ifmo.android.homework.service.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.woofilee.ifmo.android.homework.service.R;
import com.woofilee.ifmo.android.homework.service.loaders.ImageDownloader;
import com.woofilee.ifmo.android.homework.service.loaders.RedditLinksLoader;
import com.woofilee.ifmo.android.homework.service.utils.ImageUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private ImageView imageView;
    private ProgressBar progressBar;
    private Button buttonStart;
    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        buttonStart = (Button) findViewById(R.id.button_start);
        buttonStop = (Button) findViewById(R.id.button_stop);

        RedditLinksLoader.load(new RedditLinksLoader.OnLinksLoaderListener() {
            @Override
            public void onComplete(JsonReader result) {
                Log.d(TAG, "Woah, completed :)");
                try {
                    result.beginObject();
                    Log.d(TAG, result.nextName() + ", " + String.valueOf(result.nextString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                Log.e(TAG, "Actually, error :(");
            }
        });

        ImageDownloader.download(
                "https://i.reddituploads.com/ede98e2d860b43d885732abcf8f74036?fit=max&h=1536&w=1536&s=0dc5faee8bb8042f764e52d4815222dd",
                new ImageDownloader.OnImageLoaderListener() {
                    @Override
                    public void onComplete(Bitmap result) {
                        try {
                            ImageUtils.saveImage(result, "hell", "png", getApplicationContext());
                            Bitmap bitmap = ImageUtils.loadImage("hell", "png", getApplicationContext());
                            imageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "Actually, error :(");
                    }

                    @Override
                    public void onProgressChange(int percent) {
                        Log.d(TAG, String.valueOf(percent) + "%");
                    }
                }
        );
    }
}
