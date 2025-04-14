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


public class PlaylistAdapter extends ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder> {

    public PlaylistAdapter(/* int sourceFragmentId */) {
        super(PlaylistDiffCallback.INSTANCE);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card, parent, false);
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

        public PlaylistViewHolder(@NonNull View itemView /*, int sourceFragmentId */) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.textView);
            subtitleView = itemView.findViewById(R.id.textView5);
        }

        public void bind(Playlist playlist) {
            titleView.setText(playlist.getName());
            subtitleView.setVisibility(View.GONE);
            imageView.setImageResource(playlist.getCoverArtResId());

            itemView.setOnClickListener(v -> {
                try {
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle args = new Bundle();
                    args.putString("playlistId", playlist.getId());

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