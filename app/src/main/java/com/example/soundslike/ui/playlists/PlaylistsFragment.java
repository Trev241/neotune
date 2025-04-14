package com.example.soundslike.ui.playlists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        playlistsViewModel = new ViewModelProvider(this).get(PlaylistsViewModel.class);

        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);

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

        int numberOfColumns = 2;
        playlistsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        playlistsRecyclerView.setAdapter(playlistAdapter);
    }

    private void observeViewModel() {
        playlistsViewModel.getUserPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null) {
                playlistAdapter.submitList(playlists);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        playlistsRecyclerView = null;
    }
}