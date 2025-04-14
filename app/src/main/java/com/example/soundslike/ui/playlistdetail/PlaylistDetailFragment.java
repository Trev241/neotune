package com.example.soundslike.ui.playlistdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class PlaylistDetailFragment extends Fragment {

    private PlaylistDetailViewModel viewModel;
    private RecommendedSongAdapter songAdapter;
    private RecyclerView songsRecyclerView;
    private ImageView headerImageView;
    private TextView headerTitleTextView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_detail, container, false);

        viewModel = new ViewModelProvider(this).get(PlaylistDetailViewModel.class);

        songsRecyclerView = view.findViewById(R.id.songs_recycler_view);
        headerImageView = view.findViewById(R.id.header_image);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        toolbar = view.findViewById(R.id.toolbar);

        setupToolbar(view);
        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupToolbar(View view) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back); // Use your back arrow drawable
        toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    private void setupRecyclerView() {
        songAdapter = new RecommendedSongAdapter(); // Reusing this adapter
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsRecyclerView.setAdapter(songAdapter);
    }

    private void observeViewModel() {
        viewModel.getPlaylistDetails().observe(getViewLifecycleOwner(), playlist -> {
            if (playlist != null) {
                headerImageView.setImageResource(playlist.getCoverArtResId());
                collapsingToolbarLayout.setTitle(playlist.getName());
            }
        });

        viewModel.getPlaylistSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                songAdapter.submitList(songs);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        songsRecyclerView = null; // Avoid memory leaks
        headerImageView = null;
        collapsingToolbarLayout = null;
        toolbar = null;
    }
}