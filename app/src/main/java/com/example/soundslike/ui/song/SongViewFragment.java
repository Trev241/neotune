package com.example.soundslike.ui.song;

import android.os.Bundle;
// Removed Handler/Looper imports
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity; // Import FragmentActivity
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.soundslike.R;
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
    private ImageButton queueButton;
    private ImageButton shuffleButton;

    private boolean isTrackingSeekBar = false;
    // Removed Handler and Runnable


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_view, container, false);

        // Get Activity-Scoped ViewModel using requireActivity()
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
        queueButton = view.findViewById(R.id.button_queue);
        shuffleButton = view.findViewById(R.id.button_shuffle);

        setupUIListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
        // Removed setupProgressUpdater call
    }

    private void setupUIListeners() {
        backButton.setOnClickListener(v -> {
            if (NavHostFragment.findNavController(this).popBackStack()) {
                // Successfully popped back
            } else {
                // Handle case where popping back isn't possible (e.g., deep link)
                // Maybe navigate to home or finish activity
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
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingSeekBar = false;
                playbackViewModel.seekTo(userProgress);
            }
        });

        likeButton.setOnClickListener(v -> {
            boolean isLiked = !likeButton.isSelected(); // Toggle state
            likeButton.setSelected(isLiked);
            likeButton.setImageResource(isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
            // TODO: Send API call later
        });

        shuffleButton.setOnClickListener(v -> {
            boolean isShuffleOn = !shuffleButton.isSelected(); // Toggle state
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
                artistTextView.setText(song.getArtist());
                // TODO: Use Glide/Coil later for real album art URI from API
                albumArtImageView.setImageResource(song.getAlbumArtResId()); // Use mock Res ID for now
            } else {
                titleTextView.setText("");
                artistTextView.setText("");
                albumArtImageView.setImageResource(R.drawable.ic_genre_placeholder);
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
            if (duration > 0) {
                totalTimeTextView.setText(formatMillis(duration));
                seekBar.setMax((int) duration.longValue());
            } else {
                totalTimeTextView.setText(formatMillis(0));
                seekBar.setMax(0);
            }
        });

        // Observe current position directly from ViewModel
        playbackViewModel.getCurrentPosition().observe(getViewLifecycleOwner(), position -> {
            if (!isTrackingSeekBar) {
                seekBar.setProgress(position.intValue());
                currentTimeTextView.setText(formatMillis(position));
            }
        });
    }

    // Removed setupProgressUpdater, startProgressUpdater, stopProgressUpdater

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
        // Removed stopProgressUpdater call
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