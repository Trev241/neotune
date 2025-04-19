package com.example.soundslike.data.network;

import com.example.soundslike.data.models.Artist; // Import Artist model
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.network.requests.AddSongRequest;
import com.example.soundslike.data.network.requests.PlaylistCreateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://10.0.2.2:8001/"; // Ensure this is correct

    // --- Song Endpoints ---
    @GET("songs")
    Call<List<Song>> getSongs(
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @GET("songs/search")
    Call<List<Song>> searchSongs(
            @Query("query") String searchQuery,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    // --- Playlist Endpoints ---
    @GET("playlists/{playlist_id}")
    Call<PlaylistDetail> getPlaylistDetails(
            @Path("playlist_id") String playlistId
    );

    @GET("playlists")
    Call<List<Playlist>> getUserPlaylists(
            @Header("Authorization") String token,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @POST("playlists")
    Call<Playlist> createPlaylist(
            @Header("Authorization") String token,
            @Body PlaylistCreateRequest playlistData
    );

    @POST("playlists/{playlist_id}/songs")
    Call<Void> addSongToPlaylist(
            @Header("Authorization") String token,
            @Path("playlist_id") String playlistId,
            @Body AddSongRequest songData
    );

    // --- Artist Endpoints ---
    @GET("artists")
    Call<List<Artist>> getArtists(
            @Query("skip") int skip,
            @Query("limit") int limit
            // Add @Header("Authorization") String token if needed
    );

    // --- ADDED: Get Artist by ID ---
    @GET("artists/{artist_id}")
    Call<Artist> getArtistDetails(
            @Path("artist_id") String artistId
            // Add @Header("Authorization") String token if needed
    );
    // -----------------------------

}