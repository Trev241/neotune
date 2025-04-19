package com.example.soundslike.ui.home;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Artist; // Import Artist
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.repository.ArtistRepository; // Import ArtistRepository
import com.example.soundslike.data.repository.PlaylistRepository;
import com.example.soundslike.data.repository.SongRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    // --- REMOVED HARDCODED TOKEN ---
    // private static final String TEMP_AUTH_TOKEN = "...";
    // -----------------------------

    // --- Repositories ---
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final ArtistRepository artistRepository;

    // --- LiveData for UI ---
    // Playlists
    private final MediatorLiveData<List<Playlist>> _playlists = new MediatorLiveData<>();
    public LiveData<List<Playlist>> getPlaylists() { return _playlists; }
    private final MutableLiveData<Boolean> _isLoadingPlaylists = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoadingPlaylists() { return _isLoadingPlaylists; }
    private LiveData<List<Playlist>> homePlaylistSource = null;

    // Artists
    private final MediatorLiveData<List<Artist>> _artists = new MediatorLiveData<>();
    public LiveData<List<Artist>> getArtists() { return _artists; }
    private final MutableLiveData<Boolean> _isLoadingArtists = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoadingArtists() { return _isLoadingArtists; }
    private LiveData<List<Artist>> artistsSource = null;

    // Recommended Songs
    private final MediatorLiveData<List<Song>> _recommendedSongs = new MediatorLiveData<>();
    public LiveData<List<Song>> getRecommendedSongs() { return _recommendedSongs; }
    private final MutableLiveData<Boolean> _isLoadingSongs = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoadingSongs() { return _isLoadingSongs; }
    private LiveData<List<Song>> songsSource = null;

    // General Error Message
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    private final Random randomGenerator = new Random();

    public HomeViewModel() {
        songRepository = new SongRepository();
        playlistRepository = new PlaylistRepository();
        artistRepository = new ArtistRepository();
        fetchArtists();
        loadRecommendedSongs();
        fetchHomePlaylists();
    }

    private void fetchArtists() {
        if (artistsSource != null) {
            _artists.removeSource(artistsSource);
        }
        _isLoadingArtists.setValue(true);
        _errorMessage.setValue(null);
        Log.d(TAG, "Fetching artists for home screen...");
        artistsSource = artistRepository.getArtists(0, 10); // No token needed here
        _artists.addSource(artistsSource, artists -> {
            _isLoadingArtists.setValue(false);
            if (artists != null) {
                Log.d(TAG, "Received " + artists.size() + " artists for home.");
                _artists.setValue(artists);
            } else {
                Log.e(TAG, "Received null artist list for home from repository.");
                _errorMessage.setValue("Failed to load artists.");
                _artists.setValue(new ArrayList<>());
            }
        });
    }

    public void loadRecommendedSongs() {
        int randomSkip = randomGenerator.nextInt(10000) + 1;
        int limit = 10;
        Log.d(TAG, "Loading recommended songs starting from index (skip): " + randomSkip);
        if (songsSource != null) {
            _recommendedSongs.removeSource(songsSource);
        }
        _isLoadingSongs.setValue(true);
        _errorMessage.setValue(null);
        songsSource = songRepository.getSongs(randomSkip, limit); // No token needed here
        _recommendedSongs.addSource(songsSource, songs -> {
            _isLoadingSongs.setValue(false);
            if (songs != null) {
                Log.d(TAG, "Successfully loaded " + songs.size() + " recommended songs.");
                _recommendedSongs.setValue(songs);
            } else {
                Log.e(TAG, "Failed to load recommended songs from repository.");
                _errorMessage.setValue("Failed to load recommended songs.");
                _recommendedSongs.setValue(new ArrayList<>());
            }
        });
    }

    private void fetchHomePlaylists() {
        if (homePlaylistSource != null) {
            _playlists.removeSource(homePlaylistSource);
        }
        _isLoadingPlaylists.setValue(true);
        _errorMessage.setValue(null);
        Log.d(TAG, "Fetching home playlists from repository...");
        homePlaylistSource = playlistRepository.getUserPlaylists(0, 5); // No token needed here
        _playlists.addSource(homePlaylistSource, playlists -> {
            _isLoadingPlaylists.setValue(false);
            if (playlists != null) {
                Log.d(TAG, "Received " + playlists.size() + " playlists for home.");
                _playlists.setValue(playlists);
            } else {
                Log.e(TAG, "Received null playlist list for home from repository.");
                _errorMessage.setValue("Failed to load playlists.");
                _playlists.setValue(new ArrayList<>());
            }
        });
    }

    @Override
    protected void onCleared() {
        if (songsSource != null) {
            _recommendedSongs.removeSource(songsSource);
        }
        if (homePlaylistSource != null) {
            _playlists.removeSource(homePlaylistSource);
        }
        if (artistsSource != null) {
            _artists.removeSource(artistsSource);
        }
        super.onCleared();
    }
}