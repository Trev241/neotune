package com.example.soundslike.data.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.soundslike.data.models.Playlist; // Import Playlist
import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.data.network.ApiService;
import com.example.soundslike.data.network.RetrofitClient;
import com.example.soundslike.data.network.requests.AddSongRequest;
import com.example.soundslike.data.network.requests.PlaylistCreateRequest;

import java.util.Collections; // Import Collections
import java.util.List; // Import List

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistRepository {

    private static final String TAG = "PlaylistRepository";
    private final ApiService apiService;

    public PlaylistRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // --- Method to get Playlist Details (existing) ---
    public LiveData<PlaylistDetail> getPlaylistDetails(String playlistId) {
        MutableLiveData<PlaylistDetail> data = new MutableLiveData<>();
        Log.d(TAG, "Fetching details for playlist ID: " + playlistId);
        apiService.getPlaylistDetails(playlistId).enqueue(new Callback<PlaylistDetail>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistDetail> call, @NonNull Response<PlaylistDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Successfully fetched details for playlist: " + playlistId);
                    data.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch playlist details: " + response.code() + " - " + response.message());
                    data.setValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<PlaylistDetail> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching playlist details for " + playlistId, t);
                data.setValue(null);
            }
        });
        return data;
    }

    // --- NEW: Get User Playlists ---
    public LiveData<List<Playlist>> getUserPlaylists(int skip, int limit) {
        MutableLiveData<List<Playlist>> data = new MutableLiveData<>();
        // Add "Bearer " prefix to the token
        Log.d(TAG, "Fetching user playlists...");

        apiService.getUserPlaylists(skip, limit).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Successfully fetched " + response.body().size() + " user playlists.");
                    data.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch user playlists: " + response.code() + " - " + response.message());
                    data.setValue(Collections.emptyList()); // Return empty list on failure
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching user playlists", t);
                data.setValue(Collections.emptyList()); // Return empty list on error
            }
        });
        return data;
    }

    // --- NEW: Create Playlist ---
    // Returns LiveData containing the newly created Playlist on success, null on failure
    public LiveData<Playlist> createPlaylist(String playlistName) {
        MutableLiveData<Playlist> result = new MutableLiveData<>();
        PlaylistCreateRequest requestBody = new PlaylistCreateRequest(playlistName);
        Log.d(TAG, "Attempting to create playlist: " + playlistName);

        apiService.createPlaylist(requestBody).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "Successfully created playlist: " + response.body().getName());
                    result.setValue(response.body()); // Post the created playlist
                } else {
                    Log.e(TAG, "Failed to create playlist: " + response.code() + " - " + response.message());
                    result.setValue(null); // Signal failure
                }
            }
            @Override
            public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable t) {
                Log.e(TAG, "Error creating playlist", t);
                result.setValue(null); // Signal failure
            }
        });
        return result;
    }

    // --- NEW: Add Song to Playlist ---
    // Returns LiveData<Boolean> indicating success (true) or failure (false)
    public LiveData<Boolean> addSongToPlaylist(String playlistId, String songIdToAdd, @Nullable Integer order) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        AddSongRequest requestBody = new AddSongRequest(songIdToAdd, order);
        Log.d(TAG, "Attempting to add song " + songIdToAdd + " to playlist " + playlistId);

        apiService.addSongToPlaylist(playlistId, requestBody).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                // Check for 2xx status codes (like 204 No Content or 200 OK)
                if (response.isSuccessful()) {
                    Log.i(TAG, "Successfully added song " + songIdToAdd + " to playlist " + playlistId);
                    success.setValue(true);
                } else {
                    Log.e(TAG, "Failed to add song to playlist: " + response.code() + " - " + response.message());
                    success.setValue(false);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e(TAG, "Error adding song to playlist", t);
                success.setValue(false);
            }
        });
        return success;
    }
}