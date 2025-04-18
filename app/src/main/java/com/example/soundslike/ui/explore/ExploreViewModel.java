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

    // --- CORRECTED Mock Song Loading ---
    private void loadMockSuggestions() {
        // Simulate loading suggestions (replace with actual logic later)
        // _isLoading.setValue(true); // Start loading

        List<Song> mockSuggestions = new ArrayList<>();

        // Use the NEW Song constructor with placeholders
        // Song(String id, int songCode, String title, String release, int year, float duration, @Nullable String thumbnailUrl, String artistId)

        // Another One Bites the Dust
        mockSuggestions.add(new Song(
                "s10",                          // id
                -1,                             // songCode (placeholder)
                "Another One Bites the Dust",   // title
                "Mock Release",                 // release (placeholder)
                1980,                           // year (example)
                215.0f,                         // duration (float seconds from old long)
                null,                           // thumbnailUrl (placeholder)
                "queen_artist_id"               // artistId (placeholder)
        ));
        mockSuggestions.get(0).setArtistName("Queen"); // Set artist name separately

        // Instant Crush
        mockSuggestions.add(new Song(
                "s11",                          // id
                -1,                             // songCode
                "Instant Crush",                // title
                "Random Access Memories",       // release (example)
                2013,                           // year (example)
                337.0f,                         // duration
                null,                           // thumbnailUrl
                "daftpunk_artist_id"            // artistId
        ));
        mockSuggestions.get(1).setArtistName("Daft Punk ft. Julian Casablancas");

        // Feel Good Inc.
        mockSuggestions.add(new Song(
                "s12",                          // id
                -1,                             // songCode
                "Feel Good Inc.",               // title
                "Demon Days",                   // release (example)
                2005,                           // year (example)
                222.0f,                         // duration
                null,                           // thumbnailUrl
                "gorillaz_artist_id"            // artistId
        ));
        mockSuggestions.get(2).setArtistName("Gorillaz");

        // Take On Me
        mockSuggestions.add(new Song(
                "s13",                          // id
                -1,                             // songCode
                "Take On Me",                   // title
                "Hunting High and Low",         // release (example)
                1985,                           // year (example)
                225.0f,                         // duration
                null,                           // thumbnailUrl
                "aha_artist_id"                 // artistId
        ));
        mockSuggestions.get(3).setArtistName("a-ha");

        // Wonderwall
        mockSuggestions.add(new Song(
                "s14",                          // id
                -1,                             // songCode
                "Wonderwall",                   // title
                "(What's the Story) Morning Glory?", // release (example)
                1995,                           // year (example)
                258.0f,                         // duration
                null,                           // thumbnailUrl
                "oasis_artist_id"               // artistId
        ));
        mockSuggestions.get(4).setArtistName("Oasis");


        _suggestedSongs.setValue(mockSuggestions);
        // _isLoading.setValue(false); // Finish loading
    }
    // --- END OF CORRECTION ---


    // TODO: Add method for handling search later
    // public void search(String query) { ... }
}