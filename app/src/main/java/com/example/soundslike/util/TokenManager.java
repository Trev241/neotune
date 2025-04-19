package com.example.soundslike.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;

public class TokenManager {

    private static final String PREFS_NAME = "AuthPrefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private final SharedPreferences sharedPreferences;

    // Use application context to avoid leaks
    public TokenManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    @Nullable
    public String getToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public void clearToken() {
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply();
    }

    public boolean hasToken() {
        return getToken() != null;
    }
}