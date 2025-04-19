package com.example.soundslike.ui.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.ui.viewmodels.PlaybackViewModel;

import java.util.List;
import java.util.Random; // Import Random

public class RecommendedSongAdapter extends ListAdapter<Song, RecommendedSongAdapter.SongViewHolder> {

    private List<Song> currentSongList = null;
    // Add a Random instance for generating image URLs
    private final Random random = new Random();

    public RecommendedSongAdapter() {
        super(SongDiffCallback.INSTANCE);
    }

    @Override
    public void submitList(@Nullable List<Song> list) {
        super.submitList(list);
        this.currentSongList = list;
        Log.d("RecommendedSongAdapter", "Submitted list with " + (list == null ? 0 : list.size()) + " items.");
    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card_long, parent, false);
        // Pass the adapter instance AND the random generator to the ViewHolder
        return new SongViewHolder(view, this, random);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView artistView;
        private final RecommendedSongAdapter adapter;
        private final Random random; // Store Random instance

        // Modify constructor to accept Random
        public SongViewHolder(@NonNull View itemView, RecommendedSongAdapter adapter, Random random) {
            super(itemView);
            this.adapter = adapter;
            this.random = random; // Store it
            imageView = itemView.findViewById(R.id.imageView2);
            titleView = itemView.findViewById(R.id.songTitle);
            artistView = itemView.findViewById(R.id.textView7);
        }

        public void bind(Song song) {
            titleView.setText(song.getTitle());
            artistView.setText(song.getArtistName());

            // --- Generate Random Image URL ---
            String randomImageUrl;
            String baseUrl = "https://picsum.photos/seed/";
            int imageSize = 150; // Size for the song card long image view
            // Use song ID for a consistent random image per song
            String seed = song.getId() != null ? song.getId() : "song" + random.nextInt();
            randomImageUrl = baseUrl + seed + "/" + imageSize;
            Log.v("SongAdapter", "Using random image URL for '" + song.getTitle() + "': " + randomImageUrl);
            // ---------------------------------

            // --- Use Glide to load the RANDOM image URL ---
            Glide.with(itemView.getContext())
                    .load(randomImageUrl) // Load the generated random URL
                    .placeholder(R.drawable.ic_genre_placeholder) // Placeholder image
                    .error(R.drawable.ic_genre_placeholder) // Error image
                    .into(imageView); // Target ImageView
            // -----------------------------------------

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    List<Song> playlist = adapter.currentSongList;

                    if (playlist == null || playlist.isEmpty()) {
                        Log.e("SongAdapter", "Cannot play song, adapter list is null or empty.");
                        Toast.makeText(itemView.getContext(), "Error: Playlist data missing", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (position >= playlist.size()) {
                        Log.e("SongAdapter", "Invalid position " + position + " for list size " + playlist.size());
                        Toast.makeText(itemView.getContext(), "Error: Invalid song position", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Song clickedSong = playlist.get(position);
                    Log.d("SongAdapter", "Clicked song: " + clickedSong.getTitle() + " at position " + position);

                    try {
                        PlaybackViewModel playbackViewModel = new ViewModelProvider((FragmentActivity) itemView.getContext())
                                .get(PlaybackViewModel.class);

                        Log.d("SongAdapter", "Calling playSongList with " + playlist.size() + " songs, starting at index " + position);
                        playbackViewModel.playSongList(playlist, position);

                        NavController navController = Navigation.findNavController(itemView);
                        Bundle args = new Bundle();
                        args.putString("songId", clickedSong.getId());
                        int currentDestinationId = navController.getCurrentDestination() != null ?
                                navController.getCurrentDestination().getId() : 0;

                        if (currentDestinationId != R.id.navigation_song_view) {
                            int actionId = 0;
                            if (currentDestinationId == R.id.navigation_home) {
                                actionId = R.id.action_home_to_song_view;
                            } else if (currentDestinationId == R.id.navigation_explore) {
                                actionId = R.id.action_explore_to_song_view;
                            } else if (currentDestinationId == R.id.navigation_playlist_detail) {
                                actionId = R.id.action_playlist_detail_to_song_view;
                            } else if (currentDestinationId == R.id.navigation_library) {
                                actionId = R.id.action_library_to_song_view;
                            }

                            if (actionId != 0) {
                                navController.navigate(actionId, args);
                            } else {
                                Log.w("SongAdapter", "Navigation to SongView not defined for current destination: " + currentDestinationId);
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

    public static class SongDiffCallback extends DiffUtil.ItemCallback<Song> {
        public static final SongDiffCallback INSTANCE = new SongDiffCallback();

        @Override
        public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.getId() != null && oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.equals(newItem);
        }
    }
}