package com.example.courses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class onBoardingSecond extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_second);

        // Находим кнопки по ID
        Button btnSignIn = findViewById(R.id.btn_second_sin);
        Button btnLogin = findViewById(R.id.btn_second_log);

        // Обработка нажатия на "Sign in"
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход к экрану регистрации
                Intent intent = new Intent(onBoardingSecond.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Обработка нажатия на "Log in"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход к экрану входа
                Intent intent = new Intent(onBoardingSecond.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}