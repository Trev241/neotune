package com.example.soundslike.data.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Playlist {
    private final String id;
    private final String name;
    @Nullable
    private final String description;
    @DrawableRes
    private final int coverArtResId;

    public Playlist(String id, String name, @Nullable String description, @DrawableRes int coverArtResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coverArtResId = coverArtResId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @DrawableRes
    public int getCoverArtResId() {
        return coverArtResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return coverArtResId == playlist.coverArtResId &&
                Objects.equals(id, playlist.id) &&
                Objects.equals(name, playlist.name) &&
                Objects.equals(description, playlist.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, coverArtResId);
    }
}