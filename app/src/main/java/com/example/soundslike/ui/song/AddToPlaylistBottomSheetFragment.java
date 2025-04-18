package com.example.soundslike.ui.song;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.ui.adapters.PlaylistSelectionAdapter;
import com.example.soundslike.ui.playlists.PlaylistsViewModel; // Use this to get playlists
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Collections;

public class AddToPlaylistBottomSheetFragment extends BottomSheetDialogFragment implements PlaylistSelectionAdapter.OnPlaylistSelectedListener {

    public static final String TAG = "AddToPlaylistBottomSheet";
    private static final String ARG_SONG_ID = "song_id";

    private RecyclerView playlistRecyclerView;
    private PlaylistSelectionAdapter adapter;
    private ProgressBar loadingIndicator;
    private TextView createPlaylistButton;
    private PlaylistsViewModel playlistsViewModel; // To get user playlists

    private String songIdToAdd;

    // Factory method to create instance with arguments
    public static AddToPlaylistBottomSheetFragment newInstance(String songId) {
        AddToPlaylistBottomSheetFragment fragment = new AddToPlaylistBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SONG_ID, songId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songIdToAdd = getArguments().getString(ARG_SONG_ID);
        }
        // Get ViewModel scoped to the Activity or NavGraph to share playlist data
        // This assumes PlaylistsViewModel holds the list of user's playlists
        playlistsViewModel = new ViewModelProvider(requireActivity()).get(PlaylistsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_add_to_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistRecyclerView = view.findViewById(R.id.playlist_recycler_view);
        loadingIndicator = view.findViewById(R.id.loading_indicator_playlists);
        createPlaylistButton = view.findViewById(R.id.button_create_playlist);

        setupRecyclerView();
        setupListeners();
        observeViewModel();

        // TODO: Trigger fetching user playlists if PlaylistsViewModel doesn't load them automatically
        // For now, relies on mock data loaded in PlaylistsViewModel constructor
        // Example: playlistsViewModel.fetchUserPlaylistsIfNeeded();
    }

    // Make bottom sheet expand fully
    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(d -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) d;
            View bottomSheetInternal = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheetInternal != null) {
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
                // Optional: Set peek height if needed when not fully expanded
                // BottomSheetBehavior.from(bottomSheetInternal).setPeekHeight(getResources().getDisplayMetrics().heightPixels / 2);
            }
        });
        return dialog;
    }


    private void setupRecyclerView() {
        adapter = new PlaylistSelectionAdapter(this);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playlistRecyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        createPlaylistButton.setOnClickListener(v -> {
            // TODO: Implement Create New Playlist UI / Logic
            Toast.makeText(getContext(), "Create New Playlist clicked (Not Implemented)", Toast.LENGTH_SHORT).show();
            // dismiss(); // Optionally dismiss after click
        });
    }

    private void observeViewModel() {
        // Observe the playlists LiveData from the shared ViewModel
        // This currently gets the MOCK data from PlaylistsViewModel
        loadingIndicator.setVisibility(View.VISIBLE); // Show loading initially
        playlistsViewModel.getUserPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            loadingIndicator.setVisibility(View.GONE); // Hide loading
            if (playlists != null) {
                Log.d(TAG, "Observed " + playlists.size() + " playlists.");
                adapter.submitList(playlists);
            } else {
                Log.d(TAG, "Observed null playlist list.");
                adapter.submitList(Collections.emptyList());
                // Optionally show an error message TextView
            }
        });

        // TODO: Add observation for loading/error states if PlaylistsViewModel fetches data
        // playlistsViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> { ... });
        // playlistsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> { ... });
    }

    @Override
    public void onPlaylistSelected(Playlist playlist) {
        Log.d(TAG, "Selected playlist: " + playlist.getName() + " (ID: " + playlist.getId() + ")");
        Log.d(TAG, "Song to add: " + songIdToAdd);

        // TODO: Implement API call to add song to playlist
        // 1. Get auth token (requires auth implementation)
        // 2. Get PlaylistRepository instance (maybe via ViewModel or injection)
        // 3. Call repository method: addSong(playlist.getId(), songIdToAdd, token)
        // 4. Handle success/failure (show Toast, dismiss sheet)

        Toast.makeText(getContext(), "Adding '" + songIdToAdd + "' to '" + playlist.getName() + "' (Not Implemented)", Toast.LENGTH_SHORT).show();
        dismiss(); // Close the bottom sheet
    }
}