package com.example.soundslike.ui.viewmodels;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast; // Import Toast

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
import com.example.soundslike.data.network.ApiService;
import com.example.soundslike.service.MusicService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.Collections; // Import Collections
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@OptIn(markerClass = UnstableApi.class)
public class PlaybackViewModel extends AndroidViewModel {
    private static final String TAG = "PlaybackViewModel";
    private static final long PROGRESS_UPDATE_INTERVAL_MS = 500;

    // --- State for simplified Next/Previous ---
    private List<Song> currentPlaylist = null;
    private int currentPlaylistIndex = -1;
    // -----------------------------------------

    // LiveData for UI
    private final MutableLiveData<Song> _currentSong = new MutableLiveData<>(null);
    public LiveData<Song> getCurrentSong() { return _currentSong; }

    private final MutableLiveData<Boolean> _isPlaying = new MutableLiveData<>(false);
    public LiveData<Boolean> isPlaying() { return _isPlaying; }

    private final MutableLiveData<Long> _currentPosition = new MutableLiveData<>(0L);
    public LiveData<Long> getCurrentPosition() { return _currentPosition; }

    private final MutableLiveData<Long> _currentDuration = new MutableLiveData<>(0L);
    public LiveData<Long> getCurrentDuration() { return _currentDuration; }

    private ListenableFuture<MediaController> controllerFuture;
    private MediaController mediaController;
    private final Handler progressUpdateHandler = new Handler(Looper.getMainLooper());
    private Runnable progressUpdateRunnable;

    public PlaybackViewModel(@NonNull Application application) {
        super(application);
        initializeMediaController();
        setupProgressUpdater();
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
                    updateStateFromController(); // Initial state sync
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
            if (isPlaying) {
                startProgressUpdater();
            } else {
                stopProgressUpdater();
            }
        }

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
            Log.d(TAG, "Listener: onMediaItemTransition, reason: " + reason + ". Current VM index: " + currentPlaylistIndex);
            updateCurrentSong(mediaItem);
            updateDuration(mediaController != null ? mediaController.getDuration() : 0L);
        }


        @Override
        public void onPlaybackStateChanged(int playbackState) {
            Log.d(TAG, "Listener: onPlaybackStateChanged: " + playbackState);
            updateDuration(mediaController != null ? mediaController.getDuration() : 0L);
            if (playbackState == Player.STATE_READY) {
                updateDuration(mediaController != null ? mediaController.getDuration() : 0L);
                if (_isPlaying.getValue() != null && _isPlaying.getValue()) {
                    startProgressUpdater();
                }
            } else if (playbackState == Player.STATE_ENDED) {
                Log.d(TAG, "Playback ended. Attempting to play next song automatically.");
                stopProgressUpdater();
                skipToNext(); // Call our skipToNext logic
            } else if (playbackState == Player.STATE_IDLE) {
                stopProgressUpdater();
                _currentPosition.postValue(0L);
            }
        }

        @Override
        public void onPlayerError(@NonNull PlaybackException error) {
            Log.e(TAG, "Listener: onPlayerError: ", error);
            stopProgressUpdater();
            Toast.makeText(getApplication(), "Playback Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // Sync ViewModel state with MediaController state
    private void updateStateFromController() {
        if (mediaController == null || !mediaController.isConnected()) return;
        _isPlaying.postValue(mediaController.isPlaying());
        updateCurrentSong(mediaController.getCurrentMediaItem());
        updateDuration(mediaController.getDuration());
        _currentPosition.postValue(mediaController.getCurrentPosition());
    }

    // Update the _currentSong LiveData based on MediaItem
    private void updateCurrentSong(@Nullable MediaItem mediaItem) {
        Song songFromMediaItem = null;
        if (mediaItem != null && mediaItem.mediaId != null) {
            if (currentPlaylist != null) {
                for (int i = 0; i < currentPlaylist.size(); i++) {
                    Song s = currentPlaylist.get(i);
                    if (s != null && mediaItem.mediaId.equals(s.getId())) {
                        songFromMediaItem = s;
                        if (currentPlaylistIndex != i) {
                            Log.w(TAG, "Controller changed media item (" + mediaItem.mediaId + ") - Syncing VM index from " + currentPlaylistIndex + " to " + i);
                            currentPlaylistIndex = i;
                        }
                        break;
                    }
                }
            }

            if (songFromMediaItem == null && mediaItem.mediaMetadata != null) {
                Log.w(TAG, "MediaItem ("+mediaItem.mediaId+") not found in current VM playlist. Reconstructing from metadata.");
                MediaMetadata metadata = mediaItem.mediaMetadata;
                String title = metadata.title != null ? metadata.title.toString() : "Unknown Title";
                String artistName = metadata.artist != null ? metadata.artist.toString() : "Unknown Artist";
                String thumbnailUrl = metadata.artworkUri != null ? metadata.artworkUri.toString() : null;
                long durationMillis = (mediaController != null && mediaController.getDuration() != C.TIME_UNSET)
                        ? mediaController.getDuration() : 0L;
                float durationSeconds = durationMillis / 1000.0f;

                songFromMediaItem = new Song(mediaItem.mediaId, -1, title, "", 0, durationSeconds, thumbnailUrl, "");
                songFromMediaItem.setArtistName(artistName);
            }
        }

        Song previousSong = _currentSong.getValue();
        if (!Objects.equals(previousSong, songFromMediaItem)) {
            _currentSong.postValue(songFromMediaItem);
            Log.d(TAG, "updateCurrentSong: Posting update - " + (songFromMediaItem != null ? songFromMediaItem.getTitle() : "null"));
        } else {
            Log.v(TAG, "updateCurrentSong: Song object is the same, not posting update.");
        }
    }


    private void updateDuration(long duration) {
        long currentDuration = duration > 0 && duration != C.TIME_UNSET ? duration : 0L;
        if (_currentDuration.getValue() == null || !Objects.equals(_currentDuration.getValue(), currentDuration)) {
            _currentDuration.postValue(currentDuration);
            Log.d(TAG, "Listener: Updated duration: " + currentDuration);
        }
    }

    // --- Progress Updater (Unchanged) ---
    private void setupProgressUpdater() {
        progressUpdateRunnable = () -> {
            if (mediaController != null && mediaController.isConnected() && mediaController.isPlaying()) {
                long currentPosition = mediaController.getCurrentPosition();
                long duration = mediaController.getDuration();
                if (duration != C.TIME_UNSET && currentPosition > duration) {
                    currentPosition = duration;
                }
                if (_currentPosition.getValue() == null || !Objects.equals(_currentPosition.getValue(), currentPosition)) {
                    _currentPosition.postValue(currentPosition);
                }
                progressUpdateHandler.postDelayed(progressUpdateRunnable, PROGRESS_UPDATE_INTERVAL_MS);
            }
        };
    }

    private void startProgressUpdater() {
        stopProgressUpdater();
        progressUpdateHandler.post(progressUpdateRunnable);
        Log.d(TAG, "Started progress updater");
    }

    private void stopProgressUpdater() {
        progressUpdateHandler.removeCallbacks(progressUpdateRunnable);
        Log.d(TAG, "Stopped progress updater");
    }
    // ------------------------------------

    // --- Public methods for UI ---

    /**
     * Stores the playlist context and starts playback of the song at the specified index.
     * Uses setMediaItem to play only the single starting song.
     */
    public void playSongList(List<Song> songs, int startIndex) {
        if (songs == null || songs.isEmpty()) {
            Log.w(TAG, "playSongList: Provided song list is null or empty.");
            this.currentPlaylist = null;
            this.currentPlaylistIndex = -1;
            if (mediaController != null) mediaController.stop();
            _currentSong.postValue(null);
            return;
        }
        if (startIndex < 0 || startIndex >= songs.size()) {
            Log.w(TAG, "playSongList: Invalid start index " + startIndex + " for list size " + songs.size());
            startIndex = 0;
        }

        Log.d(TAG, "playSongList: Storing playlist of " + songs.size() + " songs. Starting at index " + startIndex);
        this.currentPlaylist = new ArrayList<>(songs);
        this.currentPlaylistIndex = startIndex;

        Song songToPlay = this.currentPlaylist.get(this.currentPlaylistIndex);

        if (songToPlay == null || songToPlay.getId() == null) {
            Log.e(TAG, "Song to play at index " + this.currentPlaylistIndex + " is null or has null ID.");
            Toast.makeText(getApplication(), "Error: Cannot play selected song.", Toast.LENGTH_SHORT).show();
            return;
        }
        playSingleMediaItem(songToPlay);
    }

    // Helper method to build and play a single MediaItem
    private void playSingleMediaItem(Song song) {
        // 1. Check if Controller is ready
        if (mediaController == null || !mediaController.isConnected()) {
            Log.w(TAG, "playSingleMediaItem: MediaController not ready yet.");
            Toast.makeText(getApplication(), "Player not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Check if Song object and its ID are valid
        if (song == null || song.getId() == null) {
            Log.e(TAG, "playSingleMediaItem: Cannot play null song or song with null ID.");
            return;
        }

        Log.d(TAG, "playSingleMediaItem: Preparing to play song: " + song.getTitle() + " (ID: " + song.getId() + ")");

        // 3. Build the Stream URL
        String streamUrl = ApiService.BASE_URL + "songs/stream?song_id=" + song.getId();
        Uri mediaUri;
        try {
            mediaUri = Uri.parse(streamUrl);
        } catch (NullPointerException e) {
            Log.e(TAG, "playSingleMediaItem: Failed to parse stream URL: " + streamUrl, e);
            Toast.makeText(getApplication(), "Error: Invalid stream URL", Toast.LENGTH_SHORT).show();
            return;
        }


        // 4. Build MediaMetadata
        MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();

        // Set Title (with default)
        metadataBuilder.setTitle(song.getTitle() != null ? song.getTitle() : "Unknown Title");

        // Set Artist (with default)
        metadataBuilder.setArtist(song.getArtistName() != null ? song.getArtistName() : "Unknown Artist");

        // --- REMOVED INCORRECT setMediaId FROM METADATA ---
        // metadataBuilder.setMediaId(song.getId()); // INCORRECT!
        // -------------------------------------------------

        // Set Artwork URI (optional, with error handling)
        String thumbnailUrl = song.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            try {
                metadataBuilder.setArtworkUri(Uri.parse(thumbnailUrl));
            } catch (Exception e) {
                Log.w(TAG, "playSingleMediaItem: Failed to parse artwork URI: " + thumbnailUrl, e);
            }
        } else {
            Log.v(TAG, "playSingleMediaItem: No thumbnail URL provided for song: " + song.getTitle());
        }

        // 5. Build MediaItem
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(mediaUri)
                // --- SET MEDIA ID ON MEDIA ITEM BUILDER (Correct place) ---
                .setMediaId(song.getId())
                // ---------------------------------------------------------
                .setMediaMetadata(metadataBuilder.build())
                .build();

        // 6. Set item on controller, prepare, and play
        mediaController.setMediaItem(mediaItem);
        mediaController.prepare();
        mediaController.play();

        Log.d(TAG, "playSingleMediaItem: Called setMediaItem, prepare, and play for " + song.getId());
    }


    /**
     * @deprecated Use playSongList for proper context management.
     */
    @Deprecated
    public void playSongById(String songId, String title, String artist) {
        Log.w(TAG, "playSongById is deprecated. Use playSongList instead.");
        Song song = new Song(songId, -1, title, "", 0, 0, null, "");
        song.setArtistName(artist);
        this.currentPlaylist = null;
        this.currentPlaylistIndex = -1;
        playSingleMediaItem(song);
    }

    public void togglePlayPause() {
        if (mediaController == null || !mediaController.isConnected()) return;
        if (mediaController.getPlaybackState() == Player.STATE_IDLE || mediaController.getPlaybackState() == Player.STATE_ENDED) {
            Log.d(TAG, "togglePlayPause: State is IDLE or ENDED. Calling prepare() and play().");
            mediaController.prepare();
            mediaController.play();
        } else if (mediaController.isPlaying()) {
            Log.d(TAG, "togglePlayPause: State is PLAYING. Calling pause().");
            mediaController.pause();
        } else {
            Log.d(TAG, "togglePlayPause: State is PAUSED or READY. Calling play().");
            mediaController.play();
        }
    }

    public void seekTo(long positionMs) {
        if (mediaController == null || !mediaController.isConnected()) return;
        Log.d(TAG, "Seeking to: " + positionMs);
        mediaController.seekTo(positionMs);
    }

    // --- Simplified Skip Methods ---
    public void skipToNext() {
        if (currentPlaylist == null || currentPlaylist.isEmpty()) {
            Log.w(TAG, "Cannot skip to next: No current playlist.");
            Toast.makeText(getApplication(), "No playlist active", Toast.LENGTH_SHORT).show();
            return;
        }

        int nextIndex = currentPlaylistIndex + 1;
        if (nextIndex >= currentPlaylist.size()) {
            Log.d(TAG, "Reached end of playlist.");
            nextIndex = 0; // Loop back to start
        }

        currentPlaylistIndex = nextIndex;
        Song nextSong = currentPlaylist.get(currentPlaylistIndex);
        Log.d(TAG, "skipToNext: Playing index " + currentPlaylistIndex + ": " + (nextSong != null ? nextSong.getTitle() : "null song"));
        if (nextSong != null) {
            playSingleMediaItem(nextSong);
        } else {
            Log.e(TAG, "skipToNext: Song at index " + currentPlaylistIndex + " is null!");
            Toast.makeText(getApplication(), "Error: Cannot play next song.", Toast.LENGTH_SHORT).show();
        }
    }

    public void skipToPrevious() {
        if (currentPlaylist == null || currentPlaylist.isEmpty()) {
            Log.w(TAG, "Cannot skip to previous: No current playlist.");
            Toast.makeText(getApplication(), "No playlist active", Toast.LENGTH_SHORT).show();
            return;
        }

        long seekToPreviousThresholdMs = 3000;
        if (mediaController != null && mediaController.getCurrentPosition() > seekToPreviousThresholdMs && mediaController.isCurrentMediaItemSeekable()) {
            Log.d(TAG, "skipToPrevious: Current position > threshold. Seeking to 0 of current item.");
            mediaController.seekTo(0);
            return;
        }

        int prevIndex = currentPlaylistIndex - 1;
        if (prevIndex < 0) {
            Log.d(TAG, "Reached beginning of playlist.");
            prevIndex = currentPlaylist.size() - 1; // Loop back to end
        }

        currentPlaylistIndex = prevIndex;
        Song prevSong = currentPlaylist.get(currentPlaylistIndex);
        Log.d(TAG, "skipToPrevious: Playing index " + currentPlaylistIndex + ": " + (prevSong != null ? prevSong.getTitle() : "null song"));
        if (prevSong != null) {
            playSingleMediaItem(prevSong);
        } else {
            Log.e(TAG, "skipToPrevious: Song at index " + currentPlaylistIndex + " is null!");
            Toast.makeText(getApplication(), "Error: Cannot play previous song.", Toast.LENGTH_SHORT).show();
        }
    }
    // --------------------


    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
        stopProgressUpdater();
        if (controllerFuture != null) {
            MediaController.releaseFuture(controllerFuture);
            Log.d(TAG,"Released MediaController future.");
        }
        mediaController = null;
        currentPlaylist = null;
        currentPlaylistIndex = -1;
    }
}