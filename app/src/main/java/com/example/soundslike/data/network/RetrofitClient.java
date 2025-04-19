package com.example.soundslike.data.network;

import android.content.Context; // Import Context
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.media3.ui.BuildConfig;
import com.example.soundslike.util.TokenManager; // Import TokenManager
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static TokenManager tokenManager = null; // Static instance

    // Initialize TokenManager once (pass application context)
    public static void initialize(Context context) {
        if (tokenManager == null) {
            tokenManager = new TokenManager(context.getApplicationContext());
        }
    }

    // --- Authentication Interceptor ---
    private static class AuthInterceptor implements Interceptor {

        // No need to pass token here anymore
        AuthInterceptor() {}

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request originalRequest = chain.request();

            // Exclude login/register/stream requests from adding auth header
            String path = originalRequest.url().encodedPath();
            if (path.contains("/auth/login") || path.contains("/auth/register") || path.contains("/songs/stream")) {
                return chain.proceed(originalRequest);
            }

            // --- Get token dynamically ---
            String token = (tokenManager != null) ? tokenManager.getToken() : null;
            // ---------------------------

            Request.Builder builder = originalRequest.newBuilder();
            if (token != null && !token.isEmpty()) {
                // Add the "Bearer " prefix
                builder.header("Authorization", "Bearer " + token);
                Log.v("RetrofitClient", "Adding Auth header to request for: " + path); // Verbose log
            } else {
                Log.w("RetrofitClient", "No token found, request to " + path + " will be unauthenticated.");
            }

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }
    // --------------------------------

    public static ApiService getApiService() {
        if (retrofit == null) {
            // Ensure TokenManager is initialized (needs context)
            // This is tricky here. Ideally, initialize() is called from Application.onCreate()
            if (tokenManager == null) {
                // This is a fallback, might not work if called before Application context is ready
                Log.e("RetrofitClient", "TokenManager not initialized! Call RetrofitClient.initialize(context) first.");
                // throw new IllegalStateException("TokenManager not initialized!");
            }


            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor()) // Use the updated interceptor
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}