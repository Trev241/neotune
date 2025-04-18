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

import com.bumptech.glide.Glide;
import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;
// Removed PlaybackViewModel import as it's not used in this click listener anymore

public class PlaylistAdapter extends ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder> {

    public PlaylistAdapter() {
        super(PlaylistDiffCallback.INSTANCE);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card, parent, false); // Use your playlist item layout
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

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.textView);
            subtitleView = itemView.findViewById(R.id.textView5);
        }

        public void bind(Playlist playlist) {
            titleView.setText(playlist.getName());
            subtitleView.setVisibility(View.GONE); // Or set song count etc.

            Glide.with(itemView.getContext())
                    .load(playlist.getCoverImageUrl()) // Use the URL
                    .placeholder(R.drawable.ic_genre_placeholder)
                    .error(R.drawable.ic_genre_placeholder)
                    .into(imageView);

            // --- TEMPORARY CLICK LISTENER FOR TESTING ---
            itemView.setOnClickListener(v -> {
                try {
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle args = new Bundle();
                    // Pass a dummy ID - PlaylistDetailViewModel will ignore it
                    // and load mock data anyway for this test setup.
                    args.putString("playlistId", "dummy_id_for_test");
                    int currentDestinationId = navController.getCurrentDestination() != null ?
                            navController.getCurrentDestination().getId() : 0;

                    Log.d("PlaylistAdapter", "Navigating to detail view (using dummy ID for test)");

                    // Navigate from appropriate source fragment
                    if (currentDestinationId == R.id.navigation_home) {
                        navController.navigate(R.id.action_home_to_playlist_detail, args);
                    } else if (currentDestinationId == R.id.navigation_playlists) {
                        navController.navigate(R.id.action_playlists_to_playlist_detail, args);
                    }
                    // Add other navigation sources if needed (e.g., Library)

                } catch (Exception e) {
                    // Use a more specific playlist identifier if available for logging
                    Log.e("PlaylistAdapter", "Click failed for playlist item", e);
                }
            });
            // --- END OF TEMPORARY CLICK LISTENER ---
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