package com.example.soundslike.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast; // Import Toast

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter;

public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;
    private RecyclerView suggestionsRecyclerView;
    private RecommendedSongAdapter suggestionsAdapter;
    private ProgressBar loadingIndicator;
    // Add reference for search input layout if needed later
    // private com.google.android.material.textfield.TextInputLayout searchInputLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        // Find views
        suggestionsRecyclerView = view.findViewById(R.id.suggestions_recycler_view);
        // Ensure this ID exists in fragment_explore.xml
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        // searchInputLayout = view.findViewById(R.id.search_input_layout);

        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupRecyclerView() {
        suggestionsAdapter = new RecommendedSongAdapter();
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        suggestionsRecyclerView.setAdapter(suggestionsAdapter);
    }

    private void observeViewModel() {
        // Observe loading state
        exploreViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (loadingIndicator != null) {
                loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            // Optionally hide RecyclerView while loading
            // suggestionsRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Observe error messages
        exploreViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                // Optionally clear the error message in ViewModel after showing
                // exploreViewModel.clearErrorMessage();
            }
        });

        // Observe suggested songs
        exploreViewModel.getSuggestedSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                suggestionsAdapter.submitList(songs);
            } else {
                suggestionsAdapter.submitList(java.util.Collections.emptyList()); // Clear list if null
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (suggestionsRecyclerView != null) {
            suggestionsRecyclerView.setAdapter(null); // Clear adapter
        }
        suggestionsRecyclerView = null;
        loadingIndicator = null;
        suggestionsAdapter = null;
        // searchInputLayout = null;
    }
}