package com.example.soundslike.ui.artistdetail;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.soundslike.R;
import com.example.soundslike.data.models.Artist;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Random;

public class ArtistDetailFragment extends Fragment {

    private static final String TAG = "ArtistDetailFragment";

    private ArtistDetailViewModel viewModel;
    private ImageView headerImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView bioTextView;
    private ProgressBar loadingIndicator;
    private final Random random = new Random(); // For placeholder image generation

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        viewModel = new ViewModelProvider(this).get(ArtistDetailViewModel.class);

        // Find views
        headerImageView = view.findViewById(R.id.header_image_artist);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout_artist);
        toolbar = view.findViewById(R.id.toolbar_artist);
        bioTextView = view.findViewById(R.id.artist_bio_text);
        loadingIndicator = view.findViewById(R.id.loading_indicator_artist_detail);

        setupToolbar();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back); // Use your back arrow drawable
        toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    private void observeViewModel() {
        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getArtistDetails().observe(getViewLifecycleOwner(), artist -> {
            if (artist != null) {
                collapsingToolbarLayout.setTitle(artist.getName());
                bioTextView.setText(TextUtils.isEmpty(artist.getBio()) ? "No biography available." : artist.getBio());

                // Load Header Image using Glide
                String imageUrl = artist.getImageUrl();
                String placeholderUrl = null;

                if (TextUtils.isEmpty(imageUrl)) {
                    String baseUrl = "https://picsum.photos/seed/";
                    int imageSize = 400;
                    String seed = artist.getId() != null ? artist.getId() : "artistDetail" + random.nextInt();
                    placeholderUrl = baseUrl + seed + "/" + imageSize;
                    Log.d(TAG, "No cover URL for artist header, using placeholder: " + placeholderUrl);
                }

                if (getContext() != null) {
                    Glide.with(this)
                            .load(TextUtils.isEmpty(imageUrl) ? placeholderUrl : imageUrl)
                            .placeholder(R.drawable.ic_genre_placeholder) // Placeholder while loading
                            .error(R.drawable.ic_genre_placeholder)       // Placeholder on error
                            .centerCrop()
                            .into(headerImageView);
                }
            } else {
                // Handle case where artist details are null after loading attempt
                collapsingToolbarLayout.setTitle("Artist");
                bioTextView.setText("Failed to load artist details.");
                if (getContext() != null) {
                    headerImageView.setImageResource(R.drawable.ic_genre_placeholder); // Reset image
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        headerImageView = null;
        collapsingToolbarLayout = null;
        toolbar = null;
        bioTextView = null;
        loadingIndicator = null;
    }
}