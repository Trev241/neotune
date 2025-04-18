package com.example.soundslike.ui.playlistdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData; // Use MediatorLiveData again
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle; // Import SavedStateHandle
import androidx.lifecycle.ViewModel;

import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.data.repository.PlaylistRepository; // Import repository

public class PlaylistDetailViewModel extends ViewModel {

    private static final String PLAYLIST_ID_KEY = "playlistId"; // Key for SavedStateHandle

    private final PlaylistRepository playlistRepository;
    private final SavedStateHandle savedStateHandle; // To get playlistId argument

    // LiveData for the full playlist details
    private final MediatorLiveData<PlaylistDetail> _playlistDetails = new MediatorLiveData<>();
    public LiveData<PlaylistDetail> getPlaylistDetails() { return _playlistDetails; }

    // LiveData for loading state and errors
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    private LiveData<PlaylistDetail> playlistSource = null; // Keep track of the source

    // --- REINSTATE Constructor with SavedStateHandle ---
    public PlaylistDetailViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.playlistRepository = new PlaylistRepository();
        loadPlaylistData(); // Load data when ViewModel is created
    }
    // --------------------------------------------------

    // --- REINSTATE Method to load data from API ---
    private void loadPlaylistData() {
        // Get the actual playlistId passed via navigation
        String playlistId = savedStateHandle.get(PLAYLIST_ID_KEY);

        // Basic validation
        if (playlistId == null || playlistId.isEmpty() || playlistId.equals("default_playlist_id")) {
            _errorMessage.setValue("Invalid Playlist ID.");
            _isLoading.setValue(false);
            return;
        }

        // Remove previous source if reloading
        if (playlistSource != null) {
            _playlistDetails.removeSource(playlistSource);
        }

        _isLoading.setValue(true);
        _errorMessage.setValue(null);

        // Call the repository to get the LiveData source
        playlistSource = playlistRepository.getPlaylistDetails(playlistId);

        // Observe the source LiveData from the repository
        _playlistDetails.addSource(playlistSource, playlistDetail -> {
            // This lambda executes when the repository LiveData updates
            _isLoading.setValue(false); // Loading is finished (success or fail)
            if (playlistDetail != null) {
                _playlistDetails.setValue(playlistDetail); // Update UI LiveData on success
            } else {
                _playlistDetails.setValue(null); // Clear data on failure
                _errorMessage.setValue("Failed to load playlist details."); // Set error message
            }
        });
    }
    // ---------------------------------------------

    // --- REMOVE Mock data loading method ---
    // private void loadMockPlaylistDataForStreamingTest() { ... }
    // ---------------------------------------

    @Override
    protected void onCleared() {
        // Clean up the observer when the ViewModel is destroyed
        if (playlistSource != null) {
            _playlistDetails.removeSource(playlistSource);
        }
        super.onCleared();
    }
}