package com.example.courses.ui.resetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.data.source.SupabaseClient;

import java.io.IOException;

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

            supabaseClient.sendPasswordResetOtp(email, new SupabaseClient.supa_callback() {
                        @Override
                        public void onFailure(IOException e) {
                            runOnUiThread(()->{
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            });
                        }

                        @Override
                        public void onResponse(String responseBody) {
                            runOnUiThread(()->{
                                Toast.makeText(getApplicationContext(), "Код отправлен", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), EnterOtpActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            });
                        }
                    }
            );
        });
    }
}
