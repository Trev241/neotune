package com.example.soundslike.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist; // Use the Playlist model

public class PlaylistSelectionAdapter extends ListAdapter<Playlist, PlaylistSelectionAdapter.PlaylistSelectionViewHolder> {

    private final OnPlaylistSelectedListener listener;

    public interface OnPlaylistSelectedListener {
        void onPlaylistSelected(Playlist playlist);
    }

    public PlaylistSelectionAdapter(@NonNull OnPlaylistSelectedListener listener) {
        super(PlaylistDiffCallback.INSTANCE); // Reuse PlaylistDiffCallback
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist_selection, parent, false);
        return new PlaylistSelectionViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistSelectionViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class PlaylistSelectionViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final OnPlaylistSelectedListener listener;

        PlaylistSelectionViewHolder(@NonNull View itemView, OnPlaylistSelectedListener listener) {
            super(itemView);
            this.listener = listener;
            nameTextView = itemView.findViewById(R.id.playlist_name_text_view);
        }

        void bind(final Playlist playlist) {
            nameTextView.setText(playlist.getName());
            itemView.setOnClickListener(v -> listener.onPlaylistSelected(playlist));
        }
    }

    // Reuse the DiffUtil Callback from PlaylistAdapter
    private static class PlaylistDiffCallback extends DiffUtil.ItemCallback<Playlist> {
        public static final PlaylistDiffCallback INSTANCE = new PlaylistDiffCallback();
        @Override
        public boolean areItemsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            // Ensure Playlist model has getId() and it's reliable
            return oldItem.getId().equals(newItem.getId());
        }
        @Override
        public boolean areContentsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            // Ensure Playlist model has a correct equals() implementation
            return oldItem.equals(newItem);
        }
    }
}