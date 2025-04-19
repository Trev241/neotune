package com.example.soundslike.ui.adapters;

import android.os.Bundle;
import android.text.TextUtils; // Import TextUtils
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;

import java.util.Locale;
import java.util.Random; // Import Random

public class PlaylistAdapter extends ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder> {

    public PlaylistAdapter() {
        super(PlaylistDiffCallback.INSTANCE);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use song_card.xml, assuming it has the necessary views
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView subtitleView;
        private final Random random = new Random(); // Instance for placeholder generation

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ensure these IDs match your song_card.xml layout
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.textView);
            subtitleView = itemView.findViewById(R.id.textView5);
        }

        public void bind(Playlist playlist) {
            // Set Playlist Name
            titleView.setText(playlist.getName());

            // Set Subtitle (e.g., Song Count)
            String songCountText = String.format(Locale.getDefault(), "%d songs", playlist.getSongCount());
            subtitleView.setText(songCountText);
            subtitleView.setVisibility(View.VISIBLE); // Make sure it's visible

            // Load Cover Image using Glide
            String imageUrl = playlist.getCoverImageUrl();
            String placeholderUrl = null; // Variable for placeholder URL

            // --- Check if API provided URL is empty/null ---
            if (TextUtils.isEmpty(imageUrl)) {
                // Generate a random placeholder URL if API URL is missing
                String baseUrl = "https://picsum.photos/seed/";
                int imageSize = 200;
                // Use playlist ID for a somewhat consistent random image per playlist
                String seed = playlist.getId() != null ? playlist.getId() : "random" + random.nextInt();
                placeholderUrl = baseUrl + seed + "/" + imageSize;
                Log.d("PlaylistAdapter", "No cover URL for '" + playlist.getName() + "', using placeholder: " + placeholderUrl);
            }
            // ---------------------------------------------

            Glide.with(itemView.getContext())
                    .load(TextUtils.isEmpty(imageUrl) ? placeholderUrl : imageUrl) // Load original or placeholder
                    .placeholder(R.drawable.ic_genre_placeholder) // Initial placeholder
                    .error(R.drawable.ic_genre_placeholder)       // Error placeholder
                    .centerCrop() // Scale type
                    .into(imageView);

            // Click listener to navigate to detail view
            itemView.setOnClickListener(v -> {
                try {
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle args = new Bundle();
                    args.putString("playlistId", playlist.getId()); // Pass the actual ID
                    int currentDestinationId = navController.getCurrentDestination() != null ?
                            navController.getCurrentDestination().getId() : 0;

                    Log.d("PlaylistAdapter", "Navigating to detail view for playlist ID: " + playlist.getId());

                    // Navigate from appropriate source fragment
                    if (currentDestinationId == R.id.navigation_home) {
                        navController.navigate(R.id.action_home_to_playlist_detail, args);
                    } else if (currentDestinationId == R.id.navigation_playlists) {
                        navController.navigate(R.id.action_playlists_to_playlist_detail, args);
                    }
                    // Add other navigation sources if needed

                } catch (Exception e) {
                    Log.e("PlaylistAdapter", "Click failed for playlist: " + playlist.getId(), e);
                }
            });
        }
    }

    // DiffCallback remains the same
    private static class PlaylistDiffCallback extends DiffUtil.ItemCallback<Playlist> {
        public static final PlaylistDiffCallback INSTANCE = new PlaylistDiffCallback();
        @Override
        public boolean areItemsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
        @Override
        public boolean areContentsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            return oldItem.equals(newItem);
        }
    }
}