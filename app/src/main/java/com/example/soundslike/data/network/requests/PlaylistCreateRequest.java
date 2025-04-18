package com.example.soundslike.data.network.requests;

// Simple class to match the expected JSON body for creating a playlist
public class PlaylistCreateRequest {
    private String name;
    // Add other fields like cover_image_url if your API expects them at creation

    public PlaylistCreateRequest(String name) {
        this.name = name;
    }

    // Getters might be needed by Gson/Retrofit, though often not for simple requests
    public String getName() {
        return name;
    }
}