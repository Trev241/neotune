package com.example.soundslike.ui.playlistdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar; // Use androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter; // Reuse adapter
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
        // Inflate the layout renamed in Phase 1 adjustment
        View view = inflater.inflate(R.layout.fragment_playlist_detail, container, false);

        viewModel = new ViewModelProvider(this).get(PlaylistDetailViewModel.class);

        // Find views (ensure IDs exist in fragment_playlist_detail.xml)
        songsRecyclerView = view.findViewById(R.id.songs_recycler_view); // ADD THIS ID TO THE RECYCLERVIEW IN XML
        headerImageView = view.findViewById(R.id.header_image); // ADD THIS ID TO THE IMAGEVIEW IN XML
        // The TextView in CollapsingToolbarLayout might not need an ID if we set title on Toolbar/CollapsingToolbar
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout); // ADD THIS ID
        toolbar = view.findViewById(R.id.toolbar); // ADD THIS ID

        setupToolbar(view);
        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();

        // TODO: Get playlistId from arguments if passed via navigation
        // String playlistId = PlaylistDetailFragmentArgs.fromBundle(getArguments()).getPlaylistId();
        // viewModel.loadPlaylistData(playlistId); // Load actual data later
    }

    private void setupToolbar(View view) {
        // Set up the toolbar for navigation
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back); // Use your back arrow drawable
        toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        // We'll set the title dynamically when data loads
    }

    private void setupRecyclerView() {
        songAdapter = new RecommendedSongAdapter(); // Reusing this adapter
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsRecyclerView.setAdapter(songAdapter);

        // TODO: Set item click listener on adapter to navigate to SongViewFragment
        // songAdapter.setOnItemClickListener(song -> {
        //     PlaylistDetailFragmentDirections.ActionPlaylistDetailFragmentToSongViewFragment action =
        //         PlaylistDetailFragmentDirections.actionPlaylistDetailFragmentToSongViewFragment(song.getId());
        //     NavHostFragment.findNavController(this).navigate(action);
        // });
    }

    private void observeViewModel() {
        viewModel.getPlaylistDetails().observe(getViewLifecycleOwner(), playlist -> {
            if (playlist != null) {
                // Update header
                // Use Glide/Coil here later to load URL
                headerImageView.setImageResource(playlist.getCoverArtResId());
                collapsingToolbarLayout.setTitle(playlist.getName()); // Set title on collapsing toolbar
                // Or set title on toolbar if you prefer: toolbar.setTitle(playlist.getName());
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