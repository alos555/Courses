package com.example.courses;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.ui.onboarding.onBoardingFirst;
import com.example.courses.ui.pin.EnterPinActivity;
import com.example.courses.ui.pin.PinPreferences;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private PinPreferences pinPreferences;

    private Handler handler = new Handler(Looper.getMainLooper());
    private int progressStatus = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Инициализация SharedPreferences
        pinPreferences = new PinPreferences(this);

        progressBar = findViewById(R.id.progress_load);

        simulateLoading();
    }

    private void simulateLoading() {
        new Thread(() -> {
            while (progressStatus <= 100) {
                progressStatus++;
                progressBar.setProgress(progressStatus);

                if (progressStatus == 100) {
                    handler.post(() -> {
                        if (pinPreferences.isPinSet()) {
                            // Переход к вводу ПИН
                            startActivity(new Intent(SplashScreen.this, EnterPinActivity.class));
                            finish();
                        } else {
                            // Переход к онбордингу
                            startActivity(new Intent(SplashScreen.this, onBoardingFirst.class));
                            finish();
                        }
                    });
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
