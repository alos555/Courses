package com.example.courses.ui.resetpassword;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courses.R;
import com.example.courses.data.source.SupabaseClient;
import com.example.courses.data.model.Result;
import com.example.courses.ui.login.LoginActivity;

public class SetNewPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnSetPassword;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        email = getIntent().getStringExtra("email");

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSetPassword = findViewById(R.id.btnSetPassword);

        btnSetPassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            SupabaseClient client = new SupabaseClient();
            client.updatePassword(email, newPassword, result -> {
                runOnUiThread(() -> {
                    if (result.isSuccess()) {
                        Toast.makeText(this, "Пароль изменён", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Ошибка: " + result.getError().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            });
        });
    }
}