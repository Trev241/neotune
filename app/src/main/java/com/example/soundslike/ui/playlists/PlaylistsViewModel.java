package com.example.soundslike.ui.playlists;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData; // Import
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PlaylistsViewModel extends ViewModel {

    private static final String TAG = "PlaylistsViewModel";
    // --- Use an integer for the mock user ID ---
    private static final int MOCK_USER_ID = 1; // Example integer ID
    // ------------------------------------------
    private static final String TEMP_AUTH_TOKEN = "YOUR_HARDCODED_JWT_TOKEN_HERE"; // Replace this

    private final PlaylistRepository playlistRepository;
    private final MediatorLiveData<List<Playlist>> _userPlaylists = new MediatorLiveData<>();
    public LiveData<List<Playlist>> getUserPlaylists() { return _userPlaylists; }
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }
    private final MutableLiveData<String> _actionFeedback = new MutableLiveData<>();
    public LiveData<String> getActionFeedback() { return _actionFeedback; }
    private LiveData<List<Playlist>> playlistSource = null;

    public PlaylistsViewModel() {
        playlistRepository = new PlaylistRepository();
        fetchUserPlaylists(); // Fetch real playlists on init
    }

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

    public void createPlaylistAndAddSong(String playlistName, String songIdToAdd) {
        Log.d(TAG, "Requesting creation of playlist '" + playlistName + "' and adding song '" + songIdToAdd + "'");
        _isLoading.setValue(true);
        _errorMessage.setValue(null);
        LiveData<Playlist> creationSource = playlistRepository.createPlaylist(TEMP_AUTH_TOKEN, playlistName);
        MediatorLiveData<Playlist> creationObserver = new MediatorLiveData<>();
        creationObserver.addSource(creationSource, newPlaylist -> {
            creationObserver.removeSource(creationSource);
            if (newPlaylist != null) {
                Log.i(TAG, "Playlist created successfully: " + newPlaylist.getName());
                _actionFeedback.setValue("Playlist '" + playlistName + "' created.");
                addSongToExistingPlaylistInternal(newPlaylist.getId(), newPlaylist.getName(), songIdToAdd);
                fetchUserPlaylists(); // Refresh list
            } else {
                Log.e(TAG, "Playlist creation failed.");
                _errorMessage.setValue("Failed to create playlist: " + playlistName);
                _isLoading.setValue(false);
            }
        });
    }

    public void addSongToPlaylist(String playlistId, String playlistName, String songIdToAdd) {
        Log.d(TAG, "Requesting add song '" + songIdToAdd + "' to playlist '" + playlistName + "' (ID: " + playlistId + ")");
        addSongToExistingPlaylistInternal(playlistId, playlistName, songIdToAdd);
    }

    private void addSongToExistingPlaylistInternal(String playlistId, String playlistName, String songIdToAdd) {
        _isLoading.setValue(true);
        _errorMessage.setValue(null);
        LiveData<Boolean> addSongSource = playlistRepository.addSongToPlaylist(TEMP_AUTH_TOKEN, playlistId, songIdToAdd, null);
        MediatorLiveData<Boolean> addSongObserver = new MediatorLiveData<>();
        addSongObserver.addSource(addSongSource, success -> {
            addSongObserver.removeSource(addSongSource);
            _isLoading.setValue(false);
            if (success != null && success) {
                Log.i(TAG, "Successfully added song " + songIdToAdd + " to playlist " + playlistId);
                _actionFeedback.setValue("Song added to " + playlistName);
            } else {
                Log.e(TAG, "Failed to add song " + songIdToAdd + " to playlist " + playlistId);
                _errorMessage.setValue("Failed to add song to " + playlistName);
            }
        });
    }

    public void clearActionFeedback() {
        _actionFeedback.setValue(null);
    }

    // REMOVE Mock data loading method if it exists
    // private void loadMockPlaylists() { ... }

    @Override
    protected void onCleared() {
        if (playlistSource != null) {
            _userPlaylists.removeSource(playlistSource);
        }
        super.onCleared();
    }
}