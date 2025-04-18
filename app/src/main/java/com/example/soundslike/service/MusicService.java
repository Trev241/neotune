package com.example.soundslike.service;

// --- Other imports ---
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.session.SessionCommand;
import androidx.media3.session.SessionResult;
import com.example.soundslike.MainActivity;
import com.example.soundslike.R;
import com.example.soundslike.data.network.ApiService; // Import ApiService
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

// --- Correct Import for UnstableApi ---
import androidx.media3.common.util.UnstableApi;
// --------------------------------------

@UnstableApi // Use the annotation directly after importing
public class MusicService extends MediaSessionService {

    private static final String TAG = "MusicService";
    private MediaSession mediaSession = null;
    private ExoPlayer player = null;

    // Custom Command Definition
    public static final String ACTION_PLAY_SONG_ID = "com.example.soundslike.PLAY_SONG_ID";
    public static final SessionCommand COMMAND_PLAY_SONG_ID =
            new SessionCommand(ACTION_PLAY_SONG_ID, Bundle.EMPTY);

    // Argument Keys
    public static final String EXTRA_SONG_ID = "com.example.soundslike.EXTRA_SONG_ID";
    public static final String EXTRA_SONG_TITLE = "com.example.soundslike.EXTRA_SONG_TITLE";
    public static final String EXTRA_SONG_ARTIST = "com.example.soundslike.EXTRA_SONG_ARTIST";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        initializeSessionAndPlayer();
    }

    private void initializeSessionAndPlayer() {
        if (player == null) {
            player = new ExoPlayer.Builder(this)
                    .setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true)
                    .setHandleAudioBecomingNoisy(true)
                    .build();
            player.addListener(new Player.Listener() {
                @Override
                public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                    Log.d(TAG, "onMediaItemTransition");
                    // updateMetadata(mediaItem); // You might need this later
                }
                // Add other listener overrides if needed
            });
        }

        if (mediaSession == null) {
            Intent activityIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    activityIntent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );

            mediaSession = new MediaSession.Builder(this, player)
                    .setSessionActivity(pendingIntent)
                    .setCallback(new MediaSessionCallback())
                    .build();
        }
    }

    @Nullable
    @Override
    public MediaSession onGetSession(@NonNull MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (mediaSession != null) {
            mediaSession.release();
            mediaSession = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    // --- MediaSession Callback ---
    private class MediaSessionCallback implements MediaSession.Callback {

        @NonNull
        @Override
        public MediaSession.ConnectionResult onConnect(@NonNull MediaSession session, @NonNull MediaSession.ControllerInfo controller) {
            Log.d(TAG, "MediaSession onConnect from: " + controller.getPackageName());
            MediaSession.ConnectionResult connectionResult = MediaSession.Callback.super.onConnect(session, controller);
            if (connectionResult.isAccepted) {
                MediaSession.ConnectionResult.AcceptedResultBuilder builder =
                        new MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                                .setAvailableSessionCommands(
                                        connectionResult.availableSessionCommands.buildUpon()
                                                .add(COMMAND_PLAY_SONG_ID) // Add custom command here
                                                .build()
                                );
                return builder.build();
            } else {
                return connectionResult;
            }
        }


        @NonNull
        @Override
        public ListenableFuture<SessionResult> onCustomCommand(@NonNull MediaSession session, @NonNull MediaSession.ControllerInfo controller, @NonNull SessionCommand customCommand, @NonNull Bundle args) {
            Log.d(TAG, "onCustomCommand received: " + customCommand.customAction);
            if (COMMAND_PLAY_SONG_ID.customAction.equals(customCommand.customAction)) { // Compare actions
                String songId = args.getString(EXTRA_SONG_ID);
                String title = args.getString(EXTRA_SONG_TITLE);
                String artist = args.getString(EXTRA_SONG_ARTIST);
                if (songId != null) {
                    handlePlaySongRequest(songId, title, artist);
                    return Futures.immediateFuture(new SessionResult(SessionResult.RESULT_SUCCESS));
                } else {
                    Log.w(TAG, "Custom command PLAY_SONG_ID missing EXTRA_SONG_ID");
                    return Futures.immediateFuture(new SessionResult(SessionResult.RESULT_ERROR_BAD_VALUE));
                }
            } else {
                // Let the base class handle unknown commands
                return MediaSession.Callback.super.onCustomCommand(session, controller, customCommand, args);
                // Or return not supported if you don't want base handling:
                // return Futures.immediateFuture(new SessionResult(SessionResult.RESULT_ERROR_NOT_SUPPORTED));
            }
        }
    }

    // --- Playback Logic ---
    private void handlePlaySongRequest(String songId, String title, String artist) {
        if (player == null) {
            initializeSessionAndPlayer();
            if (player == null) {
                Log.e(TAG, "Player still null after initialization attempt!");
                return;
            }
        }
        Log.d(TAG, "handlePlaySongRequest: ID=" + songId + ", Title=" + title + ", Artist=" + artist);

        // --- Construct the stream URL dynamically ---
        String streamUrl = ApiService.BASE_URL + "songs/stream?song_id=" + songId;
        Log.i(TAG, "Attempting to play stream URL: " + streamUrl);
        // -------------------------------------------

        Uri mediaUri = Uri.parse(streamUrl);

        MediaMetadata metadata = new MediaMetadata.Builder()
                .setTitle(title != null ? title : "Unknown Title")
                .setArtist(artist != null ? artist : "Unknown Artist")
                // TODO: Fetch actual thumbnail URL via ViewModel/Repo and set here
                // .setArtworkUri(Uri.parse(thumbnailUrlFromSongObject))
                .build();

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(mediaUri)
                .setMediaId(songId) // Use the actual song ID
                .setMediaMetadata(metadata)
                .build();

        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    // --- updateMetadata (Optional, if needed) ---
    // private void updateMetadata(@Nullable MediaItem mediaItem) { ... }
}