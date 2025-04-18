package com.example.soundslike.data.network;

//import com.example.soundslike.BuildConfig; // Correct import

import java.io.IOException; // Import IOException

import okhttp3.Interceptor; // Import Interceptor
import okhttp3.OkHttpClient;
import okhttp3.Request; // Import Request
import okhttp3.Response; // Import Response
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.annotation.NonNull; // Import NonNull
import androidx.media3.ui.BuildConfig;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    // --- TEMPORARY HARDCODED TOKEN ---
    private static final String TEMP_AUTH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNzQ1NjA4OTg3fQ.QwAVnGLKGd6jFJXLd3Qy8S-0SeSVzk4cap_PXOoWbX8";
    // ---------------------------------

    // --- Authentication Interceptor ---
    private static class AuthInterceptor implements Interceptor {
        private final String token;

        AuthInterceptor(String token) {
            this.token = token;
        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            // Get the original request
            Request originalRequest = chain.request();

            // Don't add auth header to login/register requests (adjust paths if needed)
            if (originalRequest.url().encodedPath().contains("/auth/login") ||
                    originalRequest.url().encodedPath().contains("/auth/register") ||
                    originalRequest.url().encodedPath().contains("/songs/stream")) { // Exclude stream endpoint
                return chain.proceed(originalRequest);
            }

            // Add the Authorization header if a token exists
            Request.Builder builder = originalRequest.newBuilder();
            if (token != null && !token.isEmpty()) {
                builder.header("Authorization", "Bearer " + token);
            }

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }
    // --------------------------------

    public static ApiService getApiService() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            // Create OkHttpClient with BOTH interceptors
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(TEMP_AUTH_TOKEN)) // Add Auth Interceptor FIRST
                    .addInterceptor(logging) // Then add Logging Interceptor
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .client(client) // Use the client with interceptors
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}