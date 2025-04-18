package com.example.soundslike.ui.playlists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;

import java.util.ArrayList;
import java.util.Date; // Import Date
import java.util.List;

public class PlaylistsViewModel extends ViewModel {

    private final MutableLiveData<List<Playlist>> _userPlaylists = new MutableLiveData<>();
    public LiveData<List<Playlist>> getUserPlaylists() {
        return _userPlaylists;
    }

    public PlaylistsViewModel() {
        loadMockPlaylists();
    }

    // --- CORRECTED Mock Playlist Loading ---
    private void loadMockPlaylists() {
        List<Playlist> mockPlaylists = new ArrayList<>();

        // Use the NEW Playlist constructor with placeholders
        mockPlaylists.add(new Playlist(
                "upl1",                         // id
                "My Awesome Mix",               // name
                "mock_user",                    // userId (placeholder)
                null,                           // coverImageUrl (placeholder)
                new Date(),                     // createdAt (placeholder)
                null,                           // updatedAt (placeholder)
                15                              // songCount (placeholder)
        ));
        // mockPlaylists.get(0).setDescription("My favorite tracks"); // Set description separately if needed

        mockPlaylists.add(new Playlist(
                "upl2",                         // id
                "Gym Power",                    // name
                "mock_user",                    // userId
                null,                           // coverImageUrl
                new Date(),                     // createdAt
                null,                           // updatedAt
                25                              // songCount
        ));
        // mockPlaylists.get(1).setDescription("Motivation!");

        mockPlaylists.add(new Playlist(
                "upl3",                         // id
                "Study Zone",                   // name
                "mock_user",                    // userId
                null,                           // coverImageUrl
                new Date(),                     // createdAt
                null,                           // updatedAt
                30                              // songCount
        ));
        // mockPlaylists.get(2).setDescription(null); // Explicitly null description

        mockPlaylists.add(new Playlist(
                "upl4",                         // id
                "Late Night Drive",             // name
                "mock_user",                    // userId
                null,                           // coverImageUrl
                new Date(),                     // createdAt
                null,                           // updatedAt
                18                              // songCount
        ));
        // mockPlaylists.get(3).setDescription("Synthwave and chill");

        mockPlaylists.add(new Playlist(
                "upl5",                         // id
                "Liked Songs",                  // name
                "mock_user",                    // userId
                null,                           // coverImageUrl
                new Date(),                     // createdAt
                null,                           // updatedAt
                150                             // songCount (example)
        ));
        // mockPlaylists.get(4).setDescription("All the songs you liked");

        _userPlaylists.setValue(mockPlaylists);
    }
    // --- END OF CORRECTION ---
}