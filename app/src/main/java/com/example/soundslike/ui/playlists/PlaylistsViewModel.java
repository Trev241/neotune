package com.example.soundslike.ui.playlists;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer; // Import Observer
import androidx.lifecycle.ViewModel;

import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PlaylistsViewModel extends ViewModel {

    private static final String TAG = "PlaylistsViewModel";
    // --- TODO: Replace with actual token retrieval ---
    private static final String TEMP_AUTH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNzQ1NjA4OTg3fQ.QwAVnGLKGd6jFJXLd3Qy8S-0SeSVzk4cap_PXOoWbX8"; // Replace with your actual token
    // -------------------------------------------------
    // --- REMOVED LIKED_SONGS_PLAYLIST_NAME ---
    // private static final String LIKED_SONGS_PLAYLIST_NAME = "Liked Songs";
    // -----------------------------------------

    private final PlaylistRepository playlistRepository;

    private final MediatorLiveData<List<Playlist>> _userPlaylists = new MediatorLiveData<>();
    public LiveData<List<Playlist>> getUserPlaylists() { return _userPlaylists; }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    // LiveData for short feedback messages (e.g., "Added to Playlist X")
    private final MutableLiveData<String> _actionFeedback = new MutableLiveData<>();
    public LiveData<String> getActionFeedback() { return _actionFeedback; }

    private LiveData<List<Playlist>> playlistSource = null;

    public PlaylistsViewModel() {
        playlistRepository = new PlaylistRepository();
        fetchUserPlaylists(); // Fetch playlists when ViewModel is created
    }

    // Fetch all user playlists
    public void fetchUserPlaylists() {
        if (_isLoading.getValue() != null && _isLoading.getValue()) {
            Log.d(TAG, "Already loading playlists, skipping fetch request.");
            return;
        }
        if (playlistSource != null) {
            _userPlaylists.removeSource(playlistSource);
        }
        _isLoading.setValue(true);
        _errorMessage.setValue(null);
        Log.d(TAG, "Fetching user playlists from repository...");

        playlistSource = playlistRepository.getUserPlaylists(TEMP_AUTH_TOKEN, 0, 100);

        _userPlaylists.addSource(playlistSource, playlists -> {
            _isLoading.setValue(false);
            if (playlists != null) {
                Log.d(TAG, "Received " + playlists.size() + " playlists from repository.");
                _userPlaylists.setValue(playlists);
            } else {
                Log.e(TAG, "Received null playlist list from repository.");
                _errorMessage.setValue("Failed to load playlists.");
                _userPlaylists.setValue(new ArrayList<>());
            }
        });
    }

    // --- REMOVED addSongToLikedPlaylist ---
    // public void addSongToLikedPlaylist(String songIdToAdd) { ... }
    // --------------------------------------

    // --- REMOVED createLikedSongsPlaylistAndAddSong ---
    // private void createLikedSongsPlaylistAndAddSong(String songIdToAdd) { ... }
    // --------------------------------------------------

    // --- REMOVED findLikedSongsPlaylistId ---
    // @Nullable
    // private String findLikedSongsPlaylistId(@Nullable List<Playlist> playlists) { ... }
    // ----------------------------------------

    // Internal helper to add a song to any existing playlist by ID
    private void addSongToExistingPlaylistInternal(String playlistId, String playlistName, String songIdToAdd) {
        if (_isLoading.getValue() == null || !_isLoading.getValue()) {
            _isLoading.setValue(true);
        }
        _errorMessage.setValue(null);

        LiveData<Boolean> addSongSource = playlistRepository.addSongToPlaylist(TEMP_AUTH_TOKEN, playlistId, songIdToAdd, null);

        MediatorLiveData<Boolean> addSongObserver = new MediatorLiveData<>();
        addSongObserver.addSource(addSongSource, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean success) {
                addSongObserver.removeSource(addSongSource);
                _isLoading.setValue(false);

                if (success != null && success) {
                    Log.i(TAG, "Successfully added song " + songIdToAdd + " to playlist '" + playlistName + "' (ID: " + playlistId + ")");
                    // --- REMOVED Specific feedback for Liked Songs ---
                    _actionFeedback.setValue("Song added to " + playlistName);
                    // -------------------------------------------------
                } else {
                    Log.e(TAG, "Failed to add song " + songIdToAdd + " to playlist '" + playlistName + "' (ID: " + playlistId + ")");
                    _errorMessage.setValue("Failed to add song to " + playlistName);
                }
            }
        });
    }

    // --- Methods for adding to specific playlists (from bottom sheet) ---

    // Called when user selects an existing playlist from the sheet
    public void addSongToPlaylist(String playlistId, String playlistName, String songIdToAdd) {
        Log.d(TAG, "Requesting add song '" + songIdToAdd + "' to playlist '" + playlistName + "' (ID: " + playlistId + ")");
        addSongToExistingPlaylistInternal(playlistId, playlistName, songIdToAdd);
    }

    // Called when user creates a new playlist via the bottom sheet dialog
    public void createPlaylistAndAddSong(String playlistName, String songIdToAdd) {
        Log.d(TAG, "Requesting creation of playlist '" + playlistName + "' and adding song '" + songIdToAdd + "'");
        _isLoading.setValue(true);
        _errorMessage.setValue(null);
        LiveData<Playlist> creationSource = playlistRepository.createPlaylist(TEMP_AUTH_TOKEN, playlistName);

        MediatorLiveData<Playlist> creationObserver = new MediatorLiveData<>();
        creationObserver.addSource(creationSource, new Observer<Playlist>() {
            @Override
            public void onChanged(@Nullable Playlist newPlaylist) {
                creationObserver.removeSource(creationSource);
                if (newPlaylist != null) {
                    Log.i(TAG, "Playlist created successfully: " + newPlaylist.getName());
                    _actionFeedback.setValue("Playlist '" + playlistName + "' created.");
                    addSongToExistingPlaylistInternal(newPlaylist.getId(), newPlaylist.getName(), songIdToAdd);
                    fetchUserPlaylists();
                } else {
                    Log.e(TAG, "Playlist creation failed.");
                    _errorMessage.setValue("Failed to create playlist: " + playlistName);
                    _isLoading.setValue(false);
                }
            }
        });
    }

    // --- End Bottom Sheet Methods ---


    // Call this from Fragment after showing the feedback Toast
    public void clearActionFeedback() {
        _actionFeedback.setValue(null);
    }

    @Override
    protected void onCleared() {
        if (playlistSource != null) {
            _userPlaylists.removeSource(playlistSource);
        }
        super.onCleared();
    }
}