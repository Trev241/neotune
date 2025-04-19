package com.example.soundslike.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.soundslike.MainActivity;
import com.example.soundslike.R;
import com.example.soundslike.ui.register.RegisterActivity; // Import RegisterActivity
import com.example.soundslike.util.TokenManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private TokenManager tokenManager;

    private TextInputLayout usernameInputLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenManager = new TokenManager(this);

        // Check if already logged in BEFORE setting content view
        if (tokenManager.hasToken()) {
            navigateToMainApp();
            return; // Important: return to prevent rest of onCreate running
        }

        setContentView(R.layout.activity_login); // Set content view only if not logged in

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Find Views
        usernameInputLayout = findViewById(R.id.username_input_layout);
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordInputLayout = findViewById(R.id.password_input_layout);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.login_progress_bar);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());

        // --- Updated Register Link Listener ---
        findViewById(R.id.register_link).setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });
        // ------------------------------------
    }

    private void attemptLogin() {
        String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

        boolean valid = true;
        if (username.isEmpty()) {
            usernameInputLayout.setError("Username required");
            valid = false;
        } else {
            usernameInputLayout.setError(null);
        }
        if (password.isEmpty()) {
            passwordInputLayout.setError("Password required");
            valid = false;
        } else {
            passwordInputLayout.setError(null);
        }

        if (valid) {
            loginViewModel.login(username, password);
        }
    }

    private void observeViewModel() {
        loginViewModel.isLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            loginButton.setEnabled(!isLoading);
            usernameEditText.setEnabled(!isLoading);
            passwordEditText.setEnabled(!isLoading);
        });

        loginViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                loginViewModel.clearErrorMessage();
            }
        });

        loginViewModel.getLoginSuccessToken().observe(this, accessToken -> {
            if (accessToken != null) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                tokenManager.saveToken(accessToken);
                navigateToMainApp();
            }
        });
    }

    private void navigateToMainApp() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}