package com.example.musicapp.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicapp.databinding.FragmentMusicBinding;
import com.google.android.material.slider.Slider;

import java.io.IOException;

public class MusicFragment extends Fragment {
    FragmentMusicBinding binding;
    Slider slider;
    Handler handler=new Handler();
    ToggleButton playBtn;
    Button download;
    TextView title;
    ToggleButton repeat;
    MediaPlayer mediaPlayer;
    String audioUrl, name;
    float progress=0;
    int duration;
    boolean playing=false, isRepeating=false, isDownloadAllowed=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMusicBinding.inflate(inflater,container, false);
        /*mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
        }catch (IOException e){
            e.printStackTrace();
        }*/

        slider=binding.slider;
        playBtn=binding.play;
        playBtn.setText(null);
        download=binding.dowload;
        title=binding.title;
        repeat=binding.repeat;
        repeat.setText(null);
        slider.setValue(0);
        slider.setValueTo(60);
        slider.setStepSize(0.1f);


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("Playing");
                if(playing){
                    handler.removeCallbacks(progressUpdater);
                    playing=false;
                    title.setText("Paused");
                }
                else {
                    //playAudio();
                    handler.postDelayed(progressUpdater, 100);
                    playing=true;
                    title.setText("Playing");
                }
            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isActivated()) {
                    isRepeating = false;
                    v.setActivated(false);
                }
                else {
                    isRepeating = true;
                    v.setActivated(true);
                }

            }
        });
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if(fromUser){
                    progress=value;
                }
            }
        });
        return binding.getRoot();
    }
    private Runnable progressUpdater=new Runnable() {
        @Override
        public void run() {
            progress+=1f;
            if(progress<=slider.getValueTo()){
                Log.d("progress"," "+slider.getValue());
                slider.setValue(progress);
                handler.postDelayed(this,100);
            }
            else if(isRepeating) {
                progress = 0;
                slider.setValue(progress);
                handler.postDelayed(this,100);
            }
            else {
                handler.removeCallbacks(this);
                title.setText("Playing");
            }
            Log.d("progress","progress: "+slider.getValue());

        }
    };
    private void playAudio(){

        try {
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void pauseAudio(){
        mediaPlayer.pause();
    }
}
