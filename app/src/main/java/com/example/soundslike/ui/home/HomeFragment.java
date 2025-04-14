package com.example.soundslike.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // Import correct ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.GenreAdapter; // Import adapters
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Find RecyclerViews
        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);
        genresRecyclerView = view.findViewById(R.id.genres_recycler_view);
        recommendedRecyclerView = view.findViewById(R.id.recommended_recycler_view);

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
        // Set LayoutManager programmatically for clarity
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
        recommendedRecyclerView.setAdapter(recommendedSongAdapter);
    }

    private void observeViewModel() {
        homeViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null) {
                playlistAdapter.submitList(playlists);
            }
        });

        homeViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                genreAdapter.submitList(genres);
            }
        });

        homeViewModel.getRecommendedSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                recommendedSongAdapter.submitList(songs);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        playlistsRecyclerView.setAdapter(null);
        genresRecyclerView.setAdapter(null);
        recommendedRecyclerView.setAdapter(null);
        playlistsRecyclerView = null;
        genresRecyclerView = null;
        recommendedRecyclerView = null;
        playlistAdapter = null;
        genreAdapter = null;
        recommendedSongAdapter = null;
    }
}