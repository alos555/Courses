package com.example.courses.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.data.model.Result;
import com.example.courses.R;
import com.example.courses.data.model.LogRegRequest;
import com.example.courses.data.source.SupabaseClient;
import com.example.courses.ui.main.MainActivity;
import com.example.courses.ui.pin.SetPinActivity;
import com.example.courses.ui.register.RegisterActivity;
import com.example.courses.ui.resetpassword.ResetPasswordActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private SupabaseClient supabaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_log);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        findViewById(R.id.btnLogin);
        TextView tvGoToRegister = findViewById(R.id.tvGoToRegister);
        TextView tvResetPassword = findViewById(R.id.reset_password);
        Button btnLogin = findViewById(R.id.btnLogin);

        supabaseClient = new SupabaseClient();
    }

    private void setupListeners() {
        // Переход к регистрации
        findViewById(R.id.tvGoToRegister).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        // Переход к восстановлению пароля
        findViewById(R.id.reset_password).setOnClickListener(v ->
                startActivity(new Intent(this, ResetPasswordActivity.class)));

        // Обработка входа
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            supabaseClient.signIn(new LogRegRequest(email, password),
                    new SupabaseClient.AuthCallback() {
                        @Override
                        public void onResult(Result<String> result) {
                            runOnUiThread(() -> {
                                if (result.isSuccess()) {
                                    Toast.makeText(LoginActivity.this, "Вы вошли", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, SetPinActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "Ошибка: " + result.getError().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
        });
    }
}