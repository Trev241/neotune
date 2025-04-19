package com.example.soundslike.data.models;

import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Artist {

    @SerializedName("id")
    private final String id;

    @SerializedName("name")
    private final String name;

    @SerializedName("bio")
    @Nullable // Bio might be optional
    private final String bio;

    @SerializedName("image_url")
    @Nullable // Image URL might be optional
    private final String imageUrl;

    public Artist(String id, String name, @Nullable String bio, @Nullable String imageUrl) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    @Nullable public String getBio() { return bio; }
    @Nullable public String getImageUrl() { return imageUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id) &&
                Objects.equals(name, artist.name) &&
                Objects.equals(bio, artist.bio) &&
                Objects.equals(imageUrl, artist.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bio, imageUrl);
    }
}