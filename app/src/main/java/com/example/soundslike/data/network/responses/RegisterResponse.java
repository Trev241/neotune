package com.example.soundslike.data.network.responses;

import com.google.gson.annotations.SerializedName;

// Matches the JSON response body for successful registration
public class RegisterResponse {

    @SerializedName("email")
    private String email;

    @SerializedName("id")
    private int id; // Assuming ID is an integer based on screenshot

    // Getters
    public String getEmail() { return email; }
    public int getId() { return id; }
}