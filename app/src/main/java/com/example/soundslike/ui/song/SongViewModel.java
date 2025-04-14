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

    public SongViewModel() {
        loadMockSong("mock_song_id");
    }


    public void loadMockSong(String songId) {
        _currentSong.setValue(
                new Song("s1", "Get Lucky", "Daft Punk", R.drawable.album_art_get_lucky, 248000)
        );
    }
}