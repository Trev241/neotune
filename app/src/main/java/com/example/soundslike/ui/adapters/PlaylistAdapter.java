package com.example.soundslike.ui.adapters;

import android.os.Bundle; // Import Bundle
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController; // Import NavController
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;
// Remove NavDirections imports if they exist
// import com.example.soundslike.ui.playlists.PlaylistsFragmentDirections;
// import com.example.soundslike.ui.home.HomeFragmentDirections;


public class PlaylistAdapter extends ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder> {

    // Optional: Add a field to know the source if needed, or handle in fragment
    // private final int sourceFragmentId; // e.g., R.id.navigation_home

    public PlaylistAdapter(/* int sourceFragmentId */) {
        super(PlaylistDiffCallback.INSTANCE);
        // this.sourceFragmentId = sourceFragmentId;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card, parent, false); // Using song_card layout
        return new PlaylistViewHolder(view /*, sourceFragmentId */);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView subtitleView;
        // private final int sourceFragmentId; // Optional source tracking

        public PlaylistViewHolder(@NonNull View itemView /*, int sourceFragmentId */) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.textView); // Playlist Name
            subtitleView = itemView.findViewById(R.id.textView5); // Artist/Description field
            // this.sourceFragmentId = sourceFragmentId;
        }

        public void bind(Playlist playlist) {
            titleView.setText(playlist.getName());
            subtitleView.setVisibility(View.GONE); // Hide artist field for playlists
            imageView.setImageResource(playlist.getCoverArtResId()); // Load mock image

            itemView.setOnClickListener(v -> {
                try {
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle args = new Bundle();
                    args.putString("playlistId", playlist.getId()); // Key must match argument name in nav graph

                    // Determine the correct action ID based on the current destination
                    int currentDestinationId = navController.getCurrentDestination() != null ?
                            navController.getCurrentDestination().getId() : 0;

                    if (currentDestinationId == R.id.navigation_home) {
                        navController.navigate(R.id.action_home_to_playlist_detail, args);
                    } else if (currentDestinationId == R.id.navigation_playlists) {
                        navController.navigate(R.id.action_playlists_to_playlist_detail, args);
                    } else {
                        Log.w("PlaylistAdapter", "Navigation action not defined for current destination: " + currentDestinationId);
                    }

                } catch (Exception e) { // Catch broader exceptions during navigation attempt
                    Log.e("PlaylistAdapter", "Navigation failed for playlist " + playlist.getId(), e);
                }
            });
        }
    }

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