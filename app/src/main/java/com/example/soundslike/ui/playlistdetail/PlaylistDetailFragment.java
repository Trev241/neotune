package com.example.soundslike.ui.playlistdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // Ensure this is imported
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundslike.R;
import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class PlaylistDetailFragment extends Fragment {

    private PlaylistDetailViewModel viewModel;
    private RecommendedSongAdapter songAdapter;
    private RecyclerView songsRecyclerView;
    private ImageView headerImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ProgressBar loadingIndicator; // Make sure this exists in your layout

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_detail, container, false);

        // --- Standard ViewModel Instantiation ---
        // This works correctly even when the ViewModel constructor doesn't need SavedStateHandle
        viewModel = new ViewModelProvider(this).get(PlaylistDetailViewModel.class);
        // --------------------------------------

        songsRecyclerView = view.findViewById(R.id.songs_recycler_view);
        headerImageView = view.findViewById(R.id.header_image);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        toolbar = view.findViewById(R.id.toolbar);
        // Make sure you have a ProgressBar with this ID in fragment_playlist_detail.xml
        loadingIndicator = view.findViewById(R.id.loading_indicator_detail);

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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    private void setupRecyclerView() {
        songAdapter = new RecommendedSongAdapter();
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsRecyclerView.setAdapter(songAdapter);
        songsRecyclerView.setNestedScrollingEnabled(false); // Helps with CoordinatorLayout scrolling
    }

    private void observeViewModel() {
        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (loadingIndicator != null) { // Add null check
                loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getPlaylistDetails().observe(getViewLifecycleOwner(), playlistDetail -> {
            if (playlistDetail != null) {
                collapsingToolbarLayout.setTitle(playlistDetail.getName());

                Glide.with(this)
                        .load(playlistDetail.getCoverImageUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(headerImageView);

                if (playlistDetail.getSongs() != null) {
                    songAdapter.submitList(playlistDetail.getSongs());
                } else {
                    songAdapter.submitList(java.util.Collections.emptyList());
                }
            } else {
                // Handle null case (e.g., initial state or error)
                collapsingToolbarLayout.setTitle("Playlist"); // Default title
                headerImageView.setImageResource(R.drawable.ic_launcher_background);
                songAdapter.submitList(java.util.Collections.emptyList());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (songsRecyclerView != null) {
            songsRecyclerView.setAdapter(null); // Important to prevent leaks
        }
        songsRecyclerView = null;
        headerImageView = null;
        collapsingToolbarLayout = null;
        toolbar = null;
        loadingIndicator = null;
    }
}