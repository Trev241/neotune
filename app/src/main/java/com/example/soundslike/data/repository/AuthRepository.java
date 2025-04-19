package com.example.soundslike.data.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.soundslike.data.network.ApiService;
import com.example.soundslike.data.network.RetrofitClient;
import com.example.soundslike.data.network.requests.RegisterRequest; // Import RegisterRequest
import com.example.soundslike.data.network.responses.LoginResponse;
import com.example.soundslike.data.network.responses.RegisterResponse; // Import RegisterResponse
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private static final String TAG = "AuthRepository";
    private final ApiService apiService;

    public AuthRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Login method (existing)
    public LiveData<LoginResponse> login(String username, String password) {
        MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
        Log.d(TAG, "Attempting login for user: " + username);
        apiService.loginUser(username, password)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.i(TAG, "Login successful for user: " + username);
                            loginResult.setValue(response.body());
                        } else {
                            String errorMsg = "Login failed: " + response.code();
                            if (response.errorBody() != null) { try { errorMsg += " - " + response.errorBody().string(); } catch (Exception e) {}}
                            Log.e(TAG, errorMsg);
                            loginResult.setValue(null);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "Login network error", t);
                        loginResult.setValue(null);
                    }
                });
        return loginResult;
    }

    // --- ADDED: Register Method ---
    public LiveData<RegisterResponse> register(String email, String password) {
        MutableLiveData<RegisterResponse> registerResult = new MutableLiveData<>();
        Log.d(TAG, "Attempting registration for email: " + email);

        RegisterRequest requestBody = new RegisterRequest(email, password);

        apiService.registerUser(requestBody).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // HTTP 201 Created is typical for successful registration
                    Log.i(TAG, "Registration successful for email: " + email + ", ID: " + response.body().getId());
                    registerResult.setValue(response.body());
                } else {
                    // Handle errors (e.g., 422 Validation Error - email already exists?)
                    String errorMsg = "Registration failed: " + response.code();
                    if (response.errorBody() != null) { try { errorMsg += " - " + response.errorBody().string(); } catch (Exception e) {}}
                    Log.e(TAG, errorMsg);
                    registerResult.setValue(null); // Indicate failure
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Registration network error", t);
                registerResult.setValue(null); // Indicate failure
            }
        });
        return registerResult;
    }
    // ----------------------------
}