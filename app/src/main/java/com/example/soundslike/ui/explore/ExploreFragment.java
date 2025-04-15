package com.example.soundslike.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar; // Import ProgressBar

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter; // Reuse adapter

public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;
    private RecyclerView suggestionsRecyclerView;
    private RecommendedSongAdapter suggestionsAdapter;
    private ProgressBar loadingIndicator; // Optional loading indicator

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the correct layout
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        // Find views
        suggestionsRecyclerView = view.findViewById(R.id.suggestions_recycler_view);
        loadingIndicator = view.findViewById(R.id.loading_indicator); // Optional

        setupRecyclerView();

        // TODO: Setup search listener later
        // EditText searchEditText = view.findViewById(R.id.search_edit_text);
        // searchEditText.setOnEditorActionListener(...)

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupRecyclerView() {
        suggestionsAdapter = new RecommendedSongAdapter(); // Reusing adapter
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        suggestionsRecyclerView.setAdapter(suggestionsAdapter);
    }

    private void observeViewModel() {
        exploreViewModel.getSuggestedSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                suggestionsAdapter.submitList(songs);
            }
        });

        // Optional: Observe loading state
        // exploreViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
        //     loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        //     suggestionsRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        // });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        suggestionsRecyclerView.setAdapter(null); // Clear adapter
        suggestionsRecyclerView = null;
        loadingIndicator = null;
        suggestionsAdapter = null;
    }
}