package com.example.soundslike.ui.explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;

import java.util.ArrayList;
import java.util.List;

public class ExploreViewModel extends ViewModel {

    private final MutableLiveData<List<Song>> _suggestedSongs = new MutableLiveData<>();
    public LiveData<List<Song>> getSuggestedSongs() {
        return _suggestedSongs;
    }

    // Add LiveData for loading state if needed
    // private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    // public LiveData<Boolean> isLoading() { return _isLoading; }

    public ExploreViewModel() {
        loadMockSuggestions();
    }

    private void loadMockSuggestions() {
        // Simulate loading suggestions (replace with actual logic later)
        // _isLoading.setValue(true); // Start loading

        List<Song> mockSuggestions = new ArrayList<>();
        mockSuggestions.add(new Song("s10", "Another One Bites the Dust", "Queen", R.drawable.ic_launcher_background, 215000));
        mockSuggestions.add(new Song("s11", "Instant Crush", "Daft Punk ft. Julian Casablancas", R.drawable.album_art_get_lucky, 337000));
        mockSuggestions.add(new Song("s12", "Feel Good Inc.", "Gorillaz", R.drawable.ic_genre_placeholder, 222000));
        mockSuggestions.add(new Song("s13", "Take On Me", "a-ha", R.drawable.ic_launcher_background, 225000));
        mockSuggestions.add(new Song("s14", "Wonderwall", "Oasis", R.drawable.album_art_get_lucky, 258000));

        _suggestedSongs.setValue(mockSuggestions);
        // _isLoading.setValue(false); // Finish loading
    }

    // TODO: Add method for handling search later
    // public void search(String query) { ... }
}