package com.example.soundslike.ui.adapters;

import android.os.Bundle; // Import Bundle
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController; // Import NavController
import androidx.navigation.Navigation; // Import Navigation
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundslike.R;
import com.example.soundslike.data.models.Artist;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;

public class ArtistAdapter extends ListAdapter<Artist, ArtistAdapter.ArtistViewHolder> {

    public ArtistAdapter() {
        super(ArtistDiffCallback.INSTANCE);
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = getItem(position);
        if (artist != null) {
            holder.bind(artist);
        }
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView imageView;
        private final TextView nameView;
        private final Random random = new Random();

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.artistImage);
            nameView = itemView.findViewById(R.id.artistName);
        }

        public void bind(Artist artist) {
            nameView.setText(artist.getName());

            String imageUrl = artist.getImageUrl();
            String placeholderUrl = null;

            if (TextUtils.isEmpty(imageUrl)) {
                String baseUrl = "https://picsum.photos/seed/";
                int imageSize = 150;
                String seed = artist.getId() != null ? artist.getId() : "artist" + random.nextInt();
                placeholderUrl = baseUrl + seed + "/" + imageSize;
                Log.d("ArtistAdapter", "No image URL for '" + artist.getName() + "', using placeholder: " + placeholderUrl);
            }

            Glide.with(itemView.getContext())
                    .load(TextUtils.isEmpty(imageUrl) ? placeholderUrl : imageUrl)
                    .placeholder(R.drawable.ic_genre_placeholder)
                    .error(R.drawable.ic_genre_placeholder)
                    .centerCrop()
                    .into(imageView);

            // --- Updated Click Listener for Navigation ---
            itemView.setOnClickListener(v -> {
                if (artist == null || artist.getId() == null) {
                    Log.e("ArtistAdapter", "Cannot navigate, artist or ID is null.");
                    return;
                }
                try {
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle args = new Bundle();
                    args.putString("artistId", artist.getId()); // Pass artist ID

                    int currentDestinationId = navController.getCurrentDestination() != null ?
                            navController.getCurrentDestination().getId() : 0;

                    Log.d("ArtistAdapter", "Navigating from destination ID: " + currentDestinationId + " to artist detail for ID: " + artist.getId());

                    int actionId = 0;
                    if (currentDestinationId == R.id.navigation_home) {
                        actionId = R.id.action_home_to_artist_detail;
                    } else if (currentDestinationId == R.id.navigation_library) {
                        actionId = R.id.action_library_to_artist_detail;
                    }
                    // Add other sources if artists are displayed elsewhere

                    if (actionId != 0) {
                        navController.navigate(actionId, args);
                    } else {
                        Log.e("ArtistAdapter", "Cannot determine navigation action to artist detail from destination ID: " + currentDestinationId);
                        Toast.makeText(itemView.getContext(), "Cannot open artist details", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("ArtistAdapter", "Navigation failed for artist: " + artist.getId(), e);
                    Toast.makeText(itemView.getContext(), "Could not open artist details", Toast.LENGTH_SHORT).show();
                }
            });
            // -------------------------------------------
        }
    }

    private static class ArtistDiffCallback extends DiffUtil.ItemCallback<Artist> {
        public static final ArtistDiffCallback INSTANCE = new ArtistDiffCallback();

        @Override
        public boolean areItemsTheSame(@NonNull Artist oldItem, @NonNull Artist newItem) {
            return oldItem.getId() != null && oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Artist oldItem, @NonNull Artist newItem) {
            return oldItem.equals(newItem);
        }
    }
}