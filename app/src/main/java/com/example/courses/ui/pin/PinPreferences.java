package com.example.courses.ui.pin;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class PinPreferences {

    private static final String PREF_NAME = "secure_pin_prefs";
    private static final String KEY_PIN = "user_pin";

    private SharedPreferences sharedPreferences;

    public PinPreferences(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    public void savePin(String pin) {
        sharedPreferences.edit().putString(KEY_PIN, pin).apply();
    }

    public String getPin() {
        return sharedPreferences.getString(KEY_PIN, null);
    }

    public boolean isPinSet() {
        return sharedPreferences.contains(KEY_PIN);
    }

    public void clearPin() {
        sharedPreferences.edit().remove(KEY_PIN).apply();
    }
}