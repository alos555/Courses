package com.example.courses.data.model;

import android.content.Context;
import android.content.SharedPreferences;

public class DataBinding {

    private SharedPreferences sharedPreferences;

    public DataBinding(Context context) {
        sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
    }

    public String getEmail() {
        return sharedPreferences.getString("email", null);
    }
    public void setEmail(String email) { sharedPreferences.edit().putString("email", email).apply(); }
    public String getName() {
        return sharedPreferences.getString("name", null);
    }
    public void setName(String email) { sharedPreferences.edit().putString("name", email).apply(); }
    public String getPassword() { return sharedPreferences.getString("password", null); }
    public void setPassword(String password) { sharedPreferences.edit().putString("password", password).apply(); }
    public String getBearerToken() { return sharedPreferences.getString("bearer_token", null); }
    public void setBearer(String Token){ sharedPreferences.edit().putString("bearer_token", Token).apply(); }
    public String getUserId() { return sharedPreferences.getString("user_id", null); }
    public void setUserId(String id) { sharedPreferences.edit().putString("user_id", id).apply(); }
    public String getAvatar() { return sharedPreferences.getString("avatar", null); }
    public void setAvatar(String id) { sharedPreferences.edit().putString("avatar", id).apply(); }
    public void savePin(String pin) {sharedPreferences.edit().putString("pin", pin).apply();}
    public String getPin() { return sharedPreferences.getString("pin", null); }
    public boolean isPinSet() { return getPin() != null && !getPin().isEmpty(); }
    public void onboard(){sharedPreferences.edit().putString("status", "bookAll").apply();}
    public void log(){ sharedPreferences.edit().putString("status", "logined").apply();}
    public void unlog() { sharedPreferences.edit().putString("status", "unlogined").apply();}
    public String getStatus(){
        return sharedPreferences.getString("status", null);
    }
}
