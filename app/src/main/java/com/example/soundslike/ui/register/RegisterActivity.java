package com.example.soundslike.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.soundslike.R;
import com.example.soundslike.ui.login.LoginActivity; // Import LoginActivity
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;

    private TextInputLayout emailInputLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordEditText;
    private Button registerButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Find Views
        emailInputLayout = findViewById(R.id.email_input_layout);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordInputLayout = findViewById(R.id.password_input_layout_register); // Use correct ID
        passwordEditText = findViewById(R.id.password_edit_text_register); // Use correct ID
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.register_progress_bar);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        registerButton.setOnClickListener(v -> attemptRegistration());

        // Link to go back to Login
        findViewById(R.id.login_link).setOnClickListener(v -> {
            // Simply finish this activity to go back to LoginActivity if it's in the stack
            finish();
            // Or start LoginActivity explicitly if needed
            // Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            // startActivity(intent);
            // finish();
        });
    }

    private void attemptRegistration() {
        String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

        // ViewModel handles detailed validation
        registerViewModel.register(email, password);
    }

    private void observeViewModel() {
        registerViewModel.isLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            registerButton.setEnabled(!isLoading);
            emailEditText.setEnabled(!isLoading);
            passwordEditText.setEnabled(!isLoading);
        });

        registerViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                // Show error (could be in TextInputLayout or Toast)
                // Example: Set error on email field if relevant, otherwise use Toast
                if (errorMessage.toLowerCase().contains("email")) {
                    emailInputLayout.setError(errorMessage);
                    passwordInputLayout.setError(null); // Clear other errors
                } else if (errorMessage.toLowerCase().contains("password")) {
                    passwordInputLayout.setError(errorMessage);
                    emailInputLayout.setError(null); // Clear other errors
                } else {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    emailInputLayout.setError(null);
                    passwordInputLayout.setError(null);
                }
                registerViewModel.clearErrorMessage(); // Clear after showing
            } else {
                // Clear errors if message is null
                emailInputLayout.setError(null);
                passwordInputLayout.setError(null);
            }
        });

        registerViewModel.getRegistrationSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Registration Successful! Please Login.", Toast.LENGTH_LONG).show();
                // Finish this activity to return to LoginActivity
                finish();
                // Or navigate explicitly
                // Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                // startActivity(intent);
                // finish();
            }
        });
    }
}