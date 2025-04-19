package com.example.soundslike.ui.playlistdetail;

import android.os.Bundle;
import android.text.TextUtils; // Import TextUtils
import android.util.Log; // Import Log
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Ensure Glide is imported
import com.example.soundslike.R;
import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Random; // Import Random for placeholder generation

public class PlaylistDetailFragment extends Fragment {

    private PlaylistDetailViewModel viewModel;
    private RecommendedSongAdapter songAdapter;
    private RecyclerView songsRecyclerView;
    private ImageView headerImageView; // The large header image
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ProgressBar loadingIndicator;
    private final Random random = new Random(); // For placeholder image generation

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_detail, container, false);

        viewModel = new ViewModelProvider(this).get(PlaylistDetailViewModel.class);

        songsRecyclerView = view.findViewById(R.id.songs_recycler_view);
        headerImageView = view.findViewById(R.id.header_image); // Find the header ImageView
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        toolbar = view.findViewById(R.id.toolbar);
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
        songsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void observeViewModel() {
        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (loadingIndicator != null) {
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

                // --- Load Header Image using Glide ---
                String imageUrl = playlistDetail.getCoverImageUrl();
                String placeholderUrl = null;

                if (TextUtils.isEmpty(imageUrl)) {
                    // Generate a random placeholder URL if API URL is missing
                    String baseUrl = "https://picsum.photos/seed/";
                    int imageSize = 400; // Maybe request a larger size for the header
                    String seed = playlistDetail.getId() != null ? playlistDetail.getId() : "detail" + random.nextInt();
                    placeholderUrl = baseUrl + seed + "/" + imageSize;
                    Log.d("PlaylistDetailFragment", "No cover URL for header, using placeholder: " + placeholderUrl);
                }

                // Check context before loading
                if (getContext() != null) {
                    Glide.with(this) // Use fragment context
                            .load(TextUtils.isEmpty(imageUrl) ? placeholderUrl : imageUrl) // Load original or placeholder
                            .placeholder(R.drawable.ic_launcher_background) // Placeholder while loading
                            .error(R.drawable.ic_launcher_background)       // Placeholder on error
                            .centerCrop() // Adjust scale type as needed
                            .into(headerImageView);
                }
                // ------------------------------------

                // Submit songs to the adapter
                if (playlistDetail.getSongs() != null) {
                    songAdapter.submitList(playlistDetail.getSongs());
                } else {
                    songAdapter.submitList(java.util.Collections.emptyList());
                }
            } else {
                // Handle case where playlist details are null
                collapsingToolbarLayout.setTitle("Playlist");
                if (getContext() != null) { // Check context
                    headerImageView.setImageResource(R.drawable.ic_launcher_background); // Reset image
                }
                songAdapter.submitList(java.util.Collections.emptyList());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (songsRecyclerView != null) {
            songsRecyclerView.setAdapter(null);
        }
        songsRecyclerView = null;
        headerImageView = null;
        collapsingToolbarLayout = null;
        toolbar = null;
        loadingIndicator = null;
    }
}