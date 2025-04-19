package com.example.soundslike.ui.song;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.ui.playlists.PlaylistsViewModel; // Import PlaylistsViewModel
import com.example.soundslike.ui.viewmodels.PlaybackViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SongViewFragment extends Fragment {
    private static final String TAG = "SongViewFragment";

    private PlaybackViewModel playbackViewModel;
    private PlaylistsViewModel playlistsViewModel; // Still needed for add to playlist

    // View references
    private ImageButton backButton;
    private ShapeableImageView albumArtImageView;
    private TextView titleTextView;
    private TextView artistTextView;
    private TextView currentTimeTextView;
    private TextView totalTimeTextView;
    private SeekBar seekBar;
    private ImageButton playPauseButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private ImageButton likeButton;
    private ImageButton queueButton;
    // Removed Shuffle Button

    private boolean isTrackingSeekBar = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_view, container, false);

        // Get ViewModels scoped to the Activity/NavGraph
        playbackViewModel = new ViewModelProvider(requireActivity()).get(PlaybackViewModel.class);
        playlistsViewModel = new ViewModelProvider(requireActivity()).get(PlaylistsViewModel.class); // Get playlists VM

        // Find views
        backButton = view.findViewById(R.id.button_back);
        albumArtImageView = view.findViewById(R.id.image_album_art);
        titleTextView = view.findViewById(R.id.text_song_title);
        artistTextView = view.findViewById(R.id.text_artist_name);
        currentTimeTextView = view.findViewById(R.id.text_current_time);
        totalTimeTextView = view.findViewById(R.id.text_total_time);
        seekBar = view.findViewById(R.id.seek_bar);
        playPauseButton = view.findViewById(R.id.button_play_pause);
        previousButton = view.findViewById(R.id.button_previous);
        nextButton = view.findViewById(R.id.button_next);
        likeButton = view.findViewById(R.id.button_like);
        queueButton = view.findViewById(R.id.button_queue);
        // Removed Shuffle Button Find

        setupUIListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupUIListeners() {
        backButton.setOnClickListener(v -> {
            if (!NavHostFragment.findNavController(this).popBackStack()) {
                requireActivity().finish();
            }
        });


        playPauseButton.setOnClickListener(v -> playbackViewModel.togglePlayPause());

        // Next/Previous Button Listeners (Unchanged)
        previousButton.setOnClickListener(v -> {
            Log.d(TAG, "Previous button clicked");
            playbackViewModel.skipToPrevious();
        });
        nextButton.setOnClickListener(v -> {
            Log.d(TAG, "Next button clicked");
            playbackViewModel.skipToNext();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int userProgress = 0;
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    userProgress = progress;
                    currentTimeTextView.setText(formatMillis(progress));
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingSeekBar = true;
                Log.d(TAG, "SeekBar tracking started");
            }
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "SeekBar tracking stopped at " + userProgress);
                isTrackingSeekBar = false;
                playbackViewModel.seekTo(userProgress);
            }
        });

        // --- Reverted Like Button Listener ---
        likeButton.setOnClickListener(v -> {
            // Temporarily disable or show placeholder message
            Toast.makeText(getContext(), "Like function not implemented yet", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Like button clicked (functionality removed for now)");
            // Reset visual state if needed (though it shouldn't change now)
            likeButton.setSelected(false);
            likeButton.setImageResource(R.drawable.ic_heart);
        });
        // ------------------------------------

        queueButton.setOnClickListener(v -> {
            Song currentSong = playbackViewModel.getCurrentSong().getValue();
            if (currentSong != null && currentSong.getId() != null && !currentSong.getId().equals("unknown")) {
                AddToPlaylistBottomSheetFragment bottomSheet = AddToPlaylistBottomSheetFragment.newInstance(currentSong.getId());
                bottomSheet.show(getChildFragmentManager(), AddToPlaylistBottomSheetFragment.TAG);
            } else {
                Toast.makeText(getContext(), "Cannot add song: Information missing", Toast.LENGTH_SHORT).show();
            }
        });

        // Removed Shuffle Listener
    }

    private void observeViewModel() {
        playbackViewModel.getCurrentSong().observe(getViewLifecycleOwner(), song -> {
            Log.d(TAG, "Observed current song change: " + (song != null ? song.getTitle() + " (ID: " + song.getId() + ")" : "null"));
            if (song != null) {
                titleTextView.setText(song.getTitle());
                artistTextView.setText(song.getArtistName());
                if (isAdded() && getContext() != null) {
                    Glide.with(this)
                            .load(song.getThumbnailUrl())
                            .placeholder(R.drawable.ic_genre_placeholder)
                            .error(R.drawable.ic_genre_placeholder)
                            .into(albumArtImageView);
                }
                // --- REMOVED Liked State Observation TODO ---
                // Reset like button visually on song change
                likeButton.setSelected(false);
                likeButton.setImageResource(R.drawable.ic_heart);
                // -------------------------------------------

            } else {
                // Reset UI if song is null
                titleTextView.setText("");
                artistTextView.setText("");
                if (isAdded() && getContext() != null) {
                    Glide.with(this).clear(albumArtImageView);
                    albumArtImageView.setImageResource(R.drawable.ic_genre_placeholder);
                }
                totalTimeTextView.setText(formatMillis(0));
                currentTimeTextView.setText(formatMillis(0));
                seekBar.setMax(0);
                seekBar.setProgress(0);
                likeButton.setSelected(false);
                likeButton.setImageResource(R.drawable.ic_heart);
            }
        });

        playbackViewModel.isPlaying().observe(getViewLifecycleOwner(), isPlaying -> {
            Log.d(TAG, "Observed isPlaying change: " + isPlaying);
            playPauseButton.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
            playPauseButton.setContentDescription(getString(isPlaying ? R.string.cd_pause : R.string.cd_play));
        });

        playbackViewModel.getCurrentDuration().observe(getViewLifecycleOwner(), duration -> {
            Log.d(TAG, "Observed duration change: " + duration);
            if (duration != null && duration > 0) {
                totalTimeTextView.setText(formatMillis(duration));
                seekBar.setMax(duration.intValue());
            } else {
                totalTimeTextView.setText(formatMillis(0));
                seekBar.setMax(0);
            }
        });

        playbackViewModel.getCurrentPosition().observe(getViewLifecycleOwner(), position -> {
            if (!isTrackingSeekBar && position != null) {
                seekBar.setProgress(position.intValue());
                currentTimeTextView.setText(formatMillis(position));
            }
        });

        // Removed Shuffle Observation

        // Observe feedback from playlist actions (add to playlist)
        playlistsViewModel.getActionFeedback().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                playlistsViewModel.clearActionFeedback();
            }
        });
        playlistsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String formatMillis(long millis) {
        if (millis < 0) millis = 0;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(minutes);
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        backButton = null;
        albumArtImageView = null;
        titleTextView = null;
        artistTextView = null;
        currentTimeTextView = null;
        totalTimeTextView = null;
        seekBar = null;
        playPauseButton = null;
        previousButton = null;
        nextButton = null;
        likeButton = null;
        queueButton = null;
        // Removed Shuffle Button Nullify
        Log.d(TAG, "onDestroyView called");
    }
}