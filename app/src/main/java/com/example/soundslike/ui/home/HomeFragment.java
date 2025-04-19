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
import com.example.soundslike.ui.adapters.GenreAdapter;
import com.example.soundslike.ui.adapters.PlaylistAdapter;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView playlistsRecyclerView;
    private RecyclerView genresRecyclerView;
    private RecyclerView recommendedRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private GenreAdapter genreAdapter;
    private RecommendedSongAdapter recommendedSongAdapter;
    // Add ProgressBars if you want separate loading indicators
    private ProgressBar playlistsLoadingIndicator;
    private ProgressBar songsLoadingIndicator;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Find RecyclerViews
        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);
        genresRecyclerView = view.findViewById(R.id.genres_recycler_view);
        recommendedRecyclerView = view.findViewById(R.id.recommended_recycler_view);
        // Find ProgressBars (Add IDs to fragment_home.xml if needed)
        // Example IDs:
        // playlistsLoadingIndicator = view.findViewById(R.id.loading_indicator_home_playlists);
        // songsLoadingIndicator = view.findViewById(R.id.loading_indicator_home_songs);


        setupPlaylistsRecyclerView();
        setupGenresRecyclerView();
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

    private void setupGenresRecyclerView() {
        genreAdapter = new GenreAdapter();
        genresRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        genresRecyclerView.setAdapter(genreAdapter);
    }

    private void setupRecommendedRecyclerView() {
        recommendedSongAdapter = new RecommendedSongAdapter();
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Disable nested scrolling for the vertical RecyclerView inside NestedScrollView
        recommendedRecyclerView.setNestedScrollingEnabled(false);
        recommendedRecyclerView.setAdapter(recommendedSongAdapter);
    }

    private void observeViewModel() {
        // Observe Playlist Loading/Error/Data
        homeViewModel.isLoadingPlaylists().observe(getViewLifecycleOwner(), isLoading -> {
            // Show/hide playlistsLoadingIndicator if you added one
            Log.d("HomeFragment", "Playlist loading state: " + isLoading);
            if (playlistsLoadingIndicator != null) {
                playlistsLoadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        homeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                // Optionally clear error in VM
            }
        });

        homeViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null) {
                Log.d("HomeFragment", "Updating playlists adapter with " + playlists.size() + " items.");
                playlistAdapter.submitList(playlists);
            } else {
                playlistAdapter.submitList(java.util.Collections.emptyList());
            }
        });

        // Observe Genres (still mock)
        homeViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                genreAdapter.submitList(genres);
            }
        });

        // Observe Recommended Songs Loading/Data
        homeViewModel.isLoadingSongs().observe(getViewLifecycleOwner(), isLoading -> {
            // Show/hide songsLoadingIndicator if you added one
            Log.d("HomeFragment", "Songs loading state: " + isLoading);
            if (songsLoadingIndicator != null) {
                songsLoadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        homeViewModel.getRecommendedSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                Log.d("HomeFragment", "Updating songs adapter with " + songs.size() + " items.");
                recommendedSongAdapter.submitList(songs);
            } else {
                recommendedSongAdapter.submitList(java.util.Collections.emptyList());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify views and adapters
        if (playlistsRecyclerView != null) playlistsRecyclerView.setAdapter(null);
        if (genresRecyclerView != null) genresRecyclerView.setAdapter(null);
        if (recommendedRecyclerView != null) recommendedRecyclerView.setAdapter(null);
        playlistsRecyclerView = null;
        genresRecyclerView = null;
        recommendedRecyclerView = null;
        playlistAdapter = null;
        genreAdapter = null;
        recommendedSongAdapter = null;
        playlistsLoadingIndicator = null;
        songsLoadingIndicator = null;
    }
}