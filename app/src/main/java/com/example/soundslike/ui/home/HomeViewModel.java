package com.example.soundslike.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R; // Import your R class
import com.example.soundslike.data.models.Genre;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.Song;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Playlist>> _playlists = new MutableLiveData<>();
    public LiveData<List<Playlist>> getPlaylists() {
        return _playlists;
    }

    private final MutableLiveData<List<Genre>> _genres = new MutableLiveData<>();
    public LiveData<List<Genre>> getGenres() {
        return _genres;
    }

    private final MutableLiveData<List<Song>> _recommendedSongs = new MutableLiveData<>();
    public LiveData<List<Song>> getRecommendedSongs() {
        return _recommendedSongs;
    }

    public HomeViewModel() {
        loadMockData();
    }

    private void loadMockData() {
        // Mock Playlists
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist("pl1", "Chill Vibes", "Relaxing tunes", R.drawable.album_art_get_lucky)); // Use actual drawable
        mockPlaylists.add(new Playlist("pl2", "Workout Beats", "High energy tracks", R.drawable.ic_launcher_background));
        mockPlaylists.add(new Playlist("pl3", "Focus Flow", "Instrumental focus music", R.drawable.ic_genre_placeholder));
        mockPlaylists.add(new Playlist("pl4", "Road Trip", null, R.drawable.album_art_get_lucky));
        mockPlaylists.add(new Playlist("pl5", "Throwbacks", "Hits from the 2000s", R.drawable.ic_launcher_background));
        _playlists.setValue(mockPlaylists);

        // Mock Genres
        List<Genre> mockGenres = new ArrayList<>();
        mockGenres.add(new Genre("g1", "Pop", R.drawable.ic_genre_placeholder));
        mockGenres.add(new Genre("g2", "Rock", R.drawable.ic_genre_placeholder));
        mockGenres.add(new Genre("g3", "Hip Hop", R.drawable.ic_genre_placeholder));
        mockGenres.add(new Genre("g4", "Electronic", R.drawable.ic_genre_placeholder));
        _genres.setValue(mockGenres);

        // Mock Recommended Songs
        List<Song> mockSongs = new ArrayList<>();
        mockSongs.add(new Song("s1", "Get Lucky", "Daft Punk", R.drawable.album_art_get_lucky, 248000));
        mockSongs.add(new Song("s2", "Bohemian Rhapsody", "Queen", R.drawable.ic_launcher_background, 355000));
        mockSongs.add(new Song("s3", "Shape of You", "Ed Sheeran", R.drawable.ic_genre_placeholder, 233000));
        mockSongs.add(new Song("s4", "Blinding Lights", "The Weeknd", R.drawable.album_art_get_lucky, 200000));
        mockSongs.add(new Song("s5", "Starlight", "Muse", R.drawable.ic_launcher_background, 240000));
        mockSongs.add(new Song("s6", "One More Time", "Daft Punk", R.drawable.ic_genre_placeholder, 320000));
        mockSongs.add(new Song("s7", "Uptown Funk", "Mark Ronson ft. Bruno Mars", R.drawable.album_art_get_lucky, 270000));
        _recommendedSongs.setValue(mockSongs);
    }
}