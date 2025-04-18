package com.example.soundslike.data.network;

import com.example.soundslike.data.models.Playlist; // Import base Playlist for response/body
import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.network.requests.AddSongRequest; // Create this request body class
import com.example.soundslike.data.network.requests.PlaylistCreateRequest; // Create this request body class

import java.util.List;

import retrofit2.Call;
import retrofit2.Response; // Import Response for POST calls without specific body
import retrofit2.http.Body; // Import Body
import retrofit2.http.GET;
import retrofit2.http.Header; // Import Header
import retrofit2.http.POST; // Import POST
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://10.0.2.2:8001/";

    @GET("songs")
    Call<List<Song>> getSongs(
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @GET("playlists/{playlist_id}")
    Call<PlaylistDetail> getPlaylistDetails(
            @Path("playlist_id") String playlistId
    );

    // --- NEW: Get User Playlists (Requires Auth) ---
    @GET("playlists")
    Call<List<Playlist>> getUserPlaylists(
            @Header("Authorization") String token, // Pass token in header
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    // --- NEW: Create Playlist (Requires Auth) ---
    @POST("playlists")
    Call<Playlist> createPlaylist( // API returns the created playlist
                                   @Header("Authorization") String token,
                                   @Body PlaylistCreateRequest playlistData // Send name in body
    );

    // --- NEW: Add Song to Playlist (Requires Auth) ---
    @POST("playlists/{playlist_id}/songs")
    Call<Void> addSongToPlaylist( // Or Call<Response<Void>> if you need status code
                                  @Header("Authorization") String token,
                                  @Path("playlist_id") String playlistId,
                                  @Body AddSongRequest songData // Send song_id (and optional order) in body
    );

    // TODO: Add endpoints for removing/reordering songs later
}