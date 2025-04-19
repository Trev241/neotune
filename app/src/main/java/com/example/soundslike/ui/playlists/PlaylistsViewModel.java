package com.example.soundslike.ui.playlists;

import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.repository.PlaylistRepository;
import java.util.ArrayList;
import java.util.List;

public class PlaylistsViewModel extends ViewModel {

    private static final String TAG = "PlaylistsViewModel";
    // --- REMOVED HARDCODED TOKEN ---
    // private static final String TEMP_AUTH_TOKEN = "...";
    // -----------------------------

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
        fetchUserPlaylists();
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
        playlistSource = playlistRepository.getUserPlaylists(0, 100); // No token needed here
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

    private void addSongToExistingPlaylistInternal(String playlistId, String playlistName, String songIdToAdd) {
        if (_isLoading.getValue() == null || !_isLoading.getValue()) {
            _isLoading.setValue(true);
        }
        _errorMessage.setValue(null);
        LiveData<Boolean> addSongSource = playlistRepository.addSongToPlaylist(playlistId, songIdToAdd, null); // No token needed here
        MediatorLiveData<Boolean> addSongObserver = new MediatorLiveData<>();
        addSongObserver.addSource(addSongSource, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean success) {
                addSongObserver.removeSource(addSongSource);
                _isLoading.setValue(false);
                if (success != null && success) {
                    Log.i(TAG, "Successfully added song " + songIdToAdd + " to playlist '" + playlistName + "' (ID: " + playlistId + ")");
                    _actionFeedback.setValue("Song added to " + playlistName);
                } else {
                    Log.e(TAG, "Failed to add song " + songIdToAdd + " to playlist '" + playlistName + "' (ID: " + playlistId + ")");
                    _errorMessage.setValue("Failed to add song to " + playlistName);
                }
            }
        });
    }

    public void addSongToPlaylist(String playlistId, String playlistName, String songIdToAdd) {
        Log.d(TAG, "Requesting add song '" + songIdToAdd + "' to playlist '" + playlistName + "' (ID: " + playlistId + ")");
        addSongToExistingPlaylistInternal(playlistId, playlistName, songIdToAdd);
    }

    public void createPlaylistAndAddSong(String playlistName, String songIdToAdd) {
        Log.d(TAG, "Requesting creation of playlist '" + playlistName + "' and adding song '" + songIdToAdd + "'");
        _isLoading.setValue(true);
        _errorMessage.setValue(null);
        LiveData<Playlist> creationSource = playlistRepository.createPlaylist(playlistName); // No token needed here
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