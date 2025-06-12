package com.example.courses.ui.pin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.ui.main.MainActivity;

public class SetPinActivity extends AppCompatActivity {

    private EditText etPin;
    private PinPreferences pinPreferences;

    private ImageView pin1, pin2, pin3, pin4;
    private Button btnSetPin;

    private StringBuilder pinBuilder = new StringBuilder();
    private int maxPinLength = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_pin_activity);

        pinPreferences = new PinPreferences(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etPin = findViewById(R.id.etSetPin);
        etPin.setFocusable(false); // Запрет системной клавиатуры

        pin1 = findViewById(R.id.set_pin1);
        pin2 = findViewById(R.id.set_pin2);
        pin3 = findViewById(R.id.set_pin3);
        pin4 = findViewById(R.id.set_pin4);

        btnSetPin = findViewById(R.id.btnSetPin);
    }

    private void setupListeners() {
        View.OnClickListener digitListener = v -> {
            Button btn = (Button) v;
            String digit = btn.getText().toString();

            if (pinBuilder.length() < maxPinLength) {
                pinBuilder.append(digit);
                updatePinDots();
                checkPinComplete();
            }
        };

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (pinBuilder.length() > 0) {
                pinBuilder.deleteCharAt(pinBuilder.length() - 1);
                updatePinDots();
                btnSetPin.setEnabled(false);
            }
        });

        findViewById(R.id.btn0).setOnClickListener(digitListener);
        findViewById(R.id.btn1).setOnClickListener(digitListener);
        findViewById(R.id.btn2).setOnClickListener(digitListener);
        findViewById(R.id.btn3).setOnClickListener(digitListener);
        findViewById(R.id.btn4).setOnClickListener(digitListener);
        findViewById(R.id.btn5).setOnClickListener(digitListener);
        findViewById(R.id.btn6).setOnClickListener(digitListener);
        findViewById(R.id.btn7).setOnClickListener(digitListener);
        findViewById(R.id.btn8).setOnClickListener(digitListener);
        findViewById(R.id.btn9).setOnClickListener(digitListener);

        findViewById(R.id.btnSetPin).setOnClickListener(v -> {
            if (pinBuilder.length() == maxPinLength) {
                String pin = pinBuilder.toString();
                pinPreferences.savePin(pin);

                Toast.makeText(this, "ПИН установлен", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

    private void updatePinDots() {
        String currentPin = pinBuilder.toString();

        pin1.setImageResource(currentPin.length() >= 1 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin2.setImageResource(currentPin.length() >= 2 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin3.setImageResource(currentPin.length() >= 3 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin4.setImageResource(currentPin.length() >= 4 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);

        etPin.setText(pinBuilder.toString());
    }

    private void checkPinComplete() {
        btnSetPin.setEnabled(pinBuilder.length() == maxPinLength);
    }
}
