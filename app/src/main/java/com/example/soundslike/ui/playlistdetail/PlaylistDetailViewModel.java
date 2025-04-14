package com.example.soundslike.ui.playlistdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.Song;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailViewModel extends ViewModel {

    private final MutableLiveData<Playlist> _playlistDetails = new MutableLiveData<>();
    public LiveData<Playlist> getPlaylistDetails() {
        return _playlistDetails;
    }

    private final MutableLiveData<List<Song>> _playlistSongs = new MutableLiveData<>();
    public LiveData<List<Song>> getPlaylistSongs() {
        return _playlistSongs;
    }

    public PlaylistDetailViewModel() {
        loadMockPlaylistData("mock_id_1");
    }

    public void loadMockPlaylistData(String playlistId) {
        _playlistDetails.setValue(
                new Playlist(playlistId, "Chill Vibes", "Relaxing tunes for your evening", R.drawable.album_art_get_lucky)
        );

        // Mock Songs for this specific playlist
        List<Song> mockSongs = new ArrayList<>();
        mockSongs.add(new Song("s1", "Get Lucky", "Daft Punk", R.drawable.album_art_get_lucky, 248000));
        mockSongs.add(new Song("s6", "One More Time", "Daft Punk", R.drawable.ic_genre_placeholder, 320000));
        mockSongs.add(new Song("s8", "Weightless", "Marconi Union", R.drawable.ic_launcher_background, 480000)); // Example different song
        mockSongs.add(new Song("s9", "Teardrop", "Massive Attack", R.drawable.album_art_get_lucky, 321000));
        _playlistSongs.setValue(mockSongs);
    }
}