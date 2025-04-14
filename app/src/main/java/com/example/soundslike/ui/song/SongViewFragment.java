package com.example.soundslike.ui.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.soundslike.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SongViewFragment extends Fragment {

    private SongViewModel viewModel;

    // View references
    private ImageButton backButton;
    private ShapeableImageView albumArtImageView;
    private TextView titleTextView;
    private TextView artistTextView;
    private TextView currentTimeTextView;
    private TextView totalTimeTextView;
    private SeekBar seekBar;
    private ImageButton playPauseButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_view, container, false);

        viewModel = new ViewModelProvider(this).get(SongViewModel.class);

        backButton = view.findViewById(R.id.button_back);
        albumArtImageView = view.findViewById(R.id.image_album_art);
        titleTextView = view.findViewById(R.id.text_song_title);
        artistTextView = view.findViewById(R.id.text_artist_name);
        currentTimeTextView = view.findViewById(R.id.text_current_time);
        totalTimeTextView = view.findViewById(R.id.text_total_time);
        seekBar = view.findViewById(R.id.seek_bar);
        playPauseButton = view.findViewById(R.id.button_play_pause);

        setupUIListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void setupUIListeners() {
        backButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

    }

    private void observeViewModel() {
        viewModel.getCurrentSong().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                titleTextView.setText(song.getTitle());
                artistTextView.setText(song.getArtist());
                albumArtImageView.setImageResource(song.getAlbumArtResId());

                totalTimeTextView.setText(formatMillis(song.getDurationMillis()));
                seekBar.setMax((int) song.getDurationMillis());
                seekBar.setProgress(0);
                currentTimeTextView.setText(formatMillis(0));
            }
        });
    }

    private String formatMillis(long millis) {
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
    }
}