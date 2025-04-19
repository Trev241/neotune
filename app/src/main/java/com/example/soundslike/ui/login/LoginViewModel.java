package com.example.soundslike.ui.login;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.soundslike.data.network.responses.LoginResponse;
import com.example.soundslike.data.repository.AuthRepository;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    // Emits the access token on successful login
    private final MutableLiveData<String> _loginSuccessToken = new MutableLiveData<>(null);
    public LiveData<String> getLoginSuccessToken() { return _loginSuccessToken; }

    private LiveData<LoginResponse> currentLoginSource = null;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public void login(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            _errorMessage.setValue("Username and password cannot be empty.");
            return;
        }

        // Avoid multiple simultaneous login attempts
        if (_isLoading.getValue() != null && _isLoading.getValue()) {
            return;
        }

        _isLoading.setValue(true);
        _errorMessage.setValue(null); // Clear previous errors
        _loginSuccessToken.setValue(null); // Reset success state

        // Remove observer from previous source if any
        if (currentLoginSource != null) {
            // We don't need a Mediator here if we just observe once
            // _loginResultMediator.removeSource(currentLoginSource);
        }

        currentLoginSource = authRepository.login(username, password);

        // Observe the result from the repository
        currentLoginSource.observeForever(loginResponse -> {
            // We only need the first result, but observeForever needs manual removal later
            // A better approach might use SingleLiveEvent or coroutines/flows

            _isLoading.setValue(false); // Loading finished
            if (loginResponse != null && loginResponse.getAccessToken() != null) {
                _loginSuccessToken.setValue(loginResponse.getAccessToken());
            } else {
                // Login failed (either network error or API error handled in repo)
                _errorMessage.setValue("Login failed. Please check credentials.");
                _loginSuccessToken.setValue(null); // Ensure success state is null
            }
            // Clean up observer if needed, though observeForever is tricky here without lifecycle
            // If this ViewModel survives configuration changes, this observer might fire again.
            // Consider using Transformations.switchMap or coroutines for cleaner one-shot observation.
        });
    }

    // Helper to clear error message after it's shown
    public void clearErrorMessage() {
        _errorMessage.setValue(null);
    }

    @Override
    protected void onCleared() {
        // Clean up if necessary, though observeForever makes this complex
        super.onCleared();
    }
}