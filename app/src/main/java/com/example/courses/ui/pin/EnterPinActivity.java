package com.example.courses.ui.pin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.data.model.DataBinding;
import com.example.courses.data.source.SupabaseClient;
import com.example.courses.ui.login.LoginActivity;
import com.example.courses.ui.main.MainActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.ui.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EnterPinActivity extends AppCompatActivity {

    private StringBuilder pinBuilder = new StringBuilder();
    private static final int PIN_LENGTH = 4;

    private ImageView pin1, pin2, pin3, pin4;
    private PinPreferences pinPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_pin);

        pinPreferences = new PinPreferences(this);
        initViews();
        setupDigitButtons();
    }

    private void initViews() {
        pin1 = findViewById(R.id.pin1);
        pin2 = findViewById(R.id.pin2);
        pin3 = findViewById(R.id.pin3);
        pin4 = findViewById(R.id.pin4);
    }

    private void setupDigitButtons() {
        View.OnClickListener digitListener = v -> {
            Button btn = (Button) v;
            String digit = btn.getText().toString();

            if (pinBuilder.length() < PIN_LENGTH) {
                pinBuilder.append(digit);
                updatePinDots();
                if (pinBuilder.length() == PIN_LENGTH) {
                    checkPin();
                }
            }
        };

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (pinBuilder.length() > 0) {
                pinBuilder.deleteCharAt(pinBuilder.length() - 1);
                updatePinDots();
            }
        });

        // Привязка кнопок
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

        findViewById(R.id.btnEnterPin).setOnClickListener(v -> {
            if (pinBuilder.length() != PIN_LENGTH) {
                Toast.makeText(this, "Введите корректный пин", Toast.LENGTH_SHORT).show();
                return;
            }
            checkPin();
        });

        findViewById(R.id.btnForgotPin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void updatePinDots() {
        pin1.setImageResource(pinBuilder.length() >= 1 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin2.setImageResource(pinBuilder.length() >= 2 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin3.setImageResource(pinBuilder.length() >= 3 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
        pin4.setImageResource(pinBuilder.length() >= 4 ? R.drawable.circle_pin_filled : R.drawable.circle_pin);
    }

    private void checkPin() {
        String inputPin = pinBuilder.toString();
        String savedPin = pinPreferences.getPin();

        if (savedPin != null && savedPin.equals(inputPin)) {
            login();
        } else {
            Toast.makeText(this, "Неверный пин", Toast.LENGTH_SHORT).show();
            pinBuilder.setLength(0);
            resetPinDots();
        }
    }

    private void resetPinDots() {
        pin1.setImageResource(R.drawable.circle_pin);
        pin2.setImageResource(R.drawable.circle_pin);
        pin3.setImageResource(R.drawable.circle_pin);
        pin4.setImageResource(R.drawable.circle_pin);
    }

    public void login(){
        DataBinding d = new DataBinding(getApplicationContext());
        SupabaseClient s = new SupabaseClient();
        s.login(d.getEmail(), d.getPassword(), new SupabaseClient.supa_callback() {
            @Override
            public void onFailure(IOException e) {
                login();
            }

            @Override
            public void onResponse(String responseBody) {

                try {
                    JSONObject object = new JSONObject(responseBody);
                    DataBinding dataBinding = new DataBinding(getApplicationContext());
                    dataBinding.setBearer(object.getString("access_token"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                runOnUiThread(()->{
                    startActivity(new Intent(EnterPinActivity.this, MainActivity.class));
                    finish();
                });
            }
        });
    }
}