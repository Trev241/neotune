package com.example.soundslike.ui.playlists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager; // Import GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.PlaylistAdapter; // Reuse the adapter from Phase 2

public class PlaylistsFragment extends Fragment {

    private PlaylistsViewModel playlistsViewModel;
    private RecyclerView playlistsRecyclerView;
    private PlaylistAdapter playlistAdapter; // Reusing the adapter

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the correct layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        // Initialize ViewModel
        playlistsViewModel = new ViewModelProvider(this).get(PlaylistsViewModel.class);

        // Find RecyclerView
        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view); // Ensure this ID exists in fragment_playlists.xml

        // Setup RecyclerView
        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe LiveData from ViewModel
        observeViewModel();
    }

    private void setupRecyclerView() {
        playlistAdapter = new PlaylistAdapter(); // Create instance of the adapter

        // --- Choose Layout Manager ---
        // Option 1: Linear List
        // playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Option 2: Grid Layout (e.g., 2 columns) - Often looks better for playlists
        int numberOfColumns = 2;
        playlistsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        // --------------------------

        playlistsRecyclerView.setAdapter(playlistAdapter);
        // Optional: Add item decoration for spacing if needed
    }

    private void observeViewModel() {
        playlistsViewModel.getUserPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            // Update the adapter
            if (playlists != null) {
                playlistAdapter.submitList(playlists);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Avoid memory leaks
        playlistsRecyclerView = null;
        // Adapter can be left to GC if it doesn't hold strong references inappropriately
    }
}