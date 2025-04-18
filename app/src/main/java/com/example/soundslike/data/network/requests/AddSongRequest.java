package com.example.soundslike.data.network.requests;

import androidx.annotation.Nullable;

// Matches the expected JSON body for adding a song
public class AddSongRequest {
    private String song_id;
    @Nullable // Make order optional
    private Integer order; // Use Integer to allow null

    public AddSongRequest(String songId, @Nullable Integer order) {
        this.song_id = songId;
        this.order = order;
    }
    public AddSongRequest(String songId) {
        this(songId, null); // Constructor without order
    }

    // Getters might be needed by Gson/Retrofit
    public String getSong_id() { return song_id; }
    @Nullable public Integer getOrder() { return order; }
}