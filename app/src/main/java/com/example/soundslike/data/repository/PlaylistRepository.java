package com.example.soundslike.data.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.data.network.ApiService;
import com.example.soundslike.data.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistRepository {

    private static final String TAG = "PlaylistRepository";
    private final ApiService apiService;

    public PlaylistRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public LiveData<PlaylistDetail> getPlaylistDetails(String playlistId) {
        MutableLiveData<PlaylistDetail> data = new MutableLiveData<>();
        Log.d(TAG, "Fetching details for playlist ID: " + playlistId); // Add log

        apiService.getPlaylistDetails(playlistId).enqueue(new Callback<PlaylistDetail>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistDetail> call, @NonNull Response<PlaylistDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Successfully fetched details for playlist: " + playlistId);
                    // TODO: Fetch artist names for songs within the playlist if needed before setting value
                    data.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch playlist details: " + response.code() + " - " + response.message());
                    // You might want to inspect response.errorBody() here
                    data.setValue(null); // Post null on failure
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistDetail> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching playlist details for " + playlistId, t);
                data.setValue(null); // Post null on error
            }
        });

        return data;
    }

    // Add methods for getUserPlaylists, create/update/delete, add/remove/reorder songs later
}