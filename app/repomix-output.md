This file is a merged representation of the entire codebase, combined into a single document by Repomix.

# File Summary

## Purpose
This file contains a packed representation of the entire repository's contents.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.

## File Format
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Multiple file entries, each consisting of:
  a. A header with the file path (## File: path/to/file)
  b. The full contents of the file in a code block

## Usage Guidelines
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.

## Notes
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Files are sorted by Git change count (files with more changes are at the bottom)

## Additional Info

# Directory Structure
```
src/
  androidTest/
    java/
      com/
        example/
          soundslike/
            ExampleInstrumentedTest.java
  main/
    java/
      com/
        example/
          soundslike/
            data/
              models/
                Genre.java
                Playlist.java
                Song.java
            ui/
              adapters/
                GenreAdapter.java
                PlaylistAdapter.java
                RecommendedSongAdapter.java
              explore/
                ExploreFragment.java
              home/
                HomeFragment.java
                HomeViewModel.java
              library/
                LibraryFragment.java
              playlistdetail/
                PlaylistDetailFragment.java
                PlaylistDetailViewModel.java
              playlists/
                PlaylistsFragment.java
                PlaylistsViewModel.java
              song/
                SongViewFragment.java
                SongViewModel.java
            MainActivity.java
    res/
      drawable/
        bottom_bar_background.xml
        ic_arrow_back.xml
        ic_dots_indicator.xml
        ic_download.xml
        ic_explore.xml
        ic_genre_placeholder.xml
        ic_heart_filled.xml
        ic_heart.xml
        ic_home.xml
        ic_launcher_background.xml
        ic_launcher_foreground.xml
        ic_library_music.xml
        ic_next.xml
        ic_pause.xml
        ic_play.xml
        ic_previous.xml
        ic_queue.xml
        ic_shuffle.xml
        play_pause_background.xml
      font/
        cozy.xml
      font-v26/
        cozy.xml
      layout/
        activity_main.xml
        fragment_explore.xml
        fragment_home.xml
        fragment_library.xml
        fragment_playlist_detail.xml
        fragment_playlists.xml
        fragment_song_view.xml
        genre_card.xml
        song_card_long.xml
        song_card.xml
      menu/
        bottom_navigation_menu.xml
      mipmap-anydpi-v26/
        ic_launcher_round.xml
        ic_launcher.xml
      navigation/
        mobile_navigation.xml
      values/
        colors.xml
        strings.xml
        themes.xml
      values-night/
        themes.xml
      xml/
        backup_rules.xml
        data_extraction_rules.xml
    AndroidManifest.xml
  test/
    java/
      com/
        example/
          soundslike/
            ExampleUnitTest.java
.gitignore
build.gradle.kts
proguard-rules.pro
```

# Files

## File: src/androidTest/java/com/example/soundslike/ExampleInstrumentedTest.java
```java
package com.example.soundslike;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.soundslike", appContext.getPackageName());
    }
}
```

## File: src/main/java/com/example/soundslike/data/models/Genre.java
```java
package com.example.soundslike.data.models;

import androidx.annotation.DrawableRes;

import java.util.Objects;

public class Genre {
    private final String id;
    private final String name;
    @DrawableRes
    private final int iconResId;

    public Genre(String id, String name, @DrawableRes int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @DrawableRes
    public int getIconResId() {
        return iconResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return iconResId == genre.iconResId &&
                Objects.equals(id, genre.id) &&
                Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, iconResId);
    }
}
```

## File: src/main/java/com/example/soundslike/data/models/Playlist.java
```java
package com.example.soundslike.data.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Playlist {
    private final String id;
    private final String name;
    @Nullable
    private final String description;
    @DrawableRes
    private final int coverArtResId;

    public Playlist(String id, String name, @Nullable String description, @DrawableRes int coverArtResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coverArtResId = coverArtResId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @DrawableRes
    public int getCoverArtResId() {
        return coverArtResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return coverArtResId == playlist.coverArtResId &&
                Objects.equals(id, playlist.id) &&
                Objects.equals(name, playlist.name) &&
                Objects.equals(description, playlist.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, coverArtResId);
    }
}
```

## File: src/main/java/com/example/soundslike/data/models/Song.java
```java
package com.example.soundslike.data.models;

import androidx.annotation.DrawableRes;

import java.util.Objects;

public class Song {
    private final String id;
    private final String title;
    private final String artist;
    @DrawableRes
    private final int albumArtResId;
    private final long durationMillis;

    public Song(String id, String title, String artist, @DrawableRes int albumArtResId, long durationMillis) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.albumArtResId = albumArtResId;
        this.durationMillis = durationMillis;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @DrawableRes
    public int getAlbumArtResId() {
        return albumArtResId;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return albumArtResId == song.albumArtResId &&
                durationMillis == song.durationMillis &&
                Objects.equals(id, song.id) &&
                Objects.equals(title, song.title) &&
                Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artist, albumArtResId, durationMillis);
    }
}
```

## File: src/main/java/com/example/soundslike/ui/adapters/GenreAdapter.java
```java
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
```

## File: src/main/java/com/example/soundslike/ui/adapters/PlaylistAdapter.java
```java
package com.example.soundslike.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;

public class PlaylistAdapter extends ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder> {

    public PlaylistAdapter() {
        super(PlaylistDiffCallback.INSTANCE);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.textView);
            subtitleView = itemView.findViewById(R.id.textView5);
        }

        // Inside PlaylistViewHolder class, bind method:
        public void bind(Playlist playlist) {
            titleView.setText(playlist.getName());
            subtitleView.setVisibility(View.GONE);
            imageView.setImageResource(playlist.getCoverArtResId());

            // --- ADD CLICK LISTENER ---
            itemView.setOnClickListener(v -> {
                // Decide where this adapter is used to choose the correct action
                // This example assumes navigation from PlaylistsFragment
                // You might need to pass a listener interface or use NavDirections specific to the source fragment
                try {
                    // Option 1: If navigating from PlaylistsFragment
                    NavDirections action = PlaylistsFragmentDirections.actionPlaylistsToPlaylistDetail(playlist.getId());
                    Navigation.findNavController(itemView).navigate(action);

                    // Option 2: If navigating from HomeFragment (use different action ID)
                    // NavDirections action = HomeFragmentDirections.actionHomeToPlaylistDetail(playlist.getId());
                    // Navigation.findNavController(itemView).navigate(action);

                } catch (IllegalArgumentException e) {
                    // Handle case where NavController is not found or action is invalid
                    Log.e("PlaylistAdapter", "Navigation failed: ", e);
                }
            });
            // --------------------------
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
```

## File: src/main/java/com/example/soundslike/ui/adapters/RecommendedSongAdapter.java
```java
package com.example.soundslike.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
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
        // Ensure these IDs exist in song_card_long.xml
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView artistView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            // Assuming the first TextView is title, add an ID if it doesn't have one
            titleView = itemView.findViewById(R.id.songTitle); // Example ID, add this in XML if needed
            artistView = itemView.findViewById(R.id.textView7);
        }

        // Inside SongViewHolder class, bind method:
        public void bind(Song song) {
            titleView.setText(song.getTitle());
            artistView.setText(song.getArtist());
            imageView.setImageResource(song.getAlbumArtResId());

            // --- ADD CLICK LISTENER ---
            itemView.setOnClickListener(v -> {
                // Decide where this adapter is used to choose the correct action
                // This example assumes navigation from PlaylistDetailFragment
                try {
                    // Option 1: If navigating from PlaylistDetailFragment
                    NavDirections action = PlaylistDetailFragmentDirections.actionPlaylistDetailToSongView(song.getId());
                    Navigation.findNavController(itemView).navigate(action);

                    // Option 2: If navigating from HomeFragment (use different action ID)
                    // NavDirections action = HomeFragmentDirections.actionHomeToSongView(song.getId());
                    // Navigation.findNavController(itemView).navigate(action);

                } catch (IllegalArgumentException e) {
                    Log.e("SongAdapter", "Navigation failed: ", e);
                }
            });
            // --------------------------
        }
    }

    // --- DiffUtil Callback ---
    // Can be reused by other adapters displaying Song objects
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
```

## File: src/main/java/com/example/soundslike/ui/explore/ExploreFragment.java
```java
package com.example.soundslike.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.soundslike.R;

public class ExploreFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
}
```

## File: src/main/java/com/example/soundslike/ui/home/HomeFragment.java
```java
package com.example.soundslike.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.soundslike.R;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
```

## File: src/main/java/com/example/soundslike/ui/home/HomeViewModel.java
```java
package com.example.soundslike.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R; // Import your R class
import com.example.soundslike.data.models.Genre;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.Song;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Playlist>> _playlists = new MutableLiveData<>();
    public LiveData<List<Playlist>> getPlaylists() {
        return _playlists;
    }

    private final MutableLiveData<List<Genre>> _genres = new MutableLiveData<>();
    public LiveData<List<Genre>> getGenres() {
        return _genres;
    }

    private final MutableLiveData<List<Song>> _recommendedSongs = new MutableLiveData<>();
    public LiveData<List<Song>> getRecommendedSongs() {
        return _recommendedSongs;
    }

    public HomeViewModel() {
        loadMockData();
    }

    private void loadMockData() {
        // Mock Playlists
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist("pl1", "Chill Vibes", "Relaxing tunes", R.drawable.album_art_get_lucky)); // Use actual drawable
        mockPlaylists.add(new Playlist("pl2", "Workout Beats", "High energy tracks", R.drawable.ic_launcher_background));
        mockPlaylists.add(new Playlist("pl3", "Focus Flow", "Instrumental focus music", R.drawable.ic_genre_placeholder));
        mockPlaylists.add(new Playlist("pl4", "Road Trip", null, R.drawable.album_art_get_lucky));
        mockPlaylists.add(new Playlist("pl5", "Throwbacks", "Hits from the 2000s", R.drawable.ic_launcher_background));
        _playlists.setValue(mockPlaylists);

        // Mock Genres
        List<Genre> mockGenres = new ArrayList<>();
        mockGenres.add(new Genre("g1", "Pop", R.drawable.ic_genre_placeholder));
        mockGenres.add(new Genre("g2", "Rock", R.drawable.ic_genre_placeholder));
        mockGenres.add(new Genre("g3", "Hip Hop", R.drawable.ic_genre_placeholder));
        mockGenres.add(new Genre("g4", "Electronic", R.drawable.ic_genre_placeholder));
        _genres.setValue(mockGenres);

        // Mock Recommended Songs
        List<Song> mockSongs = new ArrayList<>();
        mockSongs.add(new Song("s1", "Get Lucky", "Daft Punk", R.drawable.album_art_get_lucky, 248000));
        mockSongs.add(new Song("s2", "Bohemian Rhapsody", "Queen", R.drawable.ic_launcher_background, 355000));
        mockSongs.add(new Song("s3", "Shape of You", "Ed Sheeran", R.drawable.ic_genre_placeholder, 233000));
        mockSongs.add(new Song("s4", "Blinding Lights", "The Weeknd", R.drawable.album_art_get_lucky, 200000));
        mockSongs.add(new Song("s5", "Starlight", "Muse", R.drawable.ic_launcher_background, 240000));
        mockSongs.add(new Song("s6", "One More Time", "Daft Punk", R.drawable.ic_genre_placeholder, 320000));
        mockSongs.add(new Song("s7", "Uptown Funk", "Mark Ronson ft. Bruno Mars", R.drawable.album_art_get_lucky, 270000));
        _recommendedSongs.setValue(mockSongs);
    }
}
```

## File: src/main/java/com/example/soundslike/ui/library/LibraryFragment.java
```java
package com.example.soundslike.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.soundslike.R;

public class LibraryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }
}
```

## File: src/main/java/com/example/soundslike/ui/playlistdetail/PlaylistDetailFragment.java
```java
package com.example.soundslike.ui.playlistdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar; // Use androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.RecommendedSongAdapter; // Reuse adapter
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class PlaylistDetailFragment extends Fragment {

    private PlaylistDetailViewModel viewModel;
    private RecommendedSongAdapter songAdapter;
    private RecyclerView songsRecyclerView;
    private ImageView headerImageView;
    private TextView headerTitleTextView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout renamed in Phase 1 adjustment
        View view = inflater.inflate(R.layout.fragment_playlist_detail, container, false);

        viewModel = new ViewModelProvider(this).get(PlaylistDetailViewModel.class);

        // Find views (ensure IDs exist in fragment_playlist_detail.xml)
        songsRecyclerView = view.findViewById(R.id.songs_recycler_view); // ADD THIS ID TO THE RECYCLERVIEW IN XML
        headerImageView = view.findViewById(R.id.header_image); // ADD THIS ID TO THE IMAGEVIEW IN XML
        // The TextView in CollapsingToolbarLayout might not need an ID if we set title on Toolbar/CollapsingToolbar
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout); // ADD THIS ID
        toolbar = view.findViewById(R.id.toolbar); // ADD THIS ID

        setupToolbar(view);
        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();

        // TODO: Get playlistId from arguments if passed via navigation
        // String playlistId = PlaylistDetailFragmentArgs.fromBundle(getArguments()).getPlaylistId();
        // viewModel.loadPlaylistData(playlistId); // Load actual data later
    }

    private void setupToolbar(View view) {
        // Set up the toolbar for navigation
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back); // Use your back arrow drawable
        toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        // We'll set the title dynamically when data loads
    }

    private void setupRecyclerView() {
        songAdapter = new RecommendedSongAdapter(); // Reusing this adapter
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsRecyclerView.setAdapter(songAdapter);

        // TODO: Set item click listener on adapter to navigate to SongViewFragment
        // songAdapter.setOnItemClickListener(song -> {
        //     PlaylistDetailFragmentDirections.ActionPlaylistDetailFragmentToSongViewFragment action =
        //         PlaylistDetailFragmentDirections.actionPlaylistDetailFragmentToSongViewFragment(song.getId());
        //     NavHostFragment.findNavController(this).navigate(action);
        // });
    }

    private void observeViewModel() {
        viewModel.getPlaylistDetails().observe(getViewLifecycleOwner(), playlist -> {
            if (playlist != null) {
                // Update header
                // Use Glide/Coil here later to load URL
                headerImageView.setImageResource(playlist.getCoverArtResId());
                collapsingToolbarLayout.setTitle(playlist.getName()); // Set title on collapsing toolbar
                // Or set title on toolbar if you prefer: toolbar.setTitle(playlist.getName());
            }
        });

        viewModel.getPlaylistSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                songAdapter.submitList(songs);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        songsRecyclerView = null; // Avoid memory leaks
        headerImageView = null;
        collapsingToolbarLayout = null;
        toolbar = null;
    }
}
```

## File: src/main/java/com/example/soundslike/ui/playlistdetail/PlaylistDetailViewModel.java
```java
package com.example.soundslike.ui.playlistdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.Song;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailViewModel extends ViewModel {

    private final MutableLiveData<Playlist> _playlistDetails = new MutableLiveData<>();
    public LiveData<Playlist> getPlaylistDetails() {
        return _playlistDetails;
    }

    private final MutableLiveData<List<Song>> _playlistSongs = new MutableLiveData<>();
    public LiveData<List<Song>> getPlaylistSongs() {
        return _playlistSongs;
    }

    // In a real app, you'd pass the playlistId here
    public PlaylistDetailViewModel() {
        loadMockPlaylistData("mock_id_1"); // Load mock data for a specific playlist
    }

    // In a real app, this would fetch data based on playlistId
    public void loadMockPlaylistData(String playlistId) {
        // Mock Playlist Details (Example: "Chill Vibes")
        _playlistDetails.setValue(
                new Playlist(playlistId, "Chill Vibes", "Relaxing tunes for your evening", R.drawable.album_art_get_lucky)
        );

        // Mock Songs for this specific playlist
        List<Song> mockSongs = new ArrayList<>();
        mockSongs.add(new Song("s1", "Get Lucky", "Daft Punk", R.drawable.album_art_get_lucky, 248000));
        mockSongs.add(new Song("s6", "One More Time", "Daft Punk", R.drawable.ic_genre_placeholder, 320000));
        mockSongs.add(new Song("s8", "Weightless", "Marconi Union", R.drawable.ic_launcher_background, 480000)); // Example different song
        mockSongs.add(new Song("s9", "Teardrop", "Massive Attack", R.drawable.album_art_get_lucky, 321000));
        _playlistSongs.setValue(mockSongs);
    }
}
```

## File: src/main/java/com/example/soundslike/ui/playlists/PlaylistsFragment.java
```java
package com.example.soundslike.ui.playlists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager; // Import GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundslike.R;
import com.example.soundslike.ui.adapters.PlaylistAdapter; // Reuse the adapter from Phase 2

public class PlaylistsFragment extends Fragment {

    private PlaylistsViewModel playlistsViewModel;
    private RecyclerView playlistsRecyclerView;
    private PlaylistAdapter playlistAdapter; // Reusing the adapter

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the correct layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        // Initialize ViewModel
        playlistsViewModel = new ViewModelProvider(this).get(PlaylistsViewModel.class);

        // Find RecyclerView
        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view); // Ensure this ID exists in fragment_playlists.xml

        // Setup RecyclerView
        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe LiveData from ViewModel
        observeViewModel();
    }

    private void setupRecyclerView() {
        playlistAdapter = new PlaylistAdapter(); // Create instance of the adapter

        // --- Choose Layout Manager ---
        // Option 1: Linear List
        // playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Option 2: Grid Layout (e.g., 2 columns) - Often looks better for playlists
        int numberOfColumns = 2;
        playlistsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        // --------------------------

        playlistsRecyclerView.setAdapter(playlistAdapter);
        // Optional: Add item decoration for spacing if needed
    }

    private void observeViewModel() {
        playlistsViewModel.getUserPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            // Update the adapter
            if (playlists != null) {
                playlistAdapter.submitList(playlists);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Avoid memory leaks
        playlistsRecyclerView = null;
        // Adapter can be left to GC if it doesn't hold strong references inappropriately
    }
}
```

## File: src/main/java/com/example/soundslike/ui/playlists/PlaylistsViewModel.java
```java
package com.example.soundslike.ui.playlists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R; // Import your R class
import com.example.soundslike.data.models.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsViewModel extends ViewModel {

    private final MutableLiveData<List<Playlist>> _userPlaylists = new MutableLiveData<>();
    public LiveData<List<Playlist>> getUserPlaylists() {
        return _userPlaylists;
    }

    public PlaylistsViewModel() {
        loadMockPlaylists();
    }

    private void loadMockPlaylists() {
        // Mock User Playlists (different from the ones maybe shown on Home)
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist("upl1", "My Awesome Mix", "My favorite tracks", R.drawable.album_art_get_lucky));
        mockPlaylists.add(new Playlist("upl2", "Gym Power", "Motivation!", R.drawable.ic_launcher_background));
        mockPlaylists.add(new Playlist("upl3", "Study Zone", null, R.drawable.ic_genre_placeholder));
        mockPlaylists.add(new Playlist("upl4", "Late Night Drive", "Synthwave and chill", R.drawable.album_art_get_lucky));
        mockPlaylists.add(new Playlist("upl5", "Liked Songs", "All the songs you liked", R.drawable.ic_heart_filled)); // Example for Liked Songs
        _userPlaylists.setValue(mockPlaylists);
        // In a real app, you'd fetch this from a local database or API
    }
}
```

## File: src/main/java/com/example/soundslike/ui/song/SongViewFragment.java
```java
package com.example.soundslike.ui.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.soundslike.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SongViewFragment extends Fragment {

    private SongViewModel viewModel;

    // View references
    private ImageButton backButton;
    private ShapeableImageView albumArtImageView;
    private TextView titleTextView;
    private TextView artistTextView;
    private TextView currentTimeTextView;
    private TextView totalTimeTextView;
    private SeekBar seekBar;
    private ImageButton playPauseButton;
    // Add other buttons (prev, next, like, queue, shuffle, download) if needed

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout renamed earlier
        View view = inflater.inflate(R.layout.fragment_song_view, container, false);

        viewModel = new ViewModelProvider(this).get(SongViewModel.class);

        // Find views
        backButton = view.findViewById(R.id.button_back);
        albumArtImageView = view.findViewById(R.id.image_album_art);
        titleTextView = view.findViewById(R.id.text_song_title);
        artistTextView = view.findViewById(R.id.text_artist_name);
        currentTimeTextView = view.findViewById(R.id.text_current_time);
        totalTimeTextView = view.findViewById(R.id.text_total_time);
        seekBar = view.findViewById(R.id.seek_bar);
        playPauseButton = view.findViewById(R.id.button_play_pause);
        // Find other buttons...

        setupUIListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();

        // TODO: Get songId from arguments if passed via navigation
        // String songId = SongViewFragmentArgs.fromBundle(getArguments()).getSongId();
        // viewModel.loadSong(songId); // Load actual data later
    }

    private void setupUIListeners() {
        backButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        // TODO: Add listeners for play/pause, next, prev, seekbar changes later
        // playPauseButton.setOnClickListener(v -> { /* send command to service */ });
        // seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { ... });
    }

    private void observeViewModel() {
        viewModel.getCurrentSong().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                titleTextView.setText(song.getTitle());
                artistTextView.setText(song.getArtist());
                // Use Glide/Coil later
                albumArtImageView.setImageResource(song.getAlbumArtResId());

                // Update duration display
                totalTimeTextView.setText(formatMillis(song.getDurationMillis()));
                seekBar.setMax((int) song.getDurationMillis());
                // Reset progress and current time for new song
                seekBar.setProgress(0);
                currentTimeTextView.setText(formatMillis(0));
            }
        });

        // TODO: Observe playback state (isPlaying, progress) later
        // viewModel.getIsPlaying().observe(...) { isPlaying -> updatePlayPauseButton(isPlaying); }
        // viewModel.getCurrentProgressMillis().observe(...) { progress -> updateSeekBar(progress); }
    }

    // Helper to format milliseconds to mm:ss
    private String formatMillis(long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(minutes);
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Null out view references
        backButton = null;
        albumArtImageView = null;
        titleTextView = null;
        artistTextView = null;
        currentTimeTextView = null;
        totalTimeTextView = null;
        seekBar = null;
        playPauseButton = null;
        // Null out others...
    }
}
```

## File: src/main/java/com/example/soundslike/ui/song/SongViewModel.java
```java
package com.example.soundslike.ui.song;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.R;
import com.example.soundslike.data.models.Song;

public class SongViewModel extends ViewModel {

    private final MutableLiveData<Song> _currentSong = new MutableLiveData<>();
    public LiveData<Song> getCurrentSong() {
        return _currentSong;
    }

    // TODO: Add LiveData for playback state (isPlaying, progress, duration) later

    // In a real app, you'd observe the playback service or receive songId
    public SongViewModel() {
        loadMockSong("mock_song_id"); // Load a default mock song
    }

    // In a real app, this would fetch details or get from playback service
    public void loadMockSong(String songId) {
        // Load a specific mock song (e.g., Get Lucky)
        _currentSong.setValue(
                new Song("s1", "Get Lucky", "Daft Punk", R.drawable.album_art_get_lucky, 248000)
        );
        // TODO: Initialize playback state LiveData here later
    }
}
```

## File: src/main/java/com/example/soundslike/MainActivity.java
```java
package com.example.soundslike;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.playlist_view_activity);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navView == null) {
            Log.e("MainActivity", "BottomNavigationView not found! Check ID in activity_main.xml");
            return;
        }
        if (navHostFragment == null) {
            Log.e("MainActivity", "NavHostFragment not found! Check ID in activity_main.xml");
            return;
        }

        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(navView, navController);
    }
}
```

## File: src/main/res/drawable/bottom_bar_background.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/accent_orange" />
    <corners
        android:topLeftRadius="20dp"
        android:topRightRadius="20dp" />
</shape>
```

## File: src/main/res/drawable/ic_arrow_back.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M20,11H7.83l5.59,-5.59L12,4l-8,8 8,8 1.41,-1.41L7.83,13H20v-2z"/>
</vector>
```

## File: src/main/res/drawable/ic_dots_indicator.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="4dp"
    android:viewportWidth="24"
    android:viewportHeight="4">
    <path
        android:fillColor="#FF000000"
        android:pathData="M4,1h3v2h-3z M9,1h3v2h-3z M14,1h3v2h-3z M19,1h3v2h-3z"/> <!-- Slightly wider dots -->
</vector>
```

## File: src/main/res/drawable/ic_download.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M19,9h-4V3H9v6H5l7,7 7,-7zM5,18v2h14v-2H5z"/>
</vector>
```

## File: src/main/res/drawable/ic_explore.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M12,10.9c-0.61,0 -1.1,0.49 -1.1,1.1s0.49,1.1 1.1,1.1c0.61,0 1.1,-0.49 1.1,-1.1s-0.49,-1.1 -1.1,-1.1zM12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM14.19,14.19L6,18l3.81,-8.19L18,6l-3.81,8.19z"/>
</vector>
```

## File: src/main/res/drawable/ic_genre_placeholder.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="64dp"
    android:height="64dp"
    android:viewportWidth="64"
    android:viewportHeight="64">
    <path
        android:fillColor="#CCCCCC"
        android:pathData="M32,4
                          C17.64,4 6,15.64 6,30
                          s11.64,26 26,26
                          26,-11.64 26,-26
                          S46.36,4 32,4z"/>
    <path
        android:fillColor="#666666"
        android:pathData="M42,20
                          l-10,4
                          V14
                          h-4
                          v18
                          l12,-4
                          V20z"/>
</vector>
```

## File: src/main/res/drawable/ic_heart_filled.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M12,21.35l-1.45,-1.32C5.4,15.36 2,12.28 2,8.5 2,5.42 4.42,3 7.5,3c1.74,0 3.41,0.81 4.5,2.09C13.09,3.81 14.76,3 16.5,3 19.58,3 22,5.42 22,8.5c0,3.78 -3.4,6.86 -8.55,11.54L12,21.35z"/>
</vector>
```

## File: src/main/res/drawable/ic_heart.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M16.5,3c-1.74,0 -3.41,0.81 -4.5,2.09C10.91,3.81 9.24,3 7.5,3 4.42,3 2,5.42 2,8.5c0,3.78 3.4,6.86 8.55,11.54L12,21.35l1.45,-1.32C18.6,15.36 22,12.28 22,8.5 22,5.42 19.58,3 16.5,3zM12.1,18.55l-0.1,0.1 -0.1,-0.1C7.14,14.24 4,11.39 4,8.5 4,6.5 5.5,5 7.5,5c1.54,0 3.04,0.99 3.57,2.36h1.87C13.46,5.99 14.96,5 16.5,5c2,0 3.5,1.5 3.5,3.5 0,2.89 -3.14,5.74 -7.9,10.05z"/>
</vector>
```

## File: src/main/res/drawable/ic_home.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M10,20v-6h4v6h5v-8h3L12,3 2,12h3v8z"/>
</vector>
```

## File: src/main/res/drawable/ic_launcher_background.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#3DDC84"
        android:pathData="M0,0h108v108h-108z" />
    <path
        android:fillColor="#00000000"
        android:pathData="M9,0L9,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,0L19,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,0L29,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,0L39,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,0L49,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,0L59,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,0L69,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,0L79,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M89,0L89,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M99,0L99,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,9L108,9"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,19L108,19"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,29L108,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,39L108,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,49L108,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,59L108,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,69L108,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,79L108,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,89L108,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,99L108,99"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,29L89,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,39L89,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,49L89,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,59L89,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,69L89,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,79L89,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,19L29,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,19L39,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,19L49,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,19L59,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,19L69,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,19L79,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
</vector>
```

## File: src/main/res/drawable/ic_launcher_foreground.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path android:pathData="M31,63.928c0,0 6.4,-11 12.1,-13.1c7.2,-2.6 26,-1.4 26,-1.4l38.1,38.1L107,108.928l-32,-1L31,63.928z">
        <aapt:attr name="android:fillColor">
            <gradient
                android:endX="85.84757"
                android:endY="92.4963"
                android:startX="42.9492"
                android:startY="49.59793"
                android:type="linear">
                <item
                    android:color="#44000000"
                    android:offset="0.0" />
                <item
                    android:color="#00000000"
                    android:offset="1.0" />
            </gradient>
        </aapt:attr>
    </path>
    <path
        android:fillColor="#FFFFFF"
        android:fillType="nonZero"
        android:pathData="M65.3,45.828l3.8,-6.6c0.2,-0.4 0.1,-0.9 -0.3,-1.1c-0.4,-0.2 -0.9,-0.1 -1.1,0.3l-3.9,6.7c-6.3,-2.8 -13.4,-2.8 -19.7,0l-3.9,-6.7c-0.2,-0.4 -0.7,-0.5 -1.1,-0.3C38.8,38.328 38.7,38.828 38.9,39.228l3.8,6.6C36.2,49.428 31.7,56.028 31,63.928h46C76.3,56.028 71.8,49.428 65.3,45.828zM43.4,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2c-0.3,-0.7 -0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C45.3,56.528 44.5,57.328 43.4,57.328L43.4,57.328zM64.6,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2s-0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C66.5,56.528 65.6,57.328 64.6,57.328L64.6,57.328z"
        android:strokeWidth="1"
        android:strokeColor="#00000000" />
</vector>
```

## File: src/main/res/drawable/ic_library_music.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M20,2H8C6.9,2 6,2.9 6,4v12c0,1.1 0.9,2 2,2h12c1.1,0 2,-0.9 2,-2V4c0,-1.1 -0.9,-2 -2,-2zM12,14.5v-9l6,2.5v6l-6,-2.5zM4,6H2v14c0,1.1 0.9,2 2,2h14v-2H4V6z"/>
</vector>
```

## File: src/main/res/drawable/ic_next.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M6,18l8.5,-6L6,6v12zM16,6v12h2V6h-2z"/>
</vector>
```

## File: src/main/res/drawable/ic_pause.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M6,19h4V5H6v14zm8,-14v14h4V5h-4z"/>
</vector>
```

## File: src/main/res/drawable/ic_play.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M8,5v14l11,-7z"/>
</vector>
```

## File: src/main/res/drawable/ic_previous.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M6,6h2v12H6zM9.5,12l8.5,6V6z"/>
</vector>
```

## File: src/main/res/drawable/ic_queue.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M15,6H3v2h12V6zm0,4H3v2h12v-2zM3,16h8v-2H3v2zM17,6v8.18c-0.31,-0.11 -0.65,-0.18 -1,-0.18 -1.66,0 -3,1.34 -3,3s1.34,3 3,3 3,-1.34 3,-3V8h3V6h-5z"/>
</vector>
```

## File: src/main/res/drawable/ic_shuffle.xml
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M10.59,9.17L5.41,4 4,5.41l5.17,5.17 1.42,-1.41zM14.5,4l2.04,2.04L4,18.59 5.41,20 17.96,7.46 20,9.5V4h-5.5zm.33,9.41l-1.41,1.41 3.13,3.13L14.5,20H20v-5.5l-2.04,2.04 -3.13,-3.13z"/>
</vector>
```

## File: src/main/res/drawable/play_pause_background.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="@color/accent_orange" />
    <size
        android:width="72dp"
        android:height="72dp" />
</shape>
```

## File: src/main/res/font/cozy.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<font-family xmlns:android="http://schemas.android.com/apk/res/android">
    <font
        android:font="@font/livvic_regular"/>
</font-family>
```

## File: src/main/res/font-v26/cozy.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<font-family xmlns:android="http://schemas.android.com/apk/res/android">
    <font
        android:font="@font/livvic_regular"/>
</font-family>
```

## File: src/main/res/layout/activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- Navigation Host Fragment -->
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment_activity_main"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toTopOf="@+id/bottom_navigation_view"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/mobile_navigation" />

    <!-- Bottom Navigation View -->
    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation_view"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="0dp"
        android:layout_marginEnd="0dp"
        android:background="?android:attr/windowBackground"
        app:labelVisibilityMode="labeled"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:menu="@menu/bottom_navigation_menu" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## File: src/main/res/layout/fragment_explore.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Explore Screen Placeholder"
        android:layout_gravity="center" />
</FrameLayout>
```

## File: src/main/res/layout/fragment_home.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.core.widget.NestedScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true"
    tools:context=".ui.home.HomeFragment">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingTop="24dp"
        android:paddingBottom="24dp">

        <TextView
            android:id="@+id/text_greeting"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="24dp"
            android:layout_marginEnd="24dp"
            android:layout_marginBottom="12dp"
            android:text="Hi, Trevis!"
            android:textSize="48sp" />

        <TextView
            android:id="@+id/header_playlists"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginStart="24dp"
            android:layout_marginEnd="24dp"
            android:layout_marginBottom="8dp"
            android:text="Playlists"
            android:textSize="34sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/playlists_recycler_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="24dp"
            android:clipToPadding="false"
            android:orientation="horizontal"
            android:paddingStart="16dp"
            android:paddingEnd="16dp"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
            tools:itemCount="5"
            tools:listitem="@layout/song_card" />

        <TextView
            android:id="@+id/header_genres"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="24dp"
            android:layout_marginEnd="24dp"
            android:layout_marginBottom="8dp"
            android:text="Genres"
            android:textSize="34sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/genres_recycler_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="24dp"
            android:clipToPadding="false"
            android:orientation="horizontal"
            android:paddingStart="16dp"
            android:paddingEnd="16dp"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
            tools:itemCount="3"
            tools:listitem="@layout/genre_card" />

        <TextView
            android:id="@+id/header_recommended"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="24dp"
            android:layout_marginEnd="24dp"
            android:layout_marginBottom="8dp"
            android:text="Recommended"
            android:textSize="34sp"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recommended_recycler_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:nestedScrollingEnabled="false"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
            tools:itemCount="7"
            tools:listitem="@layout/song_card_long" />

    </LinearLayout>
</androidx.core.widget.NestedScrollView>
```

## File: src/main/res/layout/fragment_library.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Library Screen Placeholder"
        android:layout_gravity="center" />
</FrameLayout>
```

## File: src/main/res/layout/fragment_playlist_detail.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context=".ui.playlistdetail.PlaylistDetailFragment">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/app_bar_layout"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    android:fitsSystemWindows="true"
    android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
    app:liftOnScroll="false">

    <com.google.android.material.appbar.CollapsingToolbarLayout
        android:id="@+id/collapsing_toolbar_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    app:contentScrim="?attr/colorPrimary"
    app:expandedTitleMarginEnd="64dp"
    app:expandedTitleMarginStart="48dp"
    app:layout_scrollFlags="scroll|exitUntilCollapsed|snap">

    <ImageView
        android:id="@+id/header_image"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:contentDescription="@string/cd_album_art"
    android:fitsSystemWindows="true"
    android:scaleType="centerCrop"
    app:layout_collapseMode="parallax"
    tools:src="@drawable/ic_launcher_background" />


    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    app:layout_collapseMode="pin"
    app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />

</com.google.android.material.appbar.CollapsingToolbarLayout>
    </com.google.android.material.appbar.AppBarLayout>

<androidx.core.widget.NestedScrollView
android:layout_width="match_parent"
android:layout_height="match_parent"
android:overScrollMode="never"
app:layout_behavior="@string/appbar_scrolling_view_behavior">

<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/songs_recycler_view"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:nestedScrollingEnabled="false"
android:padding="8dp"
app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
tools:listitem="@layout/song_card_long"
tools:itemCount="10"/>

</androidx.core.widget.NestedScrollView>

    </androidx.coordinatorlayout.widget.CoordinatorLayout>
```

## File: src/main/res/layout/fragment_playlists.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.playlists.PlaylistsFragment">

    <!-- Make sure the ID matches what's used in PlaylistsFragment.java -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/playlists_recycler_view"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:padding="8dp"
        android:clipToPadding="false"
        app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
        app:spanCount="2"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:listitem="@layout/song_card" /> <!-- Using song_card, adjust if you create a specific playlist card -->

</androidx.constraintlayout.widget.ConstraintLayout>
```

## File: src/main/res/layout/fragment_song_view.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background_cream"
    android:fitsSystemWindows="true"
    tools:context=".ui.song.SongViewFragment"> <!-- CHANGE CONTEXT LATER -->

    <!-- Top Bar -->
    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/top_bar"
        android:layout_width="0dp"
        android:layout_height="?attr/actionBarSize"
        android:background="@color/accent_orange"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <ImageButton
            android:id="@+id/button_back"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="16dp"
            android:background="?attr/selectableItemBackgroundBorderless"
            android:contentDescription="@string/cd_back"
            android:padding="8dp"
            android:src="@drawable/ic_arrow_back"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:tint="@color/icon_tint_dark" />

        <ImageButton
            android:id="@+id/button_download"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginEnd="16dp"
            android:background="?attr/selectableItemBackgroundBorderless"
            android:contentDescription="@string/cd_download"
            android:padding="8dp"
            android:src="@drawable/ic_download"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:tint="@color/icon_tint_dark"/>

    </androidx.constraintlayout.widget.ConstraintLayout>

    <!-- Main Content Card -->
    <com.google.android.material.card.MaterialCardView
        android:id="@+id/main_content_card"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginStart="16dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="16dp"
        android:layout_marginBottom="8dp"
        app:cardBackgroundColor="@color/white"
        app:cardCornerRadius="20dp"
        app:cardElevation="0dp"
        app:layout_constraintBottom_toTopOf="@+id/bottom_bar"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/top_bar">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="16dp">

            <!-- Album Art -->
            <com.google.android.material.imageview.ShapeableImageView
                android:id="@+id/image_album_art"
                android:layout_width="0dp"
                android:layout_height="0dp"
                android:layout_marginTop="24dp"
                android:scaleType="centerCrop"
                android:src="@drawable/album_art_get_lucky"
                app:layout_constraintDimensionRatio="1:1"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHeight_percent="0.4"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:shapeAppearanceOverlay="@style/ShapeAppearance.App.CircleImageView"
                app:strokeColor="#888888"
                app:strokeWidth="1dp" /> <!-- Optional: Added stroke to mimic slight outline -->


            <!-- Song Title -->
            <TextView
                android:id="@+id/text_song_title"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="24dp"
                android:text="Get Lucky"
                android:textColor="@color/text_primary"
                android:textSize="24sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/image_album_art" />

            <!-- Artist Name -->
            <TextView
                android:id="@+id/text_artist_name"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="4dp"
                android:fontFamily="@font/livvic_regular"
                android:text="Daft Punk"
                android:textColor="@color/text_secondary"
                android:textSize="16sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/text_song_title" />


            <!-- Controls -->
            <LinearLayout
                android:id="@+id/controls_container"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="32dp"
                android:gravity="center_vertical"
                android:orientation="horizontal"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/text_artist_name">

                <ImageButton
                    android:id="@+id/button_previous"
                    android:layout_width="48dp"
                    android:layout_height="48dp"
                    android:background="?attr/selectableItemBackgroundBorderless"
                    android:contentDescription="@string/cd_previous"
                    android:padding="8dp"
                    android:src="@drawable/ic_previous"
                    app:tint="@color/icon_tint_medium" />

                <ImageButton
                    android:id="@+id/button_play_pause"
                    android:layout_width="72dp"
                    android:layout_height="72dp"
                    android:layout_marginStart="24dp"
                    android:layout_marginEnd="24dp"
                    android:background="@drawable/play_pause_background"
                    android:contentDescription="@string/cd_pause"
                    android:elevation="4dp"
                    android:padding="16dp"
                    android:scaleType="fitCenter"
                    android:src="@drawable/ic_pause"
                    app:tint="@color/icon_tint_dark" /> <!-- Dark tint for contrast on orange -->

                <ImageButton
                    android:id="@+id/button_next"
                    android:layout_width="48dp"
                    android:layout_height="48dp"
                    android:background="?attr/selectableItemBackgroundBorderless"
                    android:contentDescription="@string/cd_next"
                    android:padding="8dp"
                    android:src="@drawable/ic_next"
                    app:tint="@color/icon_tint_medium" />

            </LinearLayout>

            <!-- Seek Bar Area -->
            <androidx.constraintlayout.widget.ConstraintLayout
                android:id="@+id/seekbar_container"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginTop="32dp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/controls_container"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintVertical_bias="0.0"
                android:layout_marginBottom="16dp">


                <TextView
                    android:id="@+id/text_current_time"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="3:05"
                    android:textColor="@color/text_secondary"
                    android:textSize="12sp"
                    app:layout_constraintBottom_toBottomOf="@+id/seek_bar"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintTop_toTopOf="@+id/seek_bar" />

                <SeekBar
                    android:id="@+id/seek_bar"
                    style="@style/Widget.App.SeekBar"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_marginStart="8dp"
                    android:layout_marginEnd="8dp"
                    android:max="248"
                    android:progress="185"
                    app:layout_constraintEnd_toStartOf="@+id/text_total_time"
                    app:layout_constraintStart_toEndOf="@+id/text_current_time"
                    app:layout_constraintTop_toTopOf="parent" />

                <TextView
                    android:id="@+id/text_total_time"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="4:08"
                    android:textColor="@color/text_secondary"
                    android:textSize="12sp"
                    app:layout_constraintBottom_toBottomOf="@+id/seek_bar"
                    app:layout_constraintEnd_toEndOf="parent"
                    app:layout_constraintTop_toTopOf="@+id/seek_bar" />

            </androidx.constraintlayout.widget.ConstraintLayout>


        </androidx.constraintlayout.widget.ConstraintLayout>

    </com.google.android.material.card.MaterialCardView>


    <!-- Bottom Bar -->
    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/bottom_bar"
        android:layout_width="0dp"
        android:layout_height="80dp"
        android:background="@drawable/bottom_bar_background"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

        <ImageButton
            android:id="@+id/button_like"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:background="?attr/selectableItemBackgroundBorderless"
            android:contentDescription="@string/cd_like"
            android:padding="16dp"
            android:src="@drawable/ic_heart"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toStartOf="@+id/button_queue"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:tint="@color/icon_tint_medium_dark" />

        <ImageButton
            android:id="@+id/button_queue"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:background="?attr/selectableItemBackgroundBorderless"
            android:contentDescription="@string/cd_queue"
            android:padding="16dp"
            android:src="@drawable/ic_queue"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toStartOf="@+id/button_shuffle"
            app:layout_constraintStart_toEndOf="@+id/button_like"
            app:layout_constraintTop_toTopOf="parent"
            app:tint="@color/icon_tint_medium_dark" />

        <ImageButton
            android:id="@+id/button_shuffle"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:background="?attr/selectableItemBackgroundBorderless"
            android:contentDescription="@string/cd_shuffle"
            android:padding="16dp"
            android:src="@drawable/ic_shuffle"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/button_queue"
            app:layout_constraintTop_toTopOf="parent"
            app:tint="@color/icon_tint_medium_dark" />

        <!-- Small indicator dots (Optional visual flair) -->
        <ImageView
            android:id="@+id/indicator_dots"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_dots_indicator"
            app:layout_constraintStart_toStartOf="@id/button_queue"
            app:layout_constraintEnd_toEndOf="@id/button_queue"
            app:layout_constraintBottom_toBottomOf="parent"
            android:layout_marginBottom="8dp"
            app:tint="@color/icon_tint_medium_dark_faded"
            />

    </androidx.constraintlayout.widget.ConstraintLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

## File: src/main/res/layout/genre_card.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:card_view="http://schemas.android.com/apk/res-auto"
    android:layout_width="120dp"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    card_view:cardCornerRadius="8dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="8dp"
        android:orientation="vertical"
        android:gravity="center">

        <ImageView
            android:id="@+id/genreImage"
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:scaleType="centerCrop"
            android:src="@drawable/ic_genre_placeholder" />

        <!-- Genre title -->
        <TextView
            android:id="@+id/genreName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Genre"
            android:textSize="16sp"
            android:textStyle="bold"
            android:layout_marginTop="8dp" />
    </LinearLayout>
</androidx.cardview.widget.CardView>
```

## File: src/main/res/layout/song_card_long.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="12sp">

    <ImageView
        android:id="@+id/imageView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:srcCompat="@tools:sample/avatars" />

    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_margin="12sp"
        android:orientation="vertical"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toEndOf="@+id/imageView2"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Song Title"
            android:textSize="20sp" />

        <TextView
            android:id="@+id/textView7"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Artist" />
    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

## File: src/main/res/layout/song_card.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="12sp">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toTopOf="@+id/textView"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:srcCompat="@tools:sample/avatars" />

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Song Title"
        android:textSize="20sp"
        app:layout_constraintBottom_toTopOf="@+id/textView5"
        app:layout_constraintStart_toStartOf="parent" />

    <TextView
        android:id="@+id/textView5"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Artist"
        android:textSize="14sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

## File: src/main/res/menu/bottom_navigation_menu.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/navigation_home"
        android:icon="@drawable/ic_home"
        android:title="@string/title_home" />

    <item
        android:id="@+id/navigation_explore"
        android:icon="@drawable/ic_explore"
        android:title="@string/title_explore" />

    <item
        android:id="@+id/navigation_playlists"
        android:icon="@drawable/ic_queue"
    android:title="@string/title_playlists" />

    <item
        android:id="@+id/navigation_library"
        android:icon="@drawable/ic_library_music"
        android:title="@string/title_library" />

</menu>
```

## File: src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

## File: src/main/res/mipmap-anydpi-v26/ic_launcher.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

## File: src/main/res/navigation/mobile_navigation.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/mobile_navigation"
    app:startDestination="@+id/navigation_home">


    <fragment
        android:id="@+id/navigation_home"
        android:name="com.example.soundslike.ui.home.HomeFragment"
        android:label="@string/title_home"
        tools:layout="@layout/fragment_home">
        <action
            android:id="@+id/action_home_to_playlist_detail"
            app:destination="@id/navigation_playlist_detail" />
        <action
            android:id="@+id/action_home_to_song_view"
            app:destination="@id/navigation_song_view" />
    </fragment>

    <fragment
        android:id="@+id/navigation_explore"
        android:name="com.example.soundslike.ui.explore.ExploreFragment"
        android:label="@string/title_explore"
        tools:layout="@layout/fragment_explore" />

    <fragment
        android:id="@+id/navigation_playlists"
        android:name="com.example.soundslike.ui.playlists.PlaylistsFragment"
        android:label="@string/title_playlists"
        tools:layout="@layout/fragment_playlists">
        <action
            android:id="@+id/action_playlists_to_playlist_detail"
            app:destination="@id/navigation_playlist_detail" />
    </fragment>

    <fragment
        android:id="@+id/navigation_library"
        android:name="com.example.soundslike.ui.library.LibraryFragment"
        android:label="@string/title_library"
        tools:layout="@layout/fragment_library" />


    <!-- Detail Screens -->
    <fragment
        android:id="@+id/navigation_playlist_detail"
        android:name="com.example.soundslike.ui.playlistdetail.PlaylistDetailFragment"
        android:label="Playlist"
        tools:layout="@layout/fragment_playlist_detail">
        <!-- Argument to receive playlist ID -->
        <argument
            android:name="playlistId"
            android:defaultValue="default_playlist_id"
            app:argType="string" />

        <action
            android:id="@+id/action_playlist_detail_to_song_view"
            app:destination="@id/navigation_song_view" />
    </fragment>

    <fragment
        android:id="@+id/navigation_song_view"
        android:name="com.example.soundslike.ui.song.SongViewFragment"
        android:label="Now Playing"
        tools:layout="@layout/fragment_song_view">
        <!-- Argument to receive song ID -->
        <argument
            android:name="songId"
            android:defaultValue="default_song_id"
            app:argType="string" />
    </fragment>

</navigation>
```

## File: src/main/res/values/colors.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="accent_orange">#FFCC80</color>
    <color name="background_cream">#FFF8E1</color>
    <color name="text_primary">#212121</color>
    <color name="text_secondary">#757575</color>
    <color name="icon_tint_dark">#424242</color>
    <color name="icon_tint_medium">#8D8D8D</color>
    <color name="icon_tint_medium_dark">#616161</color>
    <color name="icon_tint_medium_dark_faded">#A0616161</color>
    <color name="seekbar_track">#E0E0E0</color>
    <color name="seekbar_progress">#616161</color>
    <color name="seekbar_thumb">#424242</color>
</resources>
```

## File: src/main/res/values/strings.xml
```xml
<resources>
    <string name="app_name">SoundsLike</string>
    <string name="cd_back">Back</string>
    <string name="cd_download">Download</string>
    <string name="cd_album_art">Album Art</string>
    <string name="cd_previous">Previous track</string>
    <string name="cd_play">Play</string>
    <string name="cd_pause">Pause</string>
    <string name="cd_next">Next track</string>
    <string name="cd_like">Like song</string>
    <string name="cd_queue">Show queue</string>
    <string name="cd_shuffle">Shuffle</string>

    <string name="title_home">Home</string>
    <string name="title_explore">Explore</string>
    <string name="title_playlists">Playlists</string>
    <string name="title_library">Library</string>

    <string name="home_fragment_label">Home Fragment</string>
    <string name="explore_fragment_label">Explore Fragment</string>
    <string name="playlists_fragment_label">Playlists Fragment</string>
    <string name="library_fragment_label">Library Fragment</string>
</resources>
```

## File: src/main/res/values/themes.xml
```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.SoundsLike" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your light theme here. -->
        <!-- <item name="colorPrimary">@color/my_light_primary</item> -->

        <item name="fontFamily">@font/livvic_regular</item>

        <item name="android:windowLightStatusBar" tools:targetApi="m">true</item>
        <item name="android:statusBarColor">@color/accent_orange</item>
    </style>

    <style name="Theme.SoundsLike" parent="Base.Theme.SoundsLike" />

    <style name="ShapeAppearance.App.CircleImageView" parent="">
        <item name="cornerFamily">rounded</item>
        <item name="cornerSize">50%</item>
    </style>

    <style name="Widget.App.SeekBar" parent="Widget.AppCompat.SeekBar">
        <item name="android:progressBackgroundTint">@color/seekbar_track</item>
        <item name="android:progressTint">@color/seekbar_progress</item>
        <item name="android:thumbTint">@color/seekbar_thumb</item>
    </style>
</resources>
```

## File: src/main/res/values-night/themes.xml
```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.SoundsLike" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your dark theme here. -->
        <!-- <item name="colorPrimary">@color/my_dark_primary</item> -->
    </style>
</resources>
```

## File: src/main/res/xml/backup_rules.xml
```xml
<?xml version="1.0" encoding="utf-8"?><!--
   Sample backup rules file; uncomment and customize as necessary.
   See https://developer.android.com/guide/topics/data/autobackup
   for details.
   Note: This file is ignored for devices older that API 31
   See https://developer.android.com/about/versions/12/backup-restore
-->
<full-backup-content>
    <!--
   <include domain="sharedpref" path="."/>
   <exclude domain="sharedpref" path="device.xml"/>
-->
</full-backup-content>
```

## File: src/main/res/xml/data_extraction_rules.xml
```xml
<?xml version="1.0" encoding="utf-8"?><!--
   Sample data extraction rules file; uncomment and customize as necessary.
   See https://developer.android.com/about/versions/12/backup-restore#xml-changes
   for details.
-->
<data-extraction-rules>
    <cloud-backup>
        <!-- TODO: Use <include> and <exclude> to control what is backed up.
        <include .../>
        <exclude .../>
        -->
    </cloud-backup>
    <!--
    <device-transfer>
        <include .../>
        <exclude .../>
    </device-transfer>
    -->
</data-extraction-rules>
```

## File: src/main/AndroidManifest.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.SoundsLike"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

## File: src/test/java/com/example/soundslike/ExampleUnitTest.java
```java
package com.example.soundslike;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}
```

## File: .gitignore
```
/build
```

## File: build.gradle.kts
```
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.soundslike"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.soundslike"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3") 
    implementation("androidx.fragment:fragment-ktx:1.8.1")
}
```

## File: proguard-rules.pro
```
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
```
