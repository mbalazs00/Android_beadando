package com.example.musicapp.ui.songlist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.data.MusicResult;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private List<MusicResult> songs;
    private OnSongPlayClickListener listener;

    public SongListAdapter(List<MusicResult> songs, OnSongPlayClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_row, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        MusicResult song = songs.get(position);
        holder.songTextView.setText(song.name);
        holder.artistTextView.setText(song.artist_name);

        holder.playButton.setOnClickListener(v ->{
            if(listener != null){
                listener.onSongPlayClicked(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTextView, artistTextView;
        Button playButton;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(R.id.songTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            playButton = itemView.findViewById(R.id.songPlayButton);
        }
    }

    public interface OnSongPlayClickListener{
        void onSongPlayClicked(MusicResult song);
    }
}

