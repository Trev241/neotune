package com.example.soundslike.ui.home;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Genre;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.repository.PlaylistRepository; // Import PlaylistRepository
import com.example.soundslike.data.repository.SongRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    // --- TEMPORARY HARDCODED TOKEN ---
    private static final String TEMP_AUTH_TOKEN = "YOUR_HARDCODED_JWT_TOKEN_HERE"; // Replace
    // ---------------------------------

    // --- Repositories ---
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository; // Add playlist repository

    // --- LiveData for UI ---
    // Playlists (Now fetched)
    private final MediatorLiveData<List<Playlist>> _playlists = new MediatorLiveData<>();
    public LiveData<List<Playlist>> getPlaylists() { return _playlists; }
    private final MutableLiveData<Boolean> _isLoadingPlaylists = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoadingPlaylists() { return _isLoadingPlaylists; }
    private LiveData<List<Playlist>> homePlaylistSource = null; // Source for home playlists

    // Genres (Still mock)
    private final MutableLiveData<List<Genre>> _genres = new MutableLiveData<>();
    public LiveData<List<Genre>> getGenres() { return _genres; }

    // Recommended Songs (Fetched)
    private final MediatorLiveData<List<Song>> _recommendedSongs = new MediatorLiveData<>();
    public LiveData<List<Song>> getRecommendedSongs() { return _recommendedSongs; }
    private final MutableLiveData<Boolean> _isLoadingSongs = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoadingSongs() { return _isLoadingSongs; }
    private LiveData<List<Song>> songsSource = null;

    // General Error Message
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    private final Random random = new Random(); // For random images (if needed elsewhere)

    public HomeViewModel() {
        songRepository = new SongRepository();
        playlistRepository = new PlaylistRepository(); // Instantiate playlist repo
        loadMockGenres(); // Load mock genres
        loadRecommendedSongs(); // Load songs from API
        fetchHomePlaylists(); // Load playlists from API
    }

    private void loadRecommendedSongs() {
        if (songsSource != null) {
            _recommendedSongs.removeSource(songsSource);
        }
        _isLoadingSongs.setValue(true);
        _errorMessage.setValue(null); // Clear previous errors
        songsSource = songRepository.getSongs(0, 10); // Fetch first 10 songs
        _recommendedSongs.addSource(songsSource, songs -> {
            _isLoadingSongs.setValue(false);
            if (songs != null) {
                _recommendedSongs.setValue(songs);
            } else {
                _errorMessage.setValue("Failed to load recommended songs.");
                _recommendedSongs.setValue(new ArrayList<>());
            }
        });
    }

    // --- Fetch Playlists for Home Screen ---
    private void fetchHomePlaylists() {
        if (homePlaylistSource != null) {
            _playlists.removeSource(homePlaylistSource);
        }
        _isLoadingPlaylists.setValue(true);
        _errorMessage.setValue(null); // Clear previous errors
        Log.d(TAG, "Fetching home playlists from repository...");

        // Fetch maybe fewer playlists for home screen, e.g., limit 5
        homePlaylistSource = playlistRepository.getUserPlaylists(TEMP_AUTH_TOKEN, 0, 5);

        _playlists.addSource(homePlaylistSource, playlists -> {
            _isLoadingPlaylists.setValue(false);
            if (playlists != null) {
                Log.d(TAG, "Received " + playlists.size() + " playlists for home.");
                // Image handling is now done in the adapter
                _playlists.setValue(playlists);
            } else {
                Log.e(TAG, "Received null playlist list for home from repository.");
                _errorMessage.setValue("Failed to load playlists.");
                _playlists.setValue(new ArrayList<>());
            }
        });
    }
    // --------------------------------------

    // --- Load Mock Genres (Keep for now) ---
    private void loadMockGenres() {
        List<Genre> mockGenres = new ArrayList<>();
        mockGenres.add(new Genre("g1", "Pop", R.drawable.ic_genre_placeholder));
        mockGenres.add(new Genre("g2", "Rock", R.drawable.ic_genre_placeholder));
        _genres.setValue(mockGenres);
    }
    // ------------------------------------

    @Override
    protected void onCleared() {
        if (songsSource != null) {
            _recommendedSongs.removeSource(songsSource);
        }
        if (homePlaylistSource != null) {
            _playlists.removeSource(homePlaylistSource);
        }
        super.onCleared();
    }
}