package com.example.soundslike.data.models;

import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import com.example.soundslike.R; // Make sure R is imported if used for placeholder

import java.util.Objects;

public class Song {

    @SerializedName("id")
    private final String id;

    @SerializedName("song_code")
    private final int songCode;

    @SerializedName("title")
    private final String title;

    @SerializedName("release")
    private final String release;

    @SerializedName("year")
    private final int year;

    @SerializedName("duration") // API returns float (seconds)
    private final float duration;

    @SerializedName("thumbnail_url")
    @Nullable
    private final String thumbnailUrl;

    @SerializedName("artist_id")
    private final String artistId;

    // Keep a field for the name, set later if needed
    private String artistName;

    public Song(String id, int songCode, String title, String release, int year, float duration, @Nullable String thumbnailUrl, String artistId) {
        this.id = id;
        this.songCode = songCode;
        this.title = title;
        this.release = release;
        this.year = year;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
        this.artistId = artistId;
        this.artistName = "Unknown Artist"; // Default, fetch/set later
    }

    // Getters
    public String getId() { return id; }
    public int getSongCode() { return songCode; }
    public String getTitle() { return title; }
    public String getRelease() { return release; }
    public int getYear() { return year; }
    public float getDuration() { return duration; } // Returns duration in seconds
    @Nullable public String getThumbnailUrl() { return thumbnailUrl; }
    public String getArtistId() { return artistId; }

    // Getter/Setter for artistName
    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    // Helper for milliseconds
    public long getDurationMillis() {
        return (long) (duration * 1000);
    }

    // Deprecated getters for smoother transition (optional)
    /** @deprecated Use getThumbnailUrl() instead. */
    @Deprecated
    public int getAlbumArtResId() { return R.drawable.ic_genre_placeholder; }
    /** @deprecated Use getArtistName() instead. */
    @Deprecated
    public String getArtist() { return getArtistName(); }

    // --- ADDED equals() and hashCode() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        // Compare fields that define equality (usually API fields)
        return songCode == song.songCode &&
                year == song.year &&
                Float.compare(song.duration, duration) == 0 &&
                Objects.equals(id, song.id) &&
                Objects.equals(title, song.title) &&
                Objects.equals(release, song.release) &&
                Objects.equals(thumbnailUrl, song.thumbnailUrl) &&
                Objects.equals(artistId, song.artistId);
        // Note: artistName is often excluded if it's derived/set later
    }

    @Override
    public int hashCode() {
        // Generate hash based on the same fields used in equals()
        return Objects.hash(id, songCode, title, release, year, duration, thumbnailUrl, artistId);
    }
    // ------------------------------------
}