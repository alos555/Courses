package com.example.courses.ui.resetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.data.source.SupabaseClient;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private SupabaseClient supabaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etResetEmail);
        supabaseClient = new SupabaseClient();
    }

    private void setupListeners() {
        findViewById(R.id.btnSendLink).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();
                return;
            }

            supabaseClient.sendOtp(email, result -> {
                runOnUiThread(() -> {
                    if (result.isSuccess()) {
                        Toast.makeText(this, "Код отправлен", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, EnterOtpActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Ошибка: " + result.getError().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            });
        });
    }
}
