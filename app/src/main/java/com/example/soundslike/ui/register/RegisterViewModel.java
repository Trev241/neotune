package com.example.soundslike.ui.register;

import android.app.Application;
import android.text.TextUtils;
import android.util.Patterns; // Import Patterns for email validation
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.soundslike.data.network.responses.RegisterResponse;
import com.example.soundslike.data.repository.AuthRepository;

public class RegisterViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    // Emits true on successful registration
    private final MutableLiveData<Boolean> _registrationSuccess = new MutableLiveData<>(false);
    public LiveData<Boolean> getRegistrationSuccess() { return _registrationSuccess; }

    private LiveData<RegisterResponse> currentRegisterSource = null;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public void register(String email, String password) {
        // Basic Validation
        if (!isValidEmail(email)) {
            _errorMessage.setValue("Invalid email address.");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) { // Example: Minimum password length
            _errorMessage.setValue("Password must be at least 6 characters.");
            return;
        }

        if (_isLoading.getValue() != null && _isLoading.getValue()) {
            return; // Avoid multiple requests
        }

        _isLoading.setValue(true);
        _errorMessage.setValue(null);
        _registrationSuccess.setValue(false); // Reset success state

        if (currentRegisterSource != null) {
            // Handle potential previous observers if needed, though observeForever is tricky
        }

        currentRegisterSource = authRepository.register(email, password);

        // Observe the result
        currentRegisterSource.observeForever(registerResponse -> {
            _isLoading.setValue(false);
            if (registerResponse != null) {
                _registrationSuccess.setValue(true); // Signal success
            } else {
                // Registration failed (network or API error)
                _errorMessage.setValue("Registration failed. Email might already be taken.");
                _registrationSuccess.setValue(false);
            }
            // Clean up observer if needed (see comment in LoginViewModel)
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void clearErrorMessage() {
        _errorMessage.setValue(null);
    }

    @Override
    protected void onCleared() {
        // Clean up if necessary
        super.onCleared();
    }
}