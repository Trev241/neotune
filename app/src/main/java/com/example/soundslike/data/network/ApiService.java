package com.example.soundslike.data.network;

import com.example.soundslike.data.models.Song;

import java.util.List;

import retrofit2.Call; // Use Call for standard Java/Retrofit
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
// import retrofit2.http.Path; // For path parameters later

public interface ApiService {

    String BASE_URL = "http://10.0.2.2:8001/"; // Your API base URL

    @GET("songs")
    Call<List<Song>> getSongs( // Return Call<List<Song>>
                               @Query("skip") int skip,
                               @Query("limit") int limit
    );

    // Add other endpoints later
    // @GET("artists/{artist_id}")
    // Call<Artist> getArtist(@Path("artist_id") String artistId);
}
