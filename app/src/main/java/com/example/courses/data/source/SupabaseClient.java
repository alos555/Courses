package com.example.courses.data.source;

import androidx.annotation.NonNull;

import com.example.courses.data.model.LogRegRequest;
import com.example.courses.data.model.Result;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseClient {

    private static final String SUPABASE_URL = "https://grptvhcnjbfwrdrasxwj.supabase.co";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdycHR2aGNuamJmd3JkcmFzeHdqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDk3MTc1MDgsImV4cCI6MjA2NTI5MzUwOH0.RIT_0WNIPQ2ZJCqnuOoKjPVhfJsqz4E1iLHoLUgbgj4";

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    public void signIn(LogRegRequest request, AuthCallback callback) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, gson.toJson(request));

        Request requestOkhttp = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/token?grant_type=password")
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(requestOkhttp).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResult(Result.error(e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    callback.onResult(Result.success(body));
                } else {
                    callback.onResult(Result.error(new IOException("Ошибка авторизации")));
                }
            }
        });
    }

    public void signUp(LogRegRequest request, AuthCallback callback) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, gson.toJson(request));

        Request requestOkhttp = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/register")
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(requestOkhttp).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResult(Result.error(e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onResult(Result.success("Регистрация успешна"));
                } else {
                    callback.onResult(Result.error(new IOException("Ошибка регистрации")));
                }
            }
        });
    }

    public void recoverPassword(String email, AuthCallback callback) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);

        MediaType mediaType = MediaType.get("application/json");
        RequestBody body = RequestBody.create(mediaType, json.toString());

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/recover")
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResult(Result.error(e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    callback.onResult(Result.success("Ссылка отправлена"));
                } else {
                    callback.onResult(Result.error(new IOException("Ошибка восстановления пароля")));
                }
            }
        });
    }
    public void sendOtp(String email, AuthCallback callback) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/otp")
                .post(RequestBody.create(json.toString(), MediaType.get("application/json")))
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResult(Result.error(e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    callback.onResult(Result.success("Код отправлен"));
                } else {
                    callback.onResult(Result.error(new IOException("Ошибка отправки кода")));
                }
            }
        });
    }

    public void verifyOtp(String email, String code, AuthCallback callback) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("code", code);

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/verify")
                .post(RequestBody.create(json.toString(), MediaType.get("application/json")))
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResult(Result.error(e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    callback.onResult(Result.success("Код верен"));
                } else {
                    callback.onResult(Result.error(new IOException("Неверный код")));
                }
            }
        });
    }

    public void updatePassword(String email, String newPassword, AuthCallback callback) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", newPassword);

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/update-password")
                .post(RequestBody.create(json.toString(), MediaType.get("application/json")))
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResult(Result.error(e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    callback.onResult(Result.success("Пароль изменён"));
                } else {
                    callback.onResult(Result.error(new IOException("Ошибка изменения пароля")));
                }
            }
        });
    }
    public interface AuthCallback {
        void onResult(Result<String> result);
    }
}