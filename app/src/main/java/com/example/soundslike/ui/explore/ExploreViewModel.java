package com.example.soundslike.ui.explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData; // Import MediatorLiveData
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Import R if needed for placeholders, otherwise remove
// import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.repository.SongRepository; // Import repository

import java.util.ArrayList;
import java.util.List;

public class ExploreViewModel extends ViewModel {

    // Use MediatorLiveData to observe repository
    private final MediatorLiveData<List<Song>> _suggestedSongs = new MediatorLiveData<>();
    public LiveData<List<Song>> getSuggestedSongs() { return _suggestedSongs; }

    // Add LiveData for loading state and errors
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    private final SongRepository songRepository;
    private LiveData<List<Song>> songsSource = null; // Keep track of the source

    public ExploreViewModel() {
        songRepository = new SongRepository(); // Instantiate repository
        loadSuggestedSongs(); // Load songs from API
    }

    // --- Method to load songs from API ---
    private void loadSuggestedSongs() {
        if (songsSource != null) {
            _suggestedSongs.removeSource(songsSource); // Remove previous source if any
        }
        _isLoading.setValue(true);
        _errorMessage.setValue(null);

        // Fetch songs from repository (e.g., first 20 songs)
        songsSource = songRepository.getSongs(0, 20);

        _suggestedSongs.addSource(songsSource, songs -> {
            // This lambda is called when the repository LiveData updates
            _suggestedSongs.setValue(songs); // Update our LiveData
            _isLoading.setValue(false); // Loading finished
            if (songs == null || songs.isEmpty()) {
                // Optionally set an error message if the result is empty/null after loading
                _errorMessage.setValue("No suggested songs found.");
            }
        });
    }
    // --- END ---

    // --- REMOVE Mock data loading method ---
    /*
    private void loadMockSuggestions() {
        List<Song> mockSuggestions = new ArrayList<>();
        // ... mock song creation ...
        _suggestedSongs.setValue(mockSuggestions);
    }
    */
    // --- END ---

    // TODO: Add method for handling search later
    // public void search(String query) { ... }

    @Override
    protected void onCleared() {
        // Clean up observers when ViewModel is destroyed
        if (songsSource != null) {
            _suggestedSongs.removeSource(songsSource);
        }
        super.onCleared();
    }
}