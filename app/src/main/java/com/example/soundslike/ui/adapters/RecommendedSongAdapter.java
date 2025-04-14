package com.example.soundslike.ui.adapters;

import android.os.Bundle;
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

import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;


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
            imageView = itemView.findViewById(R.id.imageView2);
            titleView = itemView.findViewById(R.id.songTitle);
            artistView = itemView.findViewById(R.id.textView7);
        }

        public void bind(Song song) {
            titleView.setText(song.getTitle());
            artistView.setText(song.getArtist());
            imageView.setImageResource(song.getAlbumArtResId());

            itemView.setOnClickListener(v -> {
                try {
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle args = new Bundle();
                    args.putString("songId", song.getId());

                    int currentDestinationId = navController.getCurrentDestination() != null ?
                            navController.getCurrentDestination().getId() : 0;

                    if (currentDestinationId == R.id.navigation_home) {
                        navController.navigate(R.id.action_home_to_song_view, args);
                    } else if (currentDestinationId == R.id.navigation_playlist_detail) {
                        navController.navigate(R.id.action_playlist_detail_to_song_view, args);
                    } else {
                        Log.w("RecommendedSongAdapter", "Navigation action not defined for current destination: " + currentDestinationId);
                    }

                } catch (Exception e) { // Catch broader exceptions
                    Log.e("SongAdapter", "Navigation failed for song " + song.getId(), e);
                }
            });
        }
    }

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