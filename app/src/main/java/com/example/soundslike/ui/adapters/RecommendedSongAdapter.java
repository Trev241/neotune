package com.example.soundslike.ui.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Import Glide
import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.ui.viewmodels.PlaybackViewModel;


public class RecommendedSongAdapter extends ListAdapter<Song, RecommendedSongAdapter.SongViewHolder> {

    public RecommendedSongAdapter() {
        super(SongDiffCallback.INSTANCE);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card_long, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // --- ViewHolder ---
    public static class SongViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView artistView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            // Make sure these IDs match your song_card_long.xml
            imageView = itemView.findViewById(R.id.imageView2);
            titleView = itemView.findViewById(R.id.songTitle);
            artistView = itemView.findViewById(R.id.textView7);
        }

        public void bind(Song song) {
            titleView.setText(song.getTitle());
            // Use getArtistName() - needs to be populated correctly elsewhere if needed
            artistView.setText(song.getArtistName());

            // --- Use Glide to load image from URL ---
            Glide.with(itemView.getContext())
                    .load(song.getThumbnailUrl()) // Load URL from Song object
                    .placeholder(R.drawable.ic_genre_placeholder) // Placeholder image
                    .error(R.drawable.ic_genre_placeholder) // Error image
                    .into(imageView); // Target ImageView
            // -----------------------------------------

            itemView.setOnClickListener(v -> {
                try {
                    PlaybackViewModel playbackViewModel = new ViewModelProvider((FragmentActivity) itemView.getContext())
                            .get(PlaybackViewModel.class);
                    // Start playback
                    playbackViewModel.playSongById(song.getId(), song.getTitle(), song.getArtistName());

                    // Navigate to player view
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle args = new Bundle();
                    args.putString("songId", song.getId());
                    int currentDestinationId = navController.getCurrentDestination() != null ?
                            navController.getCurrentDestination().getId() : 0;

                    if (currentDestinationId != R.id.navigation_song_view) {
                        // Navigate from appropriate source fragment
                        if (currentDestinationId == R.id.navigation_home) { // Add Explore case if needed
                            navController.navigate(R.id.action_home_to_song_view, args);
                        } else if (currentDestinationId == R.id.navigation_explore) {
                            // *** Add this action to mobile_navigation.xml if missing ***
                            // Example: <action android:id="@+id/action_explore_to_song_view" app:destination="@id/navigation_song_view" />
                            navController.navigate(R.id.action_explore_to_song_view, args);
                        } else if (currentDestinationId == R.id.navigation_playlist_detail) {
                            navController.navigate(R.id.action_playlist_detail_to_song_view, args);
                        }
                    }
                } catch (Exception e) {
                    Log.e("SongAdapter", "Click failed for song " + song.getId(), e);
                }
            });
        }
    }

    // --- DiffCallback ---
    public static class SongDiffCallback extends DiffUtil.ItemCallback<Song> {
        public static final SongDiffCallback INSTANCE = new SongDiffCallback();

        @Override
        public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.equals(newItem);
        }
    }
}