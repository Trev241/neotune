package com.example.soundslike.data.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.network.ApiService;
import com.example.soundslike.data.network.RetrofitClient;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRepository {

    private static final String TAG = "SongRepository";
    private final ApiService apiService;

    public SongRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Get Songs (existing)
    public LiveData<List<Song>> getSongs(int skip, int limit) {
        MutableLiveData<List<Song>> data = new MutableLiveData<>();
        apiService.getSongs(skip, limit).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Fetched " + response.body().size() + " songs successfully.");
                    data.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch songs: " + response.code() + " - " + response.message());
                    data.setValue(Collections.emptyList());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching songs", t);
                data.setValue(Collections.emptyList());
            }
        });
        return data;
    }

    // --- NEW: Search Songs ---
    public LiveData<List<Song>> searchSongs(String query, int skip, int limit) {
        MutableLiveData<List<Song>> data = new MutableLiveData<>();
        Log.d(TAG, "Searching songs with query: '" + query + "'");

        apiService.searchSongs(query, skip, limit).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Search successful, found " + response.body().size() + " songs.");
                    data.setValue(response.body());
                } else {
                    // Handle cases like 404 Not Found if the API returns that for no results
                    Log.e(TAG, "Search failed: " + response.code() + " - " + response.message());
                    data.setValue(Collections.emptyList()); // Return empty list on failure/no results
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error during song search", t);
                data.setValue(Collections.emptyList()); // Return empty list on error
            }
        });
        return data;
    }
    // -------------------------
}