package com.example.soundslike.ui.playlists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsViewModel extends ViewModel {

    private final MutableLiveData<List<Playlist>> _userPlaylists = new MutableLiveData<>();
    public LiveData<List<Playlist>> getUserPlaylists() {
        return _userPlaylists;
    }

    public PlaylistsViewModel() {
        loadMockPlaylists();
    }

    private void loadMockPlaylists() {
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist("upl1", "My Awesome Mix", "My favorite tracks", R.drawable.album_art_get_lucky));
        mockPlaylists.add(new Playlist("upl2", "Gym Power", "Motivation!", R.drawable.ic_launcher_background));
        mockPlaylists.add(new Playlist("upl3", "Study Zone", null, R.drawable.ic_genre_placeholder));
        mockPlaylists.add(new Playlist("upl4", "Late Night Drive", "Synthwave and chill", R.drawable.album_art_get_lucky));
        mockPlaylists.add(new Playlist("upl5", "Liked Songs", "All the songs you liked", R.drawable.ic_heart_filled)); // Example for Liked Songs
        _userPlaylists.setValue(mockPlaylists);
    }
}