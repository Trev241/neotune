package com.example.soundslike.data.models;

import androidx.annotation.DrawableRes;

import java.util.Objects;

public class Song {
    private final String id;
    private final String title;
    private final String artist;
    @DrawableRes
    private final int albumArtResId;
    private final long durationMillis;

    public Song(String id, String title, String artist, @DrawableRes int albumArtResId, long durationMillis) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.albumArtResId = albumArtResId;
        this.durationMillis = durationMillis;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @DrawableRes
    public int getAlbumArtResId() {
        return albumArtResId;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return albumArtResId == song.albumArtResId &&
                durationMillis == song.durationMillis &&
                Objects.equals(id, song.id) &&
                Objects.equals(title, song.title) &&
                Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artist, albumArtResId, durationMillis);
    }
}