package com.example.soundslike.data.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.soundslike.data.models.Artist;
import com.example.soundslike.data.network.ApiService;
import com.example.soundslike.data.network.RetrofitClient;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRepository {

    private static final String TAG = "ArtistRepository";
    private final ApiService apiService;

    public ArtistRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Get List of Artists (Existing)
    public LiveData<List<Artist>> getArtists(int skip, int limit) {
        MutableLiveData<List<Artist>> data = new MutableLiveData<>();
        Log.d(TAG, "Fetching artists with skip=" + skip + ", limit=" + limit);
        apiService.getArtists(skip, limit).enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Artist>> call, @NonNull Response<List<Artist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Fetched " + response.body().size() + " artists successfully.");
                    data.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch artists: " + response.code() + " - " + response.message());
                    data.setValue(Collections.emptyList());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Artist>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching artists", t);
                data.setValue(Collections.emptyList());
            }
        });
        return data;
    }

    // --- ADDED: Get Artist Details ---
    public LiveData<Artist> getArtistDetails(String artistId) {
        MutableLiveData<Artist> data = new MutableLiveData<>();
        Log.d(TAG, "Fetching details for artist ID: " + artistId);
        apiService.getArtistDetails(artistId).enqueue(new Callback<Artist>() {
            @Override
            public void onResponse(@NonNull Call<Artist> call, @NonNull Response<Artist> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Successfully fetched details for artist: " + response.body().getName());
                    data.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch artist details: " + response.code() + " - " + response.message());
                    data.setValue(null); // Signal failure
                }
            }
            @Override
            public void onFailure(@NonNull Call<Artist> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching artist details for " + artistId, t);
                data.setValue(null); // Signal failure
            }
        });
        return data;
    }
    // ---------------------------------
}