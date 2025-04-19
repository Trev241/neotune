package com.example.soundslike.ui.song;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color; // Import Color
import android.graphics.drawable.ColorDrawable; // Import ColorDrawable
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window; // Import Window
import android.widget.EditText;
import android.widget.FrameLayout; // Import FrameLayout
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.ui.adapters.PlaylistSelectionAdapter;
import com.example.soundslike.ui.playlists.PlaylistsViewModel;
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
    private PlaylistsViewModel playlistsViewModel;

    private String songIdToAdd;

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

        playlistsViewModel.fetchUserPlaylists();
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // --- Attempt to remove potential spacing ---
        Window window = dialog.getWindow();
        if (window != null) {
            // Make window background transparent, letting the layout background show
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // Optional: Adjust flags if needed, but often background is enough
            // window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // -----------------------------------------

        dialog.setOnShowListener(d -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) d;
            FrameLayout bottomSheetInternal = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheetInternal != null) {
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
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
        createPlaylistButton.setOnClickListener(v -> showCreatePlaylistDialog());
    }

    private void observeViewModel() {
        playlistsViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            createPlaylistButton.setEnabled(!isLoading);
            playlistRecyclerView.setEnabled(!isLoading);
        });

        playlistsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        playlistsViewModel.getUserPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null) {
                Log.d(TAG, "Updating playlist list in sheet: " + playlists.size() + " items");
                adapter.submitList(playlists);
            } else {
                adapter.submitList(Collections.emptyList());
            }
        });

        playlistsViewModel.getActionFeedback().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                playlistsViewModel.clearActionFeedback();
            }
        });
    }

    @Override
    public void onPlaylistSelected(Playlist playlist) {
        Log.d(TAG, "Selected playlist: " + playlist.getName() + " (ID: " + playlist.getId() + ")");
        Log.d(TAG, "Song to add: " + songIdToAdd);
        playlistsViewModel.addSongToPlaylist(playlist.getId(), playlist.getName(), songIdToAdd);
        dismiss();
    }

    private void showCreatePlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create New Playlist");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setHint("Playlist Name");
        int paddingDp = 16;
        float density = getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);
        FrameLayout container = new FrameLayout(requireContext());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = paddingPixel;
        params.rightMargin = paddingPixel;
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);

        builder.setPositiveButton("Create & Add Song", (dialog, which) -> {
            String playlistName = input.getText().toString().trim();
            if (!playlistName.isEmpty() && songIdToAdd != null) {
                playlistsViewModel.createPlaylistAndAddSong(playlistName, songIdToAdd);
            } else if (playlistName.isEmpty()) {
                Toast.makeText(getContext(), "Playlist name cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error: Song ID missing", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}