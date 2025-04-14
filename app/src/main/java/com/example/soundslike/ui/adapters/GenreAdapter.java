package com.example.soundslike.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Genre;

public class GenreAdapter extends ListAdapter<Genre, GenreAdapter.GenreViewHolder> {

    public GenreAdapter() {
        super(GenreDiffCallback.INSTANCE);
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_card, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // --- ViewHolder ---
    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.genreImage);
            nameView = itemView.findViewById(R.id.genreName);
        }

        public void bind(Genre genre) {
            nameView.setText(genre.getName());
            imageView.setImageResource(genre.getIconResId()); // Load mock image
            // TODO: Add click listener later
            // itemView.setOnClickListener(v -> { /* handle click */ });
        }
    }

    // --- DiffUtil Callback ---
    private static class GenreDiffCallback extends DiffUtil.ItemCallback<Genre> {
        public static final GenreDiffCallback INSTANCE = new GenreDiffCallback();

        @Override
        public boolean areItemsTheSame(@NonNull Genre oldItem, @NonNull Genre newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Genre oldItem, @NonNull Genre newItem) {
            return oldItem.equals(newItem);
        }
    }
}