package com.example.soundslike.ui.home;

import android.os.Bundle;
import android.util.Log; // Import Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar; // Import ProgressBar
import android.widget.Toast; // Import Toast

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
// import com.example.soundslike.ui.adapters.GenreAdapter; // Remove GenreAdapter import
import com.example.soundslike.ui.adapters.ArtistAdapter; // Import ArtistAdapter
import com.example.soundslike.ui.adapters.PlaylistAdapter;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter;

import java.util.Collections; // Import Collections

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    // View & Adapter References
    private RecyclerView playlistsRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private ProgressBar playlistsLoadingIndicator; // Keep if needed

    // --- REMOVED Genre Views/Adapter ---
    // private RecyclerView genresRecyclerView;
    // private GenreAdapter genreAdapter;
    // ---------------------------------

    // --- ADDED Artist Views/Adapter ---
    private RecyclerView artistsRecyclerView;
    private ArtistAdapter artistAdapter;
    private ProgressBar artistsLoadingIndicator; // Add if separate loading needed
    // --------------------------------

    private RecyclerView recommendedRecyclerView;
    private RecommendedSongAdapter recommendedSongAdapter;
    private ProgressBar songsLoadingIndicator; // Keep if needed


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Find RecyclerViews
        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);
        // genresRecyclerView = view.findViewById(R.id.genres_recycler_view); // Remove
        artistsRecyclerView = view.findViewById(R.id.artists_recycler_view); // Find Artist RV
        recommendedRecyclerView = view.findViewById(R.id.recommended_recycler_view);

        // Find ProgressBars (Add IDs to fragment_home.xml if needed for artists)
        // playlistsLoadingIndicator = view.findViewById(R.id.loading_indicator_home_playlists);
        // artistsLoadingIndicator = view.findViewById(R.id.loading_indicator_home_artists);
        // songsLoadingIndicator = view.findViewById(R.id.loading_indicator_home_songs);


        setupPlaylistsRecyclerView();
        // setupGenresRecyclerView(); // Remove
        setupArtistsRecyclerView(); // Add setup for Artists
        setupRecommendedRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupPlaylistsRecyclerView() {
        playlistAdapter = new PlaylistAdapter();
        playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        playlistsRecyclerView.setAdapter(playlistAdapter);
    }

    // --- REMOVED setupGenresRecyclerView ---
    // private void setupGenresRecyclerView() { ... }
    // -------------------------------------

    // --- ADDED setupArtistsRecyclerView ---
    private void setupArtistsRecyclerView() {
        artistAdapter = new ArtistAdapter();
        artistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        artistsRecyclerView.setAdapter(artistAdapter);
    }
    // ------------------------------------

    private void setupRecommendedRecyclerView() {
        recommendedSongAdapter = new RecommendedSongAdapter();
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendedRecyclerView.setNestedScrollingEnabled(false);
        recommendedRecyclerView.setAdapter(recommendedSongAdapter);
    }

    private void observeViewModel() {
        // Observe Playlist Loading/Error/Data
        homeViewModel.isLoadingPlaylists().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d("HomeFragment", "Playlist loading state: " + isLoading);
            // Show/hide playlistsLoadingIndicator if you added one
        });

        homeViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null) {
                Log.d("HomeFragment", "Updating playlists adapter with " + playlists.size() + " items.");
                playlistAdapter.submitList(playlists);
            } else {
                playlistAdapter.submitList(Collections.emptyList());
            }
        });

        // --- REMOVED Genre Observation ---
        // homeViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> { ... });
        // -------------------------------

        // --- ADDED Artist Observation ---
        homeViewModel.isLoadingArtists().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d("HomeFragment", "Artist loading state: " + isLoading);
            // Show/hide artistsLoadingIndicator if you added one
        });

        homeViewModel.getArtists().observe(getViewLifecycleOwner(), artists -> {
            if (artists != null) {
                Log.d("HomeFragment", "Updating artists adapter with " + artists.size() + " items.");
                artistAdapter.submitList(artists);
            } else {
                artistAdapter.submitList(Collections.emptyList());
            }
        });
        // ------------------------------

        // Observe Recommended Songs Loading/Data
        homeViewModel.isLoadingSongs().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d("HomeFragment", "Songs loading state: " + isLoading);
            // Show/hide songsLoadingIndicator if you added one
        });

        homeViewModel.getRecommendedSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                Log.d("HomeFragment", "Updating songs adapter with " + songs.size() + " items.");
                recommendedSongAdapter.submitList(songs);
            } else {
                recommendedSongAdapter.submitList(Collections.emptyList());
            }
        });

        // Observe General Error Message
        homeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                // This might show errors for playlists, artists, or songs.
                // Consider more specific error LiveData in ViewModel if needed.
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "Error from HomeViewModel: " + error);
                // Optionally clear error in VM
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify views and adapters
        if (playlistsRecyclerView != null) playlistsRecyclerView.setAdapter(null);
        // if (genresRecyclerView != null) genresRecyclerView.setAdapter(null); // Remove
        if (artistsRecyclerView != null) artistsRecyclerView.setAdapter(null); // Add Artist RV
        if (recommendedRecyclerView != null) recommendedRecyclerView.setAdapter(null);

        playlistsRecyclerView = null;
        // genresRecyclerView = null; // Remove
        artistsRecyclerView = null; // Add Artist RV
        recommendedRecyclerView = null;

        playlistAdapter = null;
        // genreAdapter = null; // Remove
        artistAdapter = null; // Add Artist adapter
        recommendedSongAdapter = null;

        playlistsLoadingIndicator = null;
        artistsLoadingIndicator = null; // Add Artist indicator
        songsLoadingIndicator = null;
    }
}