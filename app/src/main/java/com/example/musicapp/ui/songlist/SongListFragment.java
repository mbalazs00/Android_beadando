package com.example.musicapp.ui.songlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.MusicClient;
import com.example.musicapp.R;

public class SongListFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        RecyclerView songRecyclerView = view.findViewById(R.id.songRecyclerView);
        //TODO FINISH IT
        return view;
    }

}
