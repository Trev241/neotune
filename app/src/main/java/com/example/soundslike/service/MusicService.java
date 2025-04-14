package com.example.soundslike.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat; // Use androidx
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.soundslike.MainActivity; // To launch UI from notification
import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;

public class MusicService extends Service {

    private static final String TAG = "MusicService";
    private static final String CHANNEL_ID = "MusicServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    private ExoPlayer player;
    private final IBinder binder = new MusicBinder();
    private Song currentSong; // Keep track of the current song

    // Binder class for clients to access the service instance
    public class MusicBinder extends Binder {
        public MusicService getService() {
            // Return this instance of MusicService so clients can call public methods
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        initializePlayer();
        createNotificationChannel();
    }

    private void initializePlayer() {
        if (player == null) {
            player = new ExoPlayer.Builder(this).build();
            // Add listener for state changes (e.g., to update notification or stop service)
            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    Log.d(TAG, "Playback state changed: " + playbackState);
                    if (playbackState == Player.STATE_ENDED) {
                        // Handle song end (e.g., play next, stop service)
                        Log.d(TAG, "Playback ended");
                        stopForeground(true); // Remove notification
                        stopSelf(); // Stop the service if queue is empty
                    }
                    // Update notification based on play/pause state
                    updateNotification();
                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    Log.d(TAG, "IsPlaying changed: " + isPlaying);
                    updateNotification(); // Update play/pause button in notification
                    if (!isPlaying && player.getPlaybackState() != Player.STATE_ENDED) {
                        // If paused manually, keep the service running but allow notification removal
                        stopForeground(false);
                    } else if (isPlaying) {
                        // If playing, ensure service is foreground
                        startForeground(NOTIFICATION_ID, buildNotification());
                    }
                }

                @Override
                public void onPlayerError(androidx.media3.common.PlaybackException error) {
                    Log.e(TAG, "Player Error: ", error);
                    // Handle error appropriately (e.g., skip track, stop service, notify UI)
                    stopForeground(true);
                    stopSelf();
                }
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return binder; // Return the binder instance
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        // We handle starting playback via binder calls, but this keeps service alive
        // If killed, it won't restart automatically with START_STICKY
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            currentSong = null;
        }
    }

    // --- Public methods for clients (via Binder) ---

    public void playSong(Song song) {
        if (song == null) {
            Log.w(TAG, "playSong called with null song");
            return;
        }
        Log.d(TAG, "playSong: " + song.getTitle());
        currentSong = song;
        initializePlayer(); // Ensure player is ready

        // For mock data, we need a way to get a playable URI.
        // Option 1: Use raw resource (if you add mock audio files to res/raw)
        // Example: If you have R.raw.get_lucky
        // Uri mediaUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.get_lucky); // Replace with actual logic

        // Option 2: Use a placeholder URI (won't play, but sets up player)
        // This is just for demonstrating the structure. Replace with real URI logic.
        Uri mediaUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.placeholder_audio); // ASSUMES you create an empty/silent placeholder file

        if (mediaUri == null) {
            Log.e(TAG, "Could not get URI for song: " + song.getTitle());
            return;
        }

        MediaItem mediaItem = MediaItem.fromUri(mediaUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play(); // Start playback

        // Start foreground service with notification
        startForeground(NOTIFICATION_ID, buildNotification());
    }

    public void pausePlayback() {
        if (player != null && player.isPlaying()) {
            Log.d(TAG, "pausePlayback");
            player.pause();
            // Notification will update via listener
            // Keep service running but allow notification removal
            stopForeground(false);
        }
    }

    public void resumePlayback() {
        if (player != null && !player.isPlaying() && player.getPlaybackState() != Player.STATE_IDLE) {
            Log.d(TAG, "resumePlayback");
            player.play();
            // Ensure service is foreground again
            startForeground(NOTIFICATION_ID, buildNotification());
        }
    }

    public void seekPlayback(long positionMs) {
        if (player != null) {
            Log.d(TAG, "seekPlayback to: " + positionMs);
            player.seekTo(positionMs);
        }
    }

    // --- TODO: Implement these later with queue logic ---
    public void skipToNext() {
        Log.d(TAG, "skipToNext (Not Implemented)");
        // player.seekToNextMediaItem();
    }

    public void skipToPrevious() {
        Log.d(TAG, "skipToPrevious (Not Implemented)");
        // player.seekToPreviousMediaItem();
    }
    // ----------------------------------------------------

    // --- Notification Handling ---

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Service Channel",
                    NotificationManager.IMPORTANCE_LOW // Low importance for background playback
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            } else {
                Log.e(TAG, "NotificationManager not available");
            }
        }
    }

    // Build the notification (basic version for now)
    private Notification buildNotification() {
        // Intent to launch the UI when notification is tapped
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE); // Use FLAG_IMMUTABLE

        String title = (currentSong != null) ? currentSong.getTitle() : "No Title";
        String artist = (currentSong != null) ? currentSong.getArtist() : "No Artist";

        // TODO: Add Play/Pause, Next, Prev actions later
        // TODO: Add Album Art later

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(artist)
                .setSmallIcon(R.drawable.ic_play) // Use a generic icon for now
                .setContentIntent(pendingIntent)
                .setOngoing(player != null && player.isPlaying()) // Make ongoing if playing
                .setSilent(true); // Don't make sound on update

        return builder.build();
    }

    // Update notification content (e.g., when song changes or play/pause state changes)
    private void updateNotification() {
        if (currentSong != null) { // Only update if there's a song loaded
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.notify(NOTIFICATION_ID, buildNotification());
            }
        }
    }

    // --- TODO: Add methods to get playback state for UI ---
    // public LiveData<PlaybackState> getPlaybackState() { ... }
    // public LiveData<Song> getCurrentPlayingSong() { ... }
    // public LiveData<Long> getCurrentPosition() { ... }

}