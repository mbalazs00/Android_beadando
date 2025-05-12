package com.example.musicapp.ui.songlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.MusicClient;
import com.example.musicapp.R;
import com.example.musicapp.data.MusicResponse;
import com.example.musicapp.data.MusicResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SongListFragment extends Fragment {

    private RecyclerView songRecyclerView;
    private SongListAdapter adapter;
    private List<MusicResult> songList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        songRecyclerView = view.findViewById(R.id.songRecyclerView);
        songRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SongListAdapter(songList);
        songRecyclerView.setAdapter(adapter);

        EditText searchEditText = view.findViewById(R.id.searchEditText);
        Button searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if(!query.isEmpty()){
                searchSongs(query);
            }
        });
        return view;
    }
    private void searchSongs(String title){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.jamendo.com/v3.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MusicClient client = retrofit.create(MusicClient.class);
        Call<MusicResponse> call = client.getResponse("1ba61b99", title);

        call.enqueue(new Callback<MusicResponse>() {
            @Override
            public void onResponse(Call<MusicResponse> call, Response<MusicResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    songList.clear();
                    songList.addAll(response.body().results);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MusicResponse> call, Throwable t) {
                Log.e("API_ERROR", "Hiba: " + t.getMessage());
            }
        });
    }

}
