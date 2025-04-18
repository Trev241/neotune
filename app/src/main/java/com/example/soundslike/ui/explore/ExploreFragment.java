package com.example.soundslike.ui.explore;

import android.os.Bundle;
import android.text.TextUtils; // Import TextUtils
import android.util.Log; // Import Log
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects; // Import Objects

// --- Import for static emptyList ---
import static java.util.Collections.emptyList;
// -----------------------------------


public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;
    private RecyclerView suggestionsRecyclerView;
    private RecommendedSongAdapter songsAdapter; // Rename for clarity
    private ProgressBar loadingIndicator;
    private TextInputLayout searchInputLayout;
    private TextInputEditText searchEditText;
    private TextView headerTextView; // To change header text

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        // Find views
        suggestionsRecyclerView = view.findViewById(R.id.suggestions_recycler_view);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        searchInputLayout = view.findViewById(R.id.search_input_layout);
        searchEditText = view.findViewById(R.id.search_edit_text);
        headerTextView = view.findViewById(R.id.header_suggestions); // Find header TextView

        setupRecyclerView();
        setupSearchListener(); // Setup listener for search input

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupRecyclerView() {
        songsAdapter = new RecommendedSongAdapter(); // Use the adapter
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        suggestionsRecyclerView.setAdapter(songsAdapter);
    }

    // --- Setup Listener for Search Input ---
    private void setupSearchListener() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchEditText.getText() != null ? searchEditText.getText().toString() : "";
                Log.d("ExploreFragment", "Search submitted: " + query);
                exploreViewModel.searchSongs(query); // Trigger search in ViewModel
                // Hide keyboard (optional)
                android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) requireActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                if (imm != null && v != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true; // Consume the action
            }
            return false; // Let system handle other actions
        });
    }
    // --------------------------------------

    private void observeViewModel() {
        exploreViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (loadingIndicator != null) {
                loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        exploreViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                // Clear error in VM? exploreViewModel.clearErrorMessage();
            }
        });

        // Observe the combined display list
        exploreViewModel.getDisplaySongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                Log.d("ExploreFragment", "Updating adapter with " + songs.size() + " songs.");
                songsAdapter.submitList(songs); // Update the adapter
            } else {
                // --- Use static import ---
                songsAdapter.submitList(emptyList());
                // -------------------------
            }

            // Update header text based on whether it's suggestions or results
            // Check if ViewModel and its LiveData are initialized before accessing
            if (exploreViewModel != null && exploreViewModel.getSearchQuery() != null) {
                String currentQuery = exploreViewModel.getSearchQuery().getValue();
                if (headerTextView != null) { // Check if headerTextView is not null
                    if (TextUtils.isEmpty(currentQuery)) {
                        headerTextView.setText("Suggestions For You");
                    } else {
                        headerTextView.setText("Search Results");
                    }
                }
            } else if (headerTextView != null) {
                headerTextView.setText("Suggestions For You"); // Default if ViewModel/Query is null
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (suggestionsRecyclerView != null) {
            suggestionsRecyclerView.setAdapter(null);
        }
        suggestionsRecyclerView = null;
        loadingIndicator = null;
        songsAdapter = null;
        searchInputLayout = null;
        searchEditText = null;
        headerTextView = null;
    }
}