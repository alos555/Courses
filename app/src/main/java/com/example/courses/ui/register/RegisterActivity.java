package com.example.courses.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.data.model.DataBinding;
import com.example.courses.data.source.SupabaseClient;
import com.example.courses.ui.pin.SetPinActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfPassword;
    private CheckBox checkBox;
    private SupabaseClient supabaseClient;
    DataBinding d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_reg);
        d = new DataBinding(getApplicationContext());
        initViews();
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etRegisterName);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfPassword = findViewById(R.id.etRegisterConfPassword);
        checkBox = findViewById(R.id.checkBox);
        findViewById(R.id.btn_second_sin);
        supabaseClient = new SupabaseClient();
    }

    private void setupListeners() {
        findViewById(R.id.btn_second_sin).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confPassword = etConfPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || confPassword.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkEmail(email)) {
                Toast.makeText(this, "Неверный формат почты", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkLenght(password)) {
                Toast.makeText(this, "Длина пароля должны быть от 8 до 20 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confPassword)) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkBox.isChecked()) {
                Toast.makeText(this, "Примите условия использования", Toast.LENGTH_SHORT).show();
                return;
            }

            Registration(name, email, password);
        });

        findViewById(R.id.tvSignUp).setOnClickListener(v ->
                finish());
    }

    public static boolean checkLenght(String value) {
        return value.length() <= 20 && value.length() >= 8;
    }

    public static boolean checkEmail(String email) {
        String emailPattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]+$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    public void Registration(String name, String email, String password){
        supabaseClient.registr(email, password, new SupabaseClient.supa_callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(()->{
                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(String responseBody) {
                try {
                    JSONObject object = new JSONObject(responseBody);
                    JSONObject user = object.getJSONObject("user");
                    d.setUserId(user.getString("id"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                d.setEmail(email);
                d.setPassword(password);
                UpdateName(name);
            }
        });
    }

    public void UpdateName(String name){
        supabaseClient.updateProfile(getApplicationContext(), name, new SupabaseClient.supa_callback() {
            @Override
            public void onFailure(IOException e) {
                UpdateName(name);
            }

            @Override
            public void onResponse(String responseBody) {
                d.setName(name);
                runOnUiThread(()->{
                    Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,  SetPinActivity.class));
                    finish();
                });
            }
        });
    }
}