package com.example.soundslike.ui.explore;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData; // Import LiveData
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExploreViewModel extends ViewModel {

    private static final String TAG = "ExploreViewModel";

    private final MediatorLiveData<List<Song>> _displaySongs = new MediatorLiveData<>();
    public LiveData<List<Song>> getDisplaySongs() { return _displaySongs; }

    private final MutableLiveData<List<Song>> _searchResults = new MutableLiveData<>();

    // Keep this private
    private final MutableLiveData<String> _searchQuery = new MutableLiveData<>("");
    // --- ADD PUBLIC GETTER FOR THE QUERY ---
    public LiveData<String> getSearchQuery() { return _searchQuery; }
    // ---------------------------------------

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    private final SongRepository songRepository;
    private LiveData<List<Song>> currentSource = null;

    public ExploreViewModel() {
        songRepository = new SongRepository();
        // Observe the search query LiveData (use the public getter now)
        _displaySongs.addSource(getSearchQuery(), query -> { // Use getter here
            if (TextUtils.isEmpty(query)) {
                loadSuggestedSongs();
            } else {
                performSearch(query);
            }
        });
        loadSuggestedSongs();
    }

    private void loadSuggestedSongs() {
        Log.d(TAG, "Loading suggested songs...");
        if (currentSource != null) {
            _displaySongs.removeSource(currentSource);
        }
        _isLoading.setValue(true);
        _errorMessage.setValue(null);
        currentSource = songRepository.getSongs(0, 20);
        _displaySongs.addSource(currentSource, songs -> {
            // Use the getter to check the current query state
            if (TextUtils.isEmpty(getSearchQuery().getValue())) {
                _isLoading.setValue(false);
                if (songs != null) {
                    _displaySongs.setValue(songs);
                } else {
                    _errorMessage.setValue("Failed to load suggestions.");
                    _displaySongs.setValue(new ArrayList<>());
                }
            }
        });
    }

    public void searchSongs(String query) {
        _searchQuery.setValue(query == null ? "" : query.trim());
    }

    private void performSearch(String query) {
        Log.d(TAG, "Performing search for: " + query);
        if (currentSource != null) {
            _displaySongs.removeSource(currentSource);
        }
        _isLoading.setValue(true);
        _errorMessage.setValue(null);
        currentSource = songRepository.searchSongs(query, 0, 50);
        _displaySongs.addSource(currentSource, searchResults -> {
            // Use the getter to check the current query state
            if (Objects.equals(query, getSearchQuery().getValue()) && !TextUtils.isEmpty(query)) {
                _isLoading.setValue(false);
                if (searchResults != null) {
                    _displaySongs.setValue(searchResults);
                    if (searchResults.isEmpty()) {
                        _errorMessage.setValue("No results found for '" + query + "'");
                    }
                } else {
                    _errorMessage.setValue("Search failed.");
                    _displaySongs.setValue(new ArrayList<>());
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        if (currentSource != null) {
            _displaySongs.removeSource(currentSource);
        }
        super.onCleared();
    }
}