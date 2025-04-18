package com.example.soundslike.ui.song;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast; // Import Toast

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide; // Import Glide
import com.example.soundslike.R;
import com.example.soundslike.data.models.Song; // Import Song model
import com.example.soundslike.ui.viewmodels.PlaybackViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SongViewFragment extends Fragment {
    private static final String TAG = "SongViewFragment";

    private PlaybackViewModel playbackViewModel;

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
    private ImageButton queueButton; // Renamed variable for clarity (maps to button_queue ID)
    private ImageButton shuffleButton;

    private boolean isTrackingSeekBar = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_view, container, false);

        playbackViewModel = new ViewModelProvider(requireActivity()).get(PlaybackViewModel.class);

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
        queueButton = view.findViewById(R.id.button_queue); // Find the button by its ID
        shuffleButton = view.findViewById(R.id.button_shuffle);

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
                // Handle case where popping back isn't possible
                requireActivity().finish(); // Example: finish activity
            }
        });

        playPauseButton.setOnClickListener(v -> playbackViewModel.togglePlayPause());
        previousButton.setOnClickListener(v -> playbackViewModel.skipToPrevious());
        nextButton.setOnClickListener(v -> playbackViewModel.skipToNext());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int userProgress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    userProgress = progress;
                    currentTimeTextView.setText(formatMillis(progress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { isTrackingSeekBar = true; }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingSeekBar = false;
                playbackViewModel.seekTo(userProgress);
            }
        });

        likeButton.setOnClickListener(v -> {
            boolean isLiked = !likeButton.isSelected();
            likeButton.setSelected(isLiked);
            likeButton.setImageResource(isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
            // TODO: Send API call later
        });

        // --- Updated Queue/Add Button Listener ---
        queueButton.setOnClickListener(v -> {
            Song currentSong = playbackViewModel.getCurrentSong().getValue();
            if (currentSong != null && currentSong.getId() != null && !currentSong.getId().equals("unknown")) {
                String songId = currentSong.getId();
                Log.d(TAG, "Add to Playlist button clicked for song: " + songId);

                // Create and show the bottom sheet
                AddToPlaylistBottomSheetFragment bottomSheet = AddToPlaylistBottomSheetFragment.newInstance(songId);
                // Use getChildFragmentManager() when showing a DialogFragment from within a Fragment
                bottomSheet.show(getChildFragmentManager(), AddToPlaylistBottomSheetFragment.TAG);

            } else {
                Log.w(TAG, "Add to Playlist button clicked but current song or song ID is null/invalid");
                Toast.makeText(getContext(), "Cannot add song: Information missing", Toast.LENGTH_SHORT).show();
            }
        });
        // --------------------------------------

        shuffleButton.setOnClickListener(v -> {
            boolean isShuffleOn = !shuffleButton.isSelected();
            shuffleButton.setSelected(isShuffleOn);
            shuffleButton.setAlpha(isShuffleOn ? 1.0f : 0.5f);
            // TODO: Tell Playback Service later
        });
    }

    private void observeViewModel() {
        playbackViewModel.getCurrentSong().observe(getViewLifecycleOwner(), song -> {
            Log.d(TAG, "Observed current song change: " + (song != null ? song.getTitle() : "null"));
            if (song != null) {
                titleTextView.setText(song.getTitle());
                artistTextView.setText(song.getArtistName()); // Use getArtistName

                // --- Load Album Art using Glide ---
                if (getView() != null && getContext() != null) { // Check context as well
                    Glide.with(this) // Use fragment context
                            .load(song.getThumbnailUrl()) // Load URL
                            .placeholder(R.drawable.ic_genre_placeholder) // Placeholder
                            .error(R.drawable.ic_genre_placeholder) // Error placeholder
                            .into(albumArtImageView);
                }
                // ----------------------------------

            } else {
                // Reset UI when song is null
                titleTextView.setText("");
                artistTextView.setText("");
                if (getContext() != null) { // Check context before loading resource
                    albumArtImageView.setImageResource(R.drawable.ic_genre_placeholder);
                }
                totalTimeTextView.setText(formatMillis(0));
                currentTimeTextView.setText(formatMillis(0));
                seekBar.setMax(0);
                seekBar.setProgress(0);
            }
        });

        playbackViewModel.isPlaying().observe(getViewLifecycleOwner(), isPlaying -> {
            Log.d(TAG, "Observed isPlaying change: " + isPlaying);
            playPauseButton.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        });

        playbackViewModel.getCurrentDuration().observe(getViewLifecycleOwner(), duration -> {
            Log.d(TAG, "Observed duration change: " + duration);
            if (duration != null && duration > 0) { // Check for null
                totalTimeTextView.setText(formatMillis(duration));
                seekBar.setMax(duration.intValue()); // Cast to int
            } else {
                totalTimeTextView.setText(formatMillis(0));
                seekBar.setMax(0);
            }
        });

        playbackViewModel.getCurrentPosition().observe(getViewLifecycleOwner(), position -> {
            if (!isTrackingSeekBar && position != null) { // Check for null
                seekBar.setProgress(position.intValue()); // Cast to int
                currentTimeTextView.setText(formatMillis(position));
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
        shuffleButton = null;
    }
}