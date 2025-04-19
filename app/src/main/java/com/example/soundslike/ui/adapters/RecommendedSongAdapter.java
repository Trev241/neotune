package com.example.soundslike.ui.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // Import Toast

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.List; // Import List


public class RecommendedSongAdapter extends ListAdapter<Song, RecommendedSongAdapter.SongViewHolder> {

    // Keep a reference to the full list for context when playing
    // Note: This assumes the list passed to submitList is the playback context.
    // Be mindful if the adapter's list source changes frequently or represents
    // only a partial view of a larger playlist.
    private List<Song> currentSongList = null;

    public RecommendedSongAdapter() {
        super(SongDiffCallback.INSTANCE);
    }

    // Override submitList to keep track of the current full list
    @Override
    public void submitList(@Nullable List<Song> list) {
        super.submitList(list);
        // Keep a reference to the submitted list
        this.currentSongList = list;
        Log.d("RecommendedSongAdapter", "Submitted list with " + (list == null ? 0 : list.size()) + " items.");
    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card_long, parent, false);
        // Pass the adapter instance to the ViewHolder
        return new SongViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // --- ViewHolder ---
    // Make ViewHolder non-static OR pass adapter reference if needed
    public static class SongViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView artistView;
        private final RecommendedSongAdapter adapter; // Reference to the adapter

        // Modify constructor to accept the adapter
        public SongViewHolder(@NonNull View itemView, RecommendedSongAdapter adapter) {
            super(itemView);
            this.adapter = adapter; // Store the adapter reference
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
                int position = getBindingAdapterPosition(); // Use getBindingAdapterPosition()
                if (position != RecyclerView.NO_POSITION) {
                    // Get the currently displayed list from the adapter reference
                    List<Song> playlist = adapter.currentSongList;

                    if (playlist == null || playlist.isEmpty()) {
                        Log.e("SongAdapter", "Cannot play song, adapter list is null or empty.");
                        Toast.makeText(itemView.getContext(), "Error: Playlist data missing", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Ensure position is valid for the current list
                    if (position >= playlist.size()) {
                        Log.e("SongAdapter", "Invalid position " + position + " for list size " + playlist.size());
                        Toast.makeText(itemView.getContext(), "Error: Invalid song position", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Song clickedSong = playlist.get(position); // Get the clicked song using the valid position
                    Log.d("SongAdapter", "Clicked song: " + clickedSong.getTitle() + " at position " + position);


                    try {
                        PlaybackViewModel playbackViewModel = new ViewModelProvider((FragmentActivity) itemView.getContext())
                                .get(PlaybackViewModel.class);

                        // --- Call the new method to play the list ---
                        Log.d("SongAdapter", "Calling playSongList with " + playlist.size() + " songs, starting at index " + position);
                        playbackViewModel.playSongList(playlist, position);
                        // --------------------------------------------

                        // Navigate to player view (remains the same)
                        NavController navController = Navigation.findNavController(itemView);
                        Bundle args = new Bundle();
                        args.putString("songId", clickedSong.getId()); // Pass ID for potential initial loading in SongViewFragment if needed
                        int currentDestinationId = navController.getCurrentDestination() != null ?
                                navController.getCurrentDestination().getId() : 0;

                        // Prevent navigating to song view if already there
                        if (currentDestinationId != R.id.navigation_song_view) {
                            // Navigate from appropriate source fragment
                            if (currentDestinationId == R.id.navigation_home) {
                                navController.navigate(R.id.action_home_to_song_view, args);
                            } else if (currentDestinationId == R.id.navigation_explore) {
                                navController.navigate(R.id.action_explore_to_song_view, args);
                            } else if (currentDestinationId == R.id.navigation_playlist_detail) {
                                navController.navigate(R.id.action_playlist_detail_to_song_view, args);
                            }
                            // Add other source fragments if necessary
                            else {
                                Log.w("SongAdapter", "Navigation to SongView not defined for current destination: " + currentDestinationId);
                                // Fallback or error handling if needed
                            }
                        } else {
                            Log.d("SongAdapter", "Already on SongView screen, not navigating again.");
                        }
                    } catch (Exception e) {
                        Log.e("SongAdapter", "Click failed for song " + clickedSong.getId(), e);
                        Toast.makeText(itemView.getContext(), "Error starting playback", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.w("SongAdapter", "Clicked item has no position (NO_POSITION).");
                }
            });
        }
    }

    // --- DiffCallback ---
    // Make sure Song has a proper equals() implementation
    public static class SongDiffCallback extends DiffUtil.ItemCallback<Song> {
        public static final SongDiffCallback INSTANCE = new SongDiffCallback();

        @Override
        public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            // Use a unique and stable identifier
            return oldItem.getId() != null && oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            // Compare all relevant fields
            return oldItem.equals(newItem);
        }
    }
}