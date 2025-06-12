package com.example.courses.ui.resetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.data.source.SupabaseClient;

public class EnterOtpActivity extends AppCompatActivity {

    private StringBuilder enteredCode = new StringBuilder();
    private SupabaseClient supabaseClient;

    private ImageView otp1, otp2, otp3, otp4, otp5, otp6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        // Инициализация клиента
        supabaseClient = new SupabaseClient();

        // Инициализация точек
        initViews();

        // Настройка кнопок
        setupDigitButtons();

        // Обработчик кнопки "Подтвердить"
        findViewById(R.id.btnVerifyOtp).setOnClickListener(v -> {
            if (enteredCode.length() == 6) {
                String email = getIntent().getStringExtra("email");
                verifyOtp(email, enteredCode.toString());
            } else {
                Toast.makeText(this, "Введите 6-значный код", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка удаления
        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (enteredCode.length() > 0) {
                enteredCode.deleteCharAt(enteredCode.length() - 1);
                updateOtpDots();
            }
        });
    }

    private void initViews() {
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
    }

    private void setupDigitButtons() {
        findViewById(R.id.btn0).setOnClickListener(v -> addDigit("0"));
        findViewById(R.id.btn1).setOnClickListener(v -> addDigit("1"));
        findViewById(R.id.btn2).setOnClickListener(v -> addDigit("2"));
        findViewById(R.id.btn3).setOnClickListener(v -> addDigit("3"));
        findViewById(R.id.btn4).setOnClickListener(v -> addDigit("4"));
        findViewById(R.id.btn5).setOnClickListener(v -> addDigit("5"));
        findViewById(R.id.btn6).setOnClickListener(v -> addDigit("6"));
        findViewById(R.id.btn7).setOnClickListener(v -> addDigit("7"));
        findViewById(R.id.btn8).setOnClickListener(v -> addDigit("8"));
        findViewById(R.id.btn9).setOnClickListener(v -> addDigit("9"));
    }

    private void addDigit(String digit) {
        if (enteredCode.length() < 6) {
            enteredCode.append(digit);
            updateOtpDots();
        }
    }

    private void updateOtpDots() {
        otp1.setImageResource(enteredCode.length() >= 1 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        otp2.setImageResource(enteredCode.length() >= 2 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        otp3.setImageResource(enteredCode.length() >= 3 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        otp4.setImageResource(enteredCode.length() >= 4 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        otp5.setImageResource(enteredCode.length() >= 5 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        otp6.setImageResource(enteredCode.length() >= 6 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
    }

    private void verifyOtp(String email, String code) {
        supabaseClient.verifyOtp(email, code, result -> {
            runOnUiThread(() -> {
                if (result.isSuccess()) {
                    Toast.makeText(this, "Код верен", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, SetNewPasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Неверный код", Toast.LENGTH_SHORT).show();
                    enteredCode.setLength(0);
                    updateOtpDots();
                }
            });
        });
    }
}