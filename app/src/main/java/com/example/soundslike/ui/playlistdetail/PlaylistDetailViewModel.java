package com.example.soundslike.ui.playlistdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R; // Import R for placeholder drawable
import com.example.soundslike.data.models.Playlist; // Import Playlist
import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.data.models.Song; // Import Song

import java.util.ArrayList;
import java.util.Date; // Import Date
import java.util.List;

// Removed imports for SavedStateHandle, MediatorLiveData, PlaylistRepository as they are not used in this mock version
// import androidx.lifecycle.MediatorLiveData;
// import androidx.lifecycle.SavedStateHandle;
// import com.example.soundslike.data.repository.PlaylistRepository;

public class PlaylistDetailViewModel extends ViewModel {

    // LiveData definitions remain the same
    private final MutableLiveData<PlaylistDetail> _playlistDetails = new MutableLiveData<>();
    public LiveData<PlaylistDetail> getPlaylistDetails() { return _playlistDetails; }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    // Removed repository, savedStateHandle, playlistSource fields

    // Simplified constructor for mock data
    public PlaylistDetailViewModel() {
        loadMockPlaylistDataForStreamingTest(); // Call the mock data loader directly
    }

    // --- Method to load hardcoded data for testing ---
    private void loadMockPlaylistDataForStreamingTest() {
        _isLoading.setValue(true); // Simulate loading start
        _errorMessage.setValue(null); // Clear any previous error

        // Create mock songs using IDs from your database screenshot
        List<Song> mockSongs = new ArrayList<>();

        // Song 1: TESTSONG (Index 3 in screenshot)
        Song song1 = new Song(
                "TESTSONG",                             // id
                -1,                                     // songCode (placeholder, assuming not available easily)
                "三月のパンタシア 『青春なんていらないわ』", // title (approximated)
                "TESTRELEASE",                          // release
                2018,                                   // year
                232.0f,                                 // duration (float seconds)
                null,                                   // thumbnailUrl (assuming NONE means null)
                "TESTARTIST"                            // artistId
        );
        song1.setArtistName("Test Artist"); // Set placeholder artist name
        mockSongs.add(song1);

        // Song 2: Rods And Cones (Index 1 in screenshot)
        Song song2 = new Song(
                "SOBQJJX12A6D4F7F01",                   // id
                -1,                                     // songCode (placeholder)
                "Rods And Cones",                       // title
                "Audio",                                // release
                1999,                                   // year
                357.72036f,                             // duration (float seconds)
                null,                                   // thumbnailUrl
                "ARC07PP118"                            // artistId
        );
        song2.setArtistName("Blur"); // Example artist name
        mockSongs.add(song2);

        // Song 3: Heela (Index 2 in screenshot)
        Song song3 = new Song(
                "SOBSSGK12A6D4F9EF1",                   // id
                -1,                                     // songCode (placeholder)
                "Heela",                                // title
                "Dance Hall At Louse Point",            // release (approximated)
                0,                                      // year (use 0 as in DB)
                199.47057f,                             // duration (float seconds)
                null,                                   // thumbnailUrl
                "AR466S2118"                            // artistId
        );
        song3.setArtistName("PJ Harvey"); // Example artist name
        mockSongs.add(song3);

        // Create a mock PlaylistDetail object
        PlaylistDetail mockDetail = new PlaylistDetail(
                "mock_playlist_id_for_test",            // id
                "Streaming Test Playlist",              // name
                "mock_user_id",                         // userId
                null,                                   // coverImageUrl (use null or a placeholder URL)
                new Date(),                             // createdAt (current time)
                null,                                   // updatedAt
                mockSongs.size(),                       // songCount
                mockSongs                               // the list of songs
        );

        // Post the mock data to LiveData after a short delay (optional, simulates network)
        // new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
        _playlistDetails.setValue(mockDetail);
        _isLoading.setValue(false); // Simulate loading end
        // }, 500); // 500ms delay

    }

    // Removed onCleared related to API source observation
}