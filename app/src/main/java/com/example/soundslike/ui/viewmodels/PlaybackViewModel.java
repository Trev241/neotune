package com.example.soundslike.ui.viewmodels;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler; // Import Handler
import android.os.Looper; // Import Looper
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionCommand;
import androidx.media3.session.SessionToken;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.service.MusicService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;

@OptIn(markerClass = UnstableApi.class)
public class PlaybackViewModel extends AndroidViewModel {
    private static final String TAG = "PlaybackViewModel";
    private static final long PROGRESS_UPDATE_INTERVAL_MS = 500; // Update interval

    // LiveData for UI
    private final MutableLiveData<Song> _currentSong = new MutableLiveData<>(null);
    public LiveData<Song> getCurrentSong() { return _currentSong; }

    private final MutableLiveData<Boolean> _isPlaying = new MutableLiveData<>(false);
    public LiveData<Boolean> isPlaying() { return _isPlaying; }

    private final MutableLiveData<Long> _currentPosition = new MutableLiveData<>(0L);
    public LiveData<Long> getCurrentPosition() { return _currentPosition; }

    private final MutableLiveData<Long> _currentDuration = new MutableLiveData<>(0L);
    public LiveData<Long> getCurrentDuration() { return _currentDuration; }

    // MediaController and related
    private ListenableFuture<MediaController> controllerFuture;
    private MediaController mediaController;

    // Handler for progress updates
    private final Handler progressUpdateHandler = new Handler(Looper.getMainLooper());
    private Runnable progressUpdateRunnable;


    public PlaybackViewModel(@NonNull Application application) {
        super(application);
        initializeMediaController();
        setupProgressUpdater(); // Setup the runnable
    }

    private void initializeMediaController() {
        Context context = getApplication().getApplicationContext();
        SessionToken sessionToken = new SessionToken(context, new ComponentName(context, MusicService.class));
        controllerFuture = new MediaController.Builder(context, sessionToken).buildAsync();

        controllerFuture.addListener(() -> {
            try {
                mediaController = controllerFuture.get();
                if (mediaController != null) {
                    Log.d(TAG, "MediaController connected.");
                    mediaController.addListener(playerListener);
                    updateStateFromController();
                    // Start progress updates if already playing when connected
                    if (mediaController.isPlaying()) {
                        startProgressUpdater();
                    }
                } else {
                    Log.e(TAG, "MediaController connection failed (returned null).");
                }
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error getting MediaController: ", e);
            }
        }, MoreExecutors.directExecutor());
    }

    private final Player.Listener playerListener = new Player.Listener() {
        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Log.d(TAG, "Listener: onIsPlayingChanged: " + isPlaying);
            _isPlaying.postValue(isPlaying);
            // --- Start/Stop Progress Updater ---
            if (isPlaying) {
                startProgressUpdater();
            } else {
                stopProgressUpdater();
            }
            // ---------------------------------
        }

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
            Log.d(TAG, "Listener: onMediaItemTransition");
            updateCurrentSong(mediaItem);
            updateDuration(mediaController != null ? mediaController.getDuration() : 0L);
            _currentPosition.postValue(0L); // Reset position on transition
        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            Log.d(TAG, "Listener: onPlaybackStateChanged: " + playbackState);
            updateDuration(mediaController != null ? mediaController.getDuration() : 0L);
            if (playbackState == Player.STATE_READY) {
                // Start updater if playing state is true but wasn't caught by onIsPlayingChanged
                if (_isPlaying.getValue() == Boolean.TRUE) {
                    startProgressUpdater();
                }
            } else if (playbackState == Player.STATE_ENDED || playbackState == Player.STATE_IDLE) {
                stopProgressUpdater(); // Stop updates when ended or idle
                _currentPosition.postValue(0L); // Reset position
            }
        }

        @Override
        public void onPlayerError(@NonNull PlaybackException error) {
            Log.e(TAG, "Listener: onPlayerError: ", error);
            stopProgressUpdater(); // Stop updates on error
        }
    };

    private void updateStateFromController() {
        if (mediaController == null) return;
        _isPlaying.postValue(mediaController.isPlaying());
        updateCurrentSong(mediaController.getCurrentMediaItem());
        updateDuration(mediaController.getDuration());
        _currentPosition.postValue(mediaController.getCurrentPosition()); // Get initial position
    }


    private void updateCurrentSong(@Nullable MediaItem mediaItem) {
        if (mediaItem != null && mediaItem.mediaMetadata != null) {
            MediaMetadata metadata = mediaItem.mediaMetadata;

            // Extract available info
            String songId = mediaItem.mediaId != null ? mediaItem.mediaId : "unknown";
            String title = metadata.title != null ? metadata.title.toString() : "Unknown Title";
            // Artist Name is available, but not Artist ID directly from basic metadata
            String artistName = metadata.artist != null ? metadata.artist.toString() : "Unknown Artist";
            // Duration is available from the controller
            long durationMillis = (mediaController != null && mediaController.getDuration() != C.TIME_UNSET)
                    ? mediaController.getDuration() : 0L;
            float durationSeconds = durationMillis / 1000.0f;

            // --- Create Song with available data and placeholders ---
            // TODO: Fetch full Song details from repository using songId for complete data
            // TODO: Or pass more data (artistId, thumbnailUrl, etc.) via MediaMetadata extras from MusicService
            Song song = new Song(
                    songId,                                 // id
                    -1,                                     // songCode (placeholder)
                    title,                                  // title
                    "Unknown Release",                      // release (placeholder)
                    0,                                      // year (placeholder)
                    durationSeconds,                        // duration (float seconds)
                    null,                                   // thumbnailUrl (placeholder - fetch later)
                    "unknown_artist_id"                     // artistId (placeholder - fetch later or pass via extras)
            );
            // Set the artist name separately as it's available
            song.setArtistName(artistName);
            // ---------------------------------------------------------

            _currentSong.postValue(song);
            Log.d(TAG, "Listener: Updated current song: " + song.getTitle());
        } else {
            _currentSong.postValue(null);
            Log.d(TAG, "Listener: Current song is null");
        }
    }

    private void updateDuration(long duration) {
        // Ensure duration is valid before posting
        if (duration > 0 && duration != C.TIME_UNSET) {
            // Post duration in milliseconds
            _currentDuration.postValue(duration);
        } else {
            _currentDuration.postValue(0L);
        }
        // Update the duration in the current song object if it exists
        Song current = _currentSong.getValue();
        if (current != null && duration > 0 && duration != C.TIME_UNSET) {
            // Re-create or update the song object if duration changes significantly
            // For simplicity, we might just update the LiveData used by UI directly
        }
    }

    // --- Progress Updater Logic ---
    private void setupProgressUpdater() {
        progressUpdateRunnable = () -> {
            if (mediaController != null && mediaController.isConnected() && mediaController.isPlaying()) {
                long currentPosition = mediaController.getCurrentPosition();
                if (_currentPosition.getValue() == null || _currentPosition.getValue() != currentPosition) {
                    _currentPosition.postValue(currentPosition);
                }
                // Reschedule
                progressUpdateHandler.postDelayed(progressUpdateRunnable, PROGRESS_UPDATE_INTERVAL_MS);
            }
        };
    }

    private void startProgressUpdater() {
        stopProgressUpdater(); // Ensure no duplicates
        progressUpdateHandler.post(progressUpdateRunnable);
        Log.d(TAG, "Started progress updater");
    }

    private void stopProgressUpdater() {
        progressUpdateHandler.removeCallbacks(progressUpdateRunnable);
        Log.d(TAG, "Stopped progress updater");
    }
    // ---------------------------


    // --- Public methods for UI to call ---
    public void playSongById(String songId, String title, String artist) {
        if (mediaController == null) {
            Log.w(TAG, "playSongById: MediaController not ready yet.");
            return;
        }
        Log.d(TAG, "Sending PLAY_SONG_ID command for ID: " + songId);
        Bundle args = new Bundle();
        args.putString(MusicService.EXTRA_SONG_ID, songId);
        args.putString(MusicService.EXTRA_SONG_TITLE, title);
        args.putString(MusicService.EXTRA_SONG_ARTIST, artist);
        mediaController.sendCustomCommand(MusicService.COMMAND_PLAY_SONG_ID, args);
    }

    public void togglePlayPause() {
        if (mediaController == null) return;
        if (mediaController.getPlaybackState() == Player.STATE_IDLE) {
            mediaController.prepare();
            mediaController.play();
        } else if (mediaController.isPlaying()) {
            mediaController.pause();
        } else {
            mediaController.play();
        }
    }

    public void seekTo(long positionMs) {
        if (mediaController == null) return;
        mediaController.seekTo(positionMs);
    }

    public void skipToNext() {
        if (mediaController == null) return;
        mediaController.seekToNextMediaItem();
    }

    public void skipToPrevious() {
        if (mediaController == null) return;
        mediaController.seekToPreviousMediaItem();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
        stopProgressUpdater(); // Stop handler
        if (mediaController != null) {
            mediaController.removeListener(playerListener);
        }
        MediaController.releaseFuture(controllerFuture);
    }
}