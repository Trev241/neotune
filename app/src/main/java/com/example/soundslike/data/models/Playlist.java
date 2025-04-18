package com.example.soundslike.data.models;

import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import com.example.soundslike.R;

import java.util.Date;
import java.util.Objects; // Make sure Objects is imported

public class Playlist {

    // Fields should match the corrected version from previous steps:
    // @SerializedName("id")
    private final String id;
    // @SerializedName("name")
    private final String name;
    // @SerializedName("user_id")
    private final String userId;
    // @SerializedName("cover_image_url")
    @Nullable
    private final String coverImageUrl;
    // @SerializedName("created_at")
    private final Date createdAt;
    // @SerializedName("updated_at")
    @Nullable
    private final Date updatedAt;
    // @SerializedName("song_count")
    private int songCount; // Keep as mutable if needed, or final

    // Optional description field (not from base API response)
    @Nullable
    private String description;

    // Constructor matching the fields
    public Playlist(String id, String name, String userId, @Nullable String coverImageUrl, Date createdAt, @Nullable Date updatedAt, int songCount) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.coverImageUrl = coverImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.songCount = songCount;
        this.description = null; // Or initialize differently if needed
    }

    // Getters...
    public String getId() { return id; }
    public String getName() { return name; }
    public String getUserId() { return userId; }
    @Nullable public String getCoverImageUrl() { return coverImageUrl; }
    public Date getCreatedAt() { return createdAt; }
    @Nullable public Date getUpdatedAt() { return updatedAt; }
    public int getSongCount() { return songCount; }
    public void setSongCount(int songCount) { this.songCount = songCount; }
    @Nullable public String getDescription() { return description; }
    public void setDescription(@Nullable String description) { this.description = description; }

    // Deprecated getter
    @Deprecated
    public int getCoverArtResId() { return R.drawable.ic_genre_placeholder; }

    // --- CORRECTED equals() and hashCode() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        // Compare fields that define equality based on the API/current model
        return songCount == playlist.songCount &&
                Objects.equals(id, playlist.id) &&
                Objects.equals(name, playlist.name) &&
                Objects.equals(userId, playlist.userId) &&
                Objects.equals(coverImageUrl, playlist.coverImageUrl) &&
                Objects.equals(createdAt, playlist.createdAt) &&
                Objects.equals(updatedAt, playlist.updatedAt);
        // Exclude description if it's not consistently part of the object's identity
    }

    @Override
    public int hashCode() {
        // Hash based on the same fields used in equals()
        return Objects.hash(id, name, userId, coverImageUrl, createdAt, updatedAt, songCount);
    }
    // ----------------------------------------
}