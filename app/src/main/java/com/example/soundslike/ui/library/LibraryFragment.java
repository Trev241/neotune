package com.example.soundslike.ui.library;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.ArtistAdapter; // Import ArtistAdapter
import com.example.soundslike.ui.adapters.PlaylistAdapter;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter;
import com.example.soundslike.ui.home.HomeViewModel;
import com.example.soundslike.ui.playlists.PlaylistsViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.Collections;

public class LibraryFragment extends Fragment {

    private static final String TAG = "LibraryFragment";

    private PlaylistsViewModel playlistsViewModel;
    private HomeViewModel homeViewModel; // Reused for artists & recommended songs

    // Playlist Views
    private RecyclerView playlistsRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private ProgressBar playlistsLoadingIndicator;
    private MaterialCardView likedSongsCard;

    // Recommended Song Views
    private Button recommendSongsButton;
    private TextView recommendedSongsHeader;
    private RecyclerView recommendedSongsRecyclerView;
    private RecommendedSongAdapter recommendedSongAdapter;
    private ProgressBar songsLoadingIndicator;

    // --- ADDED: Artist Views ---
    private RecyclerView artistsRecyclerView;
    private ArtistAdapter artistAdapter;
    private ProgressBar artistsLoadingIndicator;
    // ---------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        // Initialize ViewModels
        playlistsViewModel = new ViewModelProvider(requireActivity()).get(PlaylistsViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Find Playlist views
        playlistsRecyclerView = view.findViewById(R.id.library_playlists_recycler_view);
        playlistsLoadingIndicator = view.findViewById(R.id.loading_indicator_library_playlists);
        likedSongsCard = view.findViewById(R.id.card_liked_songs);

        // Find Recommended Song views
        recommendSongsButton = view.findViewById(R.id.button_recommend_songs);
        recommendedSongsHeader = view.findViewById(R.id.header_library_recommended);
        recommendedSongsRecyclerView = view.findViewById(R.id.recommended_songs_recycler_view);
        songsLoadingIndicator = view.findViewById(R.id.loading_indicator_library_songs);

        // --- Find Artist Views ---
        // Make sure these IDs exist in fragment_library.xml (add RV and ProgressBar if needed)
        artistsRecyclerView = view.findViewById(R.id.library_artists_recycler_view); // Add this ID to your XML
        artistsLoadingIndicator = view.findViewById(R.id.loading_indicator_library_artists); // Add this ID to your XML
        // -------------------------

        setupPlaylistRecyclerView();
        setupRecommendedSongsRecyclerView();
        setupArtistsRecyclerView(); // Setup Artist RV
        setupClickListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupPlaylistRecyclerView() {
        playlistAdapter = new PlaylistAdapter();
        playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playlistsRecyclerView.setAdapter(playlistAdapter);
        playlistsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupRecommendedSongsRecyclerView() {
        recommendedSongAdapter = new RecommendedSongAdapter();
        recommendedSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendedSongsRecyclerView.setAdapter(recommendedSongAdapter);
        recommendedSongsRecyclerView.setNestedScrollingEnabled(false);
    }

    // --- Setup for Artists RecyclerView ---
    private void setupArtistsRecyclerView() {
        artistAdapter = new ArtistAdapter();
        // Use horizontal layout similar to Home screen
        artistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        artistsRecyclerView.setAdapter(artistAdapter);
        // No nested scrolling needed for horizontal RV
    }
    // ------------------------------------

    private void setupClickListeners() {
        likedSongsCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Liked Songs screen coming soon!", Toast.LENGTH_SHORT).show();
        });

        recommendSongsButton.setOnClickListener(v -> {
            Log.d(TAG, "Recommend Songs button clicked");
            recommendedSongsHeader.setVisibility(View.VISIBLE);
            recommendedSongsRecyclerView.setVisibility(View.VISIBLE);
            homeViewModel.loadRecommendedSongs();
        });
    }


    private void observeViewModel() {
        // Observe PlaylistsViewModel
        playlistsViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d(TAG, "Playlists loading state: " + isLoading);
            if (playlistsLoadingIndicator != null) {
                playlistsLoadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        playlistsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), "Playlist Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
        playlistsViewModel.getUserPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null) {
                Log.d(TAG, "Updating library playlists adapter with " + playlists.size() + " items.");
                playlistAdapter.submitList(playlists);
                if (playlistsLoadingIndicator != null) {
                    playlistsLoadingIndicator.setVisibility(View.GONE);
                }
            } else {
                playlistAdapter.submitList(Collections.emptyList());
            }
        });

        // Observe HomeViewModel (for Artists and Recommended Songs)
        homeViewModel.isLoadingArtists().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d(TAG, "Library Artists loading state: " + isLoading);
            if (artistsLoadingIndicator != null) {
                artistsLoadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        homeViewModel.getArtists().observe(getViewLifecycleOwner(), artists -> {
            if (artists != null) {
                Log.d(TAG, "Updating library artists adapter with " + artists.size() + " items.");
                artistAdapter.submitList(artists); // Submit to artist adapter
                if (artistsLoadingIndicator != null) {
                    artistsLoadingIndicator.setVisibility(View.GONE);
                }
            } else {
                artistAdapter.submitList(Collections.emptyList());
            }
        });

        homeViewModel.isLoadingSongs().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d(TAG, "Recommended songs loading state: " + isLoading);
            if (songsLoadingIndicator != null) {
                songsLoadingIndicator.setVisibility(isLoading && recommendedSongsRecyclerView.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
            }
        });
        homeViewModel.getRecommendedSongs().observe(getViewLifecycleOwner(), songs -> {
            if (recommendedSongsRecyclerView != null && recommendedSongsRecyclerView.getVisibility() == View.VISIBLE) {
                if (songs != null) {
                    Log.d(TAG, "Updating recommended songs adapter with " + songs.size() + " items.");
                    recommendedSongAdapter.submitList(songs);
                    if (songsLoadingIndicator != null) {
                        songsLoadingIndicator.setVisibility(View.GONE);
                    }
                } else {
                    recommendedSongAdapter.submitList(Collections.emptyList());
                }
            } else {
                Log.d(TAG, "Recommended songs section not visible, skipping adapter update.");
            }
        });

        // Observe General Error Message from HomeViewModel
        homeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                // Check which loading indicator is visible to guess the source, or show generic error
                boolean artistLoading = artistsLoadingIndicator != null && artistsLoadingIndicator.getVisibility() == View.VISIBLE;
                boolean songLoading = songsLoadingIndicator != null && songsLoadingIndicator.getVisibility() == View.VISIBLE;
                String prefix = artistLoading ? "Artist Error: " : (songLoading ? "Song Error: " : "Error: ");
                Toast.makeText(getContext(), prefix + error, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error from HomeViewModel: " + error);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify Playlist views
        if (playlistsRecyclerView != null) playlistsRecyclerView.setAdapter(null);
        playlistsRecyclerView = null;
        playlistsLoadingIndicator = null;
        likedSongsCard = null;
        playlistAdapter = null;

        // Nullify Recommended Song views
        if (recommendedSongsRecyclerView != null) recommendedSongsRecyclerView.setAdapter(null);
        recommendSongsButton = null;
        recommendedSongsHeader = null;
        recommendedSongsRecyclerView = null;
        songsLoadingIndicator = null;
        recommendedSongAdapter = null;

        // --- Nullify Artist Views ---
        if (artistsRecyclerView != null) artistsRecyclerView.setAdapter(null);
        artistsRecyclerView = null;
        artistsLoadingIndicator = null;
        artistAdapter = null;
        // --------------------------

        Log.d(TAG, "onDestroyView called");
    }
}