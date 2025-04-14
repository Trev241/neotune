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

// No changes needed from the Java version generated in the previous step,
// just ensuring this is the complete file content.
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
        // Inflate the correct fragment layout
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize ViewModel
        // Use requireActivity() or this for ViewModelStoreOwner depending on scope needed
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Find RecyclerViews
        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);
        genresRecyclerView = view.findViewById(R.id.genres_recycler_view);
        recommendedRecyclerView = view.findViewById(R.id.recommended_recycler_view);

        // Setup RecyclerViews
        setupPlaylistsRecyclerView();
        setupGenresRecyclerView();
        setupRecommendedRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe LiveData from ViewModel
        observeViewModel();
    }

    private void setupPlaylistsRecyclerView() {
        playlistAdapter = new PlaylistAdapter();
        // Set LayoutManager programmatically for clarity
        playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        playlistsRecyclerView.setAdapter(playlistAdapter);
        // Optional: Add item decoration for spacing if needed
    }

    private void setupGenresRecyclerView() {
        genreAdapter = new GenreAdapter();
        genresRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        genresRecyclerView.setAdapter(genreAdapter);
        // Optional: Add item decoration
    }

    private void setupRecommendedRecyclerView() {
        recommendedSongAdapter = new RecommendedSongAdapter();
        // Set LayoutManager programmatically
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendedRecyclerView.setAdapter(recommendedSongAdapter);
        // Disable nested scrolling for the vertical RecyclerView inside NestedScrollView
        // This is handled by android:nestedScrollingEnabled="false" in the XML
    }

    private void observeViewModel() {
        // Observe playlists
        homeViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            // Update the adapter
            if (playlists != null) {
                playlistAdapter.submitList(playlists);
            }
        });

        // Observe genres
        homeViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                genreAdapter.submitList(genres);
            }
        });

        // Observe recommended songs
        homeViewModel.getRecommendedSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                recommendedSongAdapter.submitList(songs);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Avoid memory leaks by nulling out references to views that hold context indirectly
        playlistsRecyclerView.setAdapter(null);
        genresRecyclerView.setAdapter(null);
        recommendedRecyclerView.setAdapter(null);
        playlistsRecyclerView = null;
        genresRecyclerView = null;
        recommendedRecyclerView = null;
        // Adapters don't strictly need nulling unless they hold context/listeners improperly
        playlistAdapter = null;
        genreAdapter = null;
        recommendedSongAdapter = null;
    }
}