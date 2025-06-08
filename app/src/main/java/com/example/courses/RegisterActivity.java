    package com.example.courses;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    public class RegisterActivity extends AppCompatActivity {

        private EditText etEmail, etPassword;
        private CheckBox checkBox;
        private SharedPreferences sharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.form_reg); // Убедись, что файл называется activity_register.xml
    
            // Находим элементы по ID из XML
            etEmail = findViewById(R.id.etRegisterEmail);
            etPassword = findViewById(R.id.etRegisterPassword);
            checkBox = findViewById(R.id.checkBox);
            Button btnRegister = findViewById(R.id.btn_second_sin);
            TextView tvGoToLogin = findViewById(R.id.tvSignUp);

            // Подключаем SharedPreferences для хранения логина и пароля
            sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

            // Обработка нажатия на кнопку "Зарегистрироваться"
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    boolean isAgreed = checkBox.isChecked();

                    // Проверка заполненности полей
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Проверка согласия с условиями
                    if (!isAgreed) {
                        Toast.makeText(RegisterActivity.this, "Подтвердите согласие с условиями", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Сохраняем email и пароль в SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(email, password);
                    editor.apply();

                    // Уведомление и переход на экран входа
                    Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish(); // Закрываем текущую активность
                }
            });


            // Переход к экрану входа при нажатии на ссылку
            tvGoToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish(); // Закрываем RegisterActivity
                }
            });
        }
    }
