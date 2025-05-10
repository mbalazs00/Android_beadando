package com.example.musicapp;

import com.example.musicapp.data.MusicResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicClient {
    //base url https://api.jamendo.com/v3.0/tracks/
    @GET("tracks/")
    Call<MusicResponse> getResponse(@Query("client_id") String key,@Query("search") String title);
}
