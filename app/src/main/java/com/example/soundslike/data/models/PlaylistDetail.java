package com.example.soundslike.data.models;

import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Objects; // Import Objects

public class PlaylistDetail {

    // Fields... (as defined in previous correction)
    private final String id;
    private final String name;
    private final String userId;
    @Nullable
    private final String coverImageUrl;
    private final Date createdAt;
    @Nullable
    private final Date updatedAt;
    private final int songCount;
    private final List<Song> songs;

    // Constructor... (as defined in previous correction)
    public PlaylistDetail(String id, String name, String userId, @Nullable String coverImageUrl,
                          Date createdAt, @Nullable Date updatedAt, int songCount, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.coverImageUrl = coverImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.songCount = songCount;
        this.songs = songs;
    }

    // Getters... (as defined in previous correction)
    public String getId() { return id; }
    public String getName() { return name; }
    public String getUserId() { return userId; }
    @Nullable public String getCoverImageUrl() { return coverImageUrl; }
    public Date getCreatedAt() { return createdAt; }
    @Nullable public Date getUpdatedAt() { return updatedAt; }
    public int getSongCount() { return songCount; }
    public List<Song> getSongs() { return songs; }

    // --- ADDED equals() and hashCode() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistDetail that = (PlaylistDetail) o;
        return songCount == that.songCount &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(coverImageUrl, that.coverImageUrl) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(songs, that.songs); // Include songs list comparison
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, coverImageUrl, createdAt, updatedAt, songCount, songs);
    }
    // ------------------------------------
}