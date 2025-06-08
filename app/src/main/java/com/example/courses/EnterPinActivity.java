package com.example.courses;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EnterPinActivity extends AppCompatActivity {

    private EditText etEnterPin;
    private Button btnEnterPin;

    // Визуальные точки пин-кода
    private ImageView pin1, pin2, pin3, pin4;

    private PinPreferences pinPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_pin);

        // Инициализация UI
        etEnterPin = findViewById(R.id.etEnterPin);
        btnEnterPin = findViewById(R.id.btnEnterPin);

        pin1 = findViewById(R.id.pin1);
        pin2 = findViewById(R.id.pin2);
        pin3 = findViewById(R.id.pin3);
        pin4 = findViewById(R.id.pin4);

        pinPreferences = new PinPreferences(this);

        // Сброс точек при старте
        resetPinDots();

        // Наблюдатель за вводом
        etEnterPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePinDots(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Обработка нажатия на кнопку
        btnEnterPin.setOnClickListener(v -> {
            String inputPin = etEnterPin.getText().toString().trim();
            String savedPin = pinPreferences.getPin();

            if (inputPin.length() < 4) {
                Toast.makeText(this, "Введите корректный пин", Toast.LENGTH_SHORT).show();
                return;
            }

            if (savedPin != null && savedPin.equals(inputPin)) {
                startActivity(new Intent(EnterPinActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Неверный пин", Toast.LENGTH_SHORT).show();
                etEnterPin.setText("");
                resetPinDots();
            }
        });
    }

    // Обновление точек в зависимости от введённого количества цифр
    private void updatePinDots(String pin) {
        pin1.setImageResource(pin.length() >= 1 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin2.setImageResource(pin.length() >= 2 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin3.setImageResource(pin.length() >= 3 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin4.setImageResource(pin.length() >= 4 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
    }

    // Сброс всех точек
    private void resetPinDots() {
        pin1.setImageResource(R.drawable.circle_pin);
        pin2.setImageResource(R.drawable.circle_pin);
        pin3.setImageResource(R.drawable.circle_pin);
        pin4.setImageResource(R.drawable.circle_pin);
    }
}