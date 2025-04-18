package com.example.soundslike.data.models;

import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import com.example.soundslike.R;

import java.util.Date;
import java.util.Objects;

public class Playlist {

    @SerializedName("id")
    private final String id;

    @SerializedName("name")
    private final String name;

    // --- CHANGE TYPE TO INT ---
    @SerializedName("user_id")
    private final int userId; // Changed from String to int
    // --------------------------

    @SerializedName("cover_image_url")
    @Nullable
    private final String coverImageUrl;

    @SerializedName("created_at")
    private final Date createdAt;

    @SerializedName("updated_at")
    @Nullable
    private final Date updatedAt;

    @SerializedName("song_count")
    private int songCount;

    @Nullable
    private String description;

    // --- UPDATE CONSTRUCTOR ---
    public Playlist(String id, String name, int userId, @Nullable String coverImageUrl, Date createdAt, @Nullable Date updatedAt, int songCount) {
        this.id = id;
        this.name = name;
        this.userId = userId; // Assign int
        this.coverImageUrl = coverImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.songCount = songCount;
        this.description = null;
    }

    // Getters...
    public String getId() { return id; }
    public String getName() { return name; }
    // --- UPDATE GETTER TYPE ---
    public int getUserId() { return userId; } // Return int
    // --------------------------
    @Nullable public String getCoverImageUrl() { return coverImageUrl; }
    public Date getCreatedAt() { return createdAt; }
    @Nullable public Date getUpdatedAt() { return updatedAt; }
    public int getSongCount() { return songCount; }
    public void setSongCount(int songCount) { this.songCount = songCount; }
    @Nullable public String getDescription() { return description; }
    public void setDescription(@Nullable String description) { this.description = description; }

    @Deprecated
    public int getCoverArtResId() { return R.drawable.ic_genre_placeholder; }

    // --- UPDATE equals() and hashCode() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return userId == playlist.userId && // Compare int
                songCount == playlist.songCount &&
                Objects.equals(id, playlist.id) &&
                Objects.equals(name, playlist.name) &&
                Objects.equals(coverImageUrl, playlist.coverImageUrl) &&
                Objects.equals(createdAt, playlist.createdAt) &&
                Objects.equals(updatedAt, playlist.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, coverImageUrl, createdAt, updatedAt, songCount); // Use int userId
    }
    // ------------------------------------
}