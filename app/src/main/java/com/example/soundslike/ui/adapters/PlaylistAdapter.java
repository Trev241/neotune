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
        // Adjust layout if needed (e.g., if Library uses song_card_long)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card, parent, false); // Using song_card for grid/horizontal
        // If Library uses a vertical list with song_card_long, you might need viewType logic
        // or a separate adapter. For now, assume song_card is okay.
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
            // Ensure these IDs match your chosen layout (song_card.xml or other)
            imageView = itemView.findViewById(R.id.imageView); // From song_card.xml
            titleView = itemView.findViewById(R.id.textView); // From song_card.xml
            subtitleView = itemView.findViewById(R.id.textView5); // From song_card.xml
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

            if (TextUtils.isEmpty(imageUrl)) {
                String baseUrl = "https://picsum.photos/seed/";
                int imageSize = 200;
                String seed = playlist.getId() != null ? playlist.getId() : "random" + random.nextInt();
                placeholderUrl = baseUrl + seed + "/" + imageSize;
                Log.d("PlaylistAdapter", "No cover URL for '" + playlist.getName() + "', using placeholder: " + placeholderUrl);
            }

            Glide.with(itemView.getContext())
                    .load(TextUtils.isEmpty(imageUrl) ? placeholderUrl : imageUrl) // Load original or placeholder
                    .placeholder(R.drawable.ic_genre_placeholder) // Initial placeholder
                    .error(R.drawable.ic_genre_placeholder)       // Error placeholder
                    .centerCrop() // Scale type
                    .into(imageView);

            // --- Updated Click listener ---
            itemView.setOnClickListener(v -> {
                if (playlist == null || playlist.getId() == null) {
                    Log.e("PlaylistAdapter", "Cannot navigate, playlist or ID is null.");
                    return;
                }
                try {
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle args = new Bundle();
                    args.putString("playlistId", playlist.getId()); // Pass the actual ID

                    int currentDestinationId = navController.getCurrentDestination() != null ?
                            navController.getCurrentDestination().getId() : 0;

                    Log.d("PlaylistAdapter", "Navigating from destination ID: " + currentDestinationId + " to detail view for playlist ID: " + playlist.getId());

                    // Determine the correct action based on the current fragment
                    int actionId = 0;
                    if (currentDestinationId == R.id.navigation_home) {
                        actionId = R.id.action_home_to_playlist_detail;
                    } else if (currentDestinationId == R.id.navigation_playlists) {
                        actionId = R.id.action_playlists_to_playlist_detail;
                    } else if (currentDestinationId == R.id.navigation_library) { // **** ADD THIS CASE ****
                        actionId = R.id.action_library_to_playlist_detail;
                    }
                    // Add other potential source fragments if needed

                    if (actionId != 0) {
                        navController.navigate(actionId, args);
                    } else {
                        Log.e("PlaylistAdapter", "Cannot determine navigation action from destination ID: " + currentDestinationId);
                    }

                } catch (Exception e) { // Catch potential exceptions during navigation
                    Log.e("PlaylistAdapter", "Navigation failed for playlist: " + playlist.getId(), e);
                    // Optionally show a toast to the user
                    // Toast.makeText(itemView.getContext(), "Could not open playlist", Toast.LENGTH_SHORT).show();
                }
            });
            // --------------------------
        }
    }

    // DiffCallback remains the same
    private static class PlaylistDiffCallback extends DiffUtil.ItemCallback<Playlist> {
        public static final PlaylistDiffCallback INSTANCE = new PlaylistDiffCallback();
        @Override
        public boolean areItemsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            // Ensure IDs are not null before comparing
            return oldItem.getId() != null && oldItem.getId().equals(newItem.getId());
        }
        @Override
        public boolean areContentsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            return oldItem.equals(newItem);
        }
    }
}