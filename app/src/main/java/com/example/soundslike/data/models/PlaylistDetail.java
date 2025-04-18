package com.example.soundslike.data.models;

import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PlaylistDetail {

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
    private final int songCount;
    @SerializedName("songs")
    private final List<Song> songs;

    // --- UPDATE CONSTRUCTOR ---
    public PlaylistDetail(String id, String name, int userId, @Nullable String coverImageUrl,
                          Date createdAt, @Nullable Date updatedAt, int songCount, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.userId = userId; // Assign int
        this.coverImageUrl = coverImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.songCount = songCount;
        this.songs = songs;
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
    public List<Song> getSongs() { return songs; }

    // --- UPDATE equals() and hashCode() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistDetail that = (PlaylistDetail) o;
        return userId == that.userId && // Compare int
                songCount == that.songCount &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(coverImageUrl, that.coverImageUrl) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(songs, that.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, coverImageUrl, createdAt, updatedAt, songCount, songs); // Use int userId
    }
    // ------------------------------------
}