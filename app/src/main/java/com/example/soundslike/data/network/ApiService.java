package com.example.soundslike.data.network;

import com.example.soundslike.data.models.Artist;
import com.example.soundslike.data.models.Playlist;
import com.example.soundslike.data.models.PlaylistDetail;
import com.example.soundslike.data.models.Song;
import com.example.soundslike.data.network.requests.AddSongRequest;
import com.example.soundslike.data.network.requests.PlaylistCreateRequest;
import com.example.soundslike.data.network.requests.RegisterRequest;
import com.example.soundslike.data.network.responses.LoginResponse;
import com.example.soundslike.data.network.responses.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response; // Keep Response import if needed for Void calls status
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
// import retrofit2.http.Header; // REMOVE Header import if no longer used anywhere else
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://10.0.2.2:8001/"; // Ensure this is correct

    // --- Auth Endpoints ---
    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginResponse> loginUser(
            @Field("username") String username,
            @Field("password") String password
            // Removed optional fields for simplicity, add back if needed:
            // @Field("grant_type") String grantType,
            // @Field("scope") String scope,
            // @Field("client_id") String clientId,
            // @Field("client_secret") String clientSecret
    );

    @POST("auth/register")
    Call<RegisterResponse> registerUser(
            @Body RegisterRequest registerRequest // Send email/password in JSON body
    );

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
    // Assuming get details might need auth later, but interceptor handles it
    @GET("playlists/{playlist_id}")
    Call<PlaylistDetail> getPlaylistDetails(
            @Path("playlist_id") String playlistId
    );

    // Interceptor adds token automatically
    @GET("playlists")
    Call<List<Playlist>> getUserPlaylists(
            // @Header("Authorization") String token, // REMOVED - Interceptor handles this
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    // Interceptor adds token automatically
    @POST("playlists")
    Call<Playlist> createPlaylist(
            // @Header("Authorization") String token, // REMOVED - Interceptor handles this
            @Body PlaylistCreateRequest playlistData
    );

    // Interceptor adds token automatically
    @POST("playlists/{playlist_id}/songs")
    Call<Void> addSongToPlaylist(
            // @Header("Authorization") String token, // REMOVED - Interceptor handles this
            @Path("playlist_id") String playlistId,
            @Body AddSongRequest songData
    );

    // --- Artist Endpoints ---
    // Assuming these don't need auth based on interceptor logic
    @GET("artists")
    Call<List<Artist>> getArtists(
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    // Assuming this doesn't need auth based on interceptor logic
    @GET("artists/{artist_id}")
    Call<Artist> getArtistDetails(
            @Path("artist_id") String artistId
    );

}