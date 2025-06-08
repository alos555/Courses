package com.example.courses;

import android.content.Context;
import android.content.SharedPreferences;

class PinPreferences {

    private static final String PREF_NAME = "pin_prefs";
    private static final String KEY_PIN = "user_pin";

    private SharedPreferences sharedPreferences;

    public PinPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void savePin(String pin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PIN, pin);
        editor.apply();
    }

    public String getPin() {
        return sharedPreferences.getString(KEY_PIN, null);
    }

    public boolean isPinSet() {
        return sharedPreferences.contains(KEY_PIN);
    }

    public void clearPin() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_PIN);
        editor.apply();
    }
}