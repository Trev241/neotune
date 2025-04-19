package com.example.soundslike.ui.playlists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar; // Import ProgressBar
import android.widget.Toast; // Import Toast

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.PlaylistAdapter;

public class PlaylistsFragment extends Fragment {

    private PlaylistsViewModel playlistsViewModel;
    private RecyclerView playlistsRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private ProgressBar loadingIndicator; // Add ProgressBar reference

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        playlistsViewModel = new ViewModelProvider(this).get(PlaylistsViewModel.class);

        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);
        // --- Find ProgressBar (This ID should now exist in the layout) ---
        loadingIndicator = view.findViewById(R.id.loading_indicator_playlists_fragment);
        // ----------------------------------------------------

        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupRecyclerView() {
        playlistAdapter = new PlaylistAdapter();
        int numberOfColumns = 2; // Or your desired column count
        playlistsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        playlistsRecyclerView.setAdapter(playlistAdapter);
    }

    private void observeViewModel() {
        // Observe Loading State
        playlistsViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (loadingIndicator != null) {
                loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            // Optionally hide RecyclerView while loading
            // playlistsRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Observe Error Messages
        playlistsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                // Optionally clear error in VM
            }
        });

        // Observe Playlist Data
        playlistsViewModel.getUserPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            // Loading indicator is handled separately now
            if (playlists != null) {
                playlistAdapter.submitList(playlists);
            } else {
                playlistAdapter.submitList(java.util.Collections.emptyList());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (playlistsRecyclerView != null) {
            playlistsRecyclerView.setAdapter(null); // Clear adapter
        }
        playlistsRecyclerView = null;
        loadingIndicator = null; // Nullify reference
        playlistAdapter = null;
    }
}