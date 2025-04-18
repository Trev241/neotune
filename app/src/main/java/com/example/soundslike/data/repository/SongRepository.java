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

    public LiveData<List<Song>> getSongs(int skip, int limit) {
        MutableLiveData<List<Song>> data = new MutableLiveData<>();

        apiService.getSongs(skip, limit).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Fetched " + response.body().size() + " songs successfully.");
                    // TODO: Fetch artist names here if needed and update Song objects before posting
                    // For now, post the list directly
                    data.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch songs: " + response.code() + " - " + response.message());
                    data.setValue(Collections.emptyList()); // Post empty list on failure
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching songs", t);
                data.setValue(Collections.emptyList()); // Post empty list on error
            }
        });

        return data;
    }

    // Add methods to fetch artist details later if needed
    // public LiveData<Artist> getArtist(String artistId) { ... }
    // public void fetchArtistNameAndUpdateSong(Song song, MutableLiveData<Song> updatedSongData) { ... }
}