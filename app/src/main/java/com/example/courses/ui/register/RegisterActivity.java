package com.example.courses.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.data.model.LogRegRequest;
import com.example.courses.data.source.SupabaseClient;
import com.example.courses.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox checkBox;
    private SupabaseClient supabaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_reg);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        checkBox = findViewById(R.id.checkBox);
        findViewById(R.id.btn_second_sin);
        supabaseClient = new SupabaseClient();
    }

    private void setupListeners() {
        findViewById(R.id.btn_second_sin).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkBox.isChecked()) {
                Toast.makeText(this, "Примите условия использования", Toast.LENGTH_SHORT).show();
                return;
            }

            supabaseClient.signUp(new LogRegRequest(email, password), result -> {
                runOnUiThread(() -> {
                    if (result.isSuccess()) {
                        Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        findViewById(R.id.tvSignUp).setOnClickListener(v ->
                finish());
    }
}