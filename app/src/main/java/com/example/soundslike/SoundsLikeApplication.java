package com.example.soundslike;

import android.app.Application;
import com.example.soundslike.data.network.RetrofitClient; // Import

public class SoundsLikeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize singletons here
        RetrofitClient.initialize(this);
    }
}