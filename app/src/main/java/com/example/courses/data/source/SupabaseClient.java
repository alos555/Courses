package com.example.courses.data.source;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.courses.data.model.DataBinding;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SupabaseClient {

    private static final String SUPABASE_URL = "https://rcyxhnvfyfgtlnombezh.supabase.co/";
    public static String REST_PATH = "rest/v1/";
    public static String AUTH_PATH = "auth/v1/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJjeXhobnZmeWZndGxub21iZXpoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTA1NzcxMTgsImV4cCI6MjA2NjE1MzExOH0.QrtyiqiY7PUPKDN2JhdDZCbZBZMoK6idLLbjzaYq78I";

    private OkHttpClient client = new OkHttpClient();

    public void registr(String email, String password, final supa_callback callback){
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());

        Request request = new Request.Builder()
                .url(SUPABASE_URL + AUTH_PATH + "signup")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    callback.onResponse(responseBody);
                } else {
                    callback.onFailure(new IOException("Ошибка сервера: " + response));
                }
            }
        });
    }

    public void updateProfile(Context context, String name, final supa_callback callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("full_name", name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json")
        );

        DataBinding d = new DataBinding(context);

        Request request = new Request.Builder()
                .url(SUPABASE_URL + REST_PATH + "profiles?id=eq." + d.getUserId())
                .method("PATCH", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + d.getBearerToken())
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    callback.onResponse(responseBody);
                } else {
                    callback.onFailure(new IOException("Ошибка сервера: " + response.code()));
                }
            }
        });
    }

    public void login(String email, String password, supa_callback callback) {
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());

        Request request = new Request.Builder()
                .url(SUPABASE_URL + AUTH_PATH + "token?grant_type=password")
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    callback.onResponse(body);
                } else {
                    callback.onFailure(new IOException("Ошибка сервера: " + response.code()));
                }
            }
        });
    }

    public void sendPasswordResetOtp(String email, final supa_callback callback) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);

            RequestBody body = RequestBody.create(
                    jsonObject.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(SUPABASE_URL + AUTH_PATH + "recover")
                    .post(body)
                    .addHeader("apikey", API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onFailure(e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        callback.onResponse(response.body().string());
                    } else {
                        callback.onFailure(new IOException("Ошибка сервера: " + response.code()));
                    }
                }
            });
        } catch (Exception e) {
            callback.onFailure(new IOException("Ошибка создания запроса: " + e.getMessage()));
        }
    }

    public void verifyPasswordResetOtp(Context context, String email, String otp, final supa_callback callback) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("token", otp);
            jsonObject.put("type", "recovery");

            RequestBody body = RequestBody.create(
                    jsonObject.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(SUPABASE_URL + AUTH_PATH + "verify")
                    .post(body)
                    .addHeader("apikey", API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onFailure(e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBodyString;
                        try (ResponseBody responseBody = response.body()) {
                            if (responseBody == null) {
                                callback.onFailure(new IOException("Пустой ответ от сервера"));
                                return;
                            }

                            responseBodyString = responseBody.string();
                        }

                        try {
                            JsonElement jsonElement = new JsonParser().parse(responseBodyString);
                            Gson gson = new Gson();
                            String json = gson.toJson(jsonElement);
                            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

                            String accessToken = jsonObject.get("access_token").getAsString();

                            DataBinding d = new DataBinding(context);

                            d.setBearer(accessToken);
                        } catch (Exception e) {
                            Log.e("JSON_PARSE_ERROR", "Ошибка при парсинге JSON", e);
                        }

                        callback.onResponse(responseBodyString);
                    } else {
                        callback.onFailure(new IOException("Ошибка сервера: " + response.code()));
                    }
                }
            });
        } catch (Exception e) {
            callback.onFailure(new IOException("Ошибка создания запроса: " + e.getMessage()));
        }
    }

    public void updateUserPassword(Context context, String newPassword, final supa_callback callback) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", newPassword);

            RequestBody body = RequestBody.create(
                    jsonObject.toString(),
                    MediaType.parse("application/json")
            );

            DataBinding d = new DataBinding(context);

            Request request = new Request.Builder()
                    .url(SUPABASE_URL + AUTH_PATH + "user")
                    .method("PUT", body)
                    .addHeader("apikey", API_KEY)
                    .addHeader("Authorization", "Bearer " + d.getBearerToken())
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onFailure(e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        callback.onResponse(response.body().string());
                    } else {
                        callback.onFailure(new IOException("Ошибка сервера: " + response.code()));
                    }
                }
            });
        } catch (Exception e) {
            callback.onFailure(new IOException("Ошибка создания запроса: " + e.getMessage()));
        }
    }

    public void changeEmail(Context context, String newEmail, supa_callback callback) {
        JSONObject jsonBody = new JSONObject();
        DataBinding d = new DataBinding(context);
        try {
            jsonBody.put("user_id", d.getUserId());
            jsonBody.put("new_email", newEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(SUPABASE_URL + REST_PATH +"rpc/change_user_email")
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + d.getBearerToken())
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onResponse(response.body().string());
                } else {
                    callback.onFailure(new IOException("Ошибка сервера: " + response.code()));
                }
            }
        });
    }

    public void getCourses(Context context, final supa_callback callback) {
        DataBinding d = new DataBinding(context);

        Request request = new Request.Builder()
                .url(SUPABASE_URL + REST_PATH + "courses?select=*")
                .get()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + d.getBearerToken())
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    callback.onResponse(body);
                } else {
                    callback.onFailure(new IOException("Ошибка"));
                }
            }
        });
    }

    public void getLectures(Context context, String courseId, final supa_callback callback) {

        DataBinding d = new DataBinding(context);

        Request request = new Request.Builder()
                .url(SUPABASE_URL + REST_PATH + "lectures?select=*&course_id=eq." + courseId)
                .get()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + d.getBearerToken())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    callback.onResponse(body);
                } else {
                    callback.onFailure(new IOException("Ошибка"));
                }
            }
        });
    }

    public interface supa_callback {
        void onFailure(IOException e);
        void onResponse(String responseBody);
    }
}