package com.example.soundslike.data.network.requests;

import com.google.gson.annotations.SerializedName;

// Matches the JSON body for the register request
public class RegisterRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters might be needed by Gson/Retrofit
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}