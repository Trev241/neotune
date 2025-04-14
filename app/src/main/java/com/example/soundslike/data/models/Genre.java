package com.example.soundslike.data.models;

import androidx.annotation.DrawableRes;

import java.util.Objects;

public class Genre {
    private final String id;
    private final String name;
    @DrawableRes
    private final int iconResId;

    public Genre(String id, String name, @DrawableRes int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @DrawableRes
    public int getIconResId() {
        return iconResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return iconResId == genre.iconResId &&
                Objects.equals(id, genre.id) &&
                Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, iconResId);
    }
}