package com.example.soundslike.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Genre;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.repository.SongRepository;

import java.util.ArrayList;
import java.util.Date; // Import Date
import java.util.List;

public class HomeViewModel extends ViewModel {

    // LiveData definitions...
    private final MutableLiveData<List<Playlist>> _playlists = new MutableLiveData<>();
    public LiveData<List<Playlist>> getPlaylists() { return _playlists; }

    private final MutableLiveData<List<Genre>> _genres = new MutableLiveData<>();
    public LiveData<List<Genre>> getGenres() { return _genres; }

    private final MediatorLiveData<List<Song>> _recommendedSongs = new MediatorLiveData<>();
    public LiveData<List<Song>> getRecommendedSongs() { return _recommendedSongs; }

    private final MutableLiveData<Boolean> _isLoadingSongs = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoadingSongs() { return _isLoadingSongs; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    private final SongRepository songRepository;
    private LiveData<List<Song>> songsSource = null;

    public HomeViewModel() {
        songRepository = new SongRepository();
        loadMockPlaylistsAndGenres(); // Load mock data
        loadRecommendedSongs(); // Load songs from API
    }

    private void loadRecommendedSongs() {
        if (songsSource != null) {
            _recommendedSongs.removeSource(songsSource);
        }
        _isLoadingSongs.setValue(true);
        _errorMessage.setValue(null);
        songsSource = songRepository.getSongs(0, 10);
        _recommendedSongs.addSource(songsSource, songs -> {
            _recommendedSongs.setValue(songs);
            _isLoadingSongs.setValue(false);
            // Optional error handling if songs list is empty/null
        });
    }

    // --- CORRECTED Mock Playlist Loading ---
    private void loadMockPlaylistsAndGenres() {
        List<Playlist> mockPlaylists = new ArrayList<>();

        // Create mock playlists using the NEW constructor
        mockPlaylists.add(new Playlist(
                "pl1",                          // id
                "Chill Vibes",                  // name
                3,                    // userId (placeholder)
                null,                           // coverImageUrl (placeholder - use null or a URL string)
                new Date(),                     // createdAt (placeholder)
                null,                           // updatedAt (placeholder)
                5                               // songCount (placeholder)
        ));
        // Set description separately if needed for UI, though it's not in constructor
        // mockPlaylists.get(0).setDescription("Relaxing tunes");

        mockPlaylists.add(new Playlist(
                "pl2",                          // id
                "Workout Beats",                // name
                7,                    // userId
                null,                           // coverImageUrl
                new Date(),                     // createdAt
                null,                           // updatedAt
                12                              // songCount
        ));
        // mockPlaylists.get(1).setDescription("High energy tracks");

        // Add more mock playlists similarly...

        _playlists.setValue(mockPlaylists);

        // Mock Genres (remain unchanged)
        List<Genre> mockGenres = new ArrayList<>();
        mockGenres.add(new Genre("g1", "Pop", R.drawable.ic_genre_placeholder));
        mockGenres.add(new Genre("g2", "Rock", R.drawable.ic_genre_placeholder));
        _genres.setValue(mockGenres);
    }
    // --- END OF CORRECTION ---

    @Override
    protected void onCleared() {
        if (songsSource != null) {
            _recommendedSongs.removeSource(songsSource);
        }
        super.onCleared();
    }
}