package com.example.soundslike.ui.song;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;

public class SongViewModel extends ViewModel {

    private final MutableLiveData<Song> _currentSong = new MutableLiveData<>();
    public LiveData<Song> getCurrentSong() {
        return _currentSong;
    }

    // TODO: Add LiveData for playback state (isPlaying, progress, duration) later

    // In a real app, you'd observe the playback service or receive songId
    public SongViewModel() {
        loadMockSong("mock_song_id"); // Load a default mock song
    }

    // In a real app, this would fetch details or get from playback service
    public void loadMockSong(String songId) {
        // Load a specific mock song (e.g., Get Lucky)
        _currentSong.setValue(
                new Song("s1", "Get Lucky", "Daft Punk", R.drawable.album_art_get_lucky, 248000)
        );
        // TODO: Initialize playback state LiveData here later
    }
}