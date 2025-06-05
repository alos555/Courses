package com.example.courses;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    private Handler handler = new Handler(Looper.getMainLooper());
    private int progressStatus = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        progressBar = findViewById(R.id.progress_load);

        simulateLoading();



    }

    private void simulateLoading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus <= 100) {
                    progressStatus++;
                    progressBar.setProgress(progressStatus);

                    if (progressStatus == 100) {
                        startActivity(new Intent(getApplicationContext(),
                                onBoardingFirst.class));
                        finish();
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
