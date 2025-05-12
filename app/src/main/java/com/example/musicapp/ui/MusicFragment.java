package com.example.musicapp.ui;

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

public class MusicFragment extends Fragment {
    FragmentMusicBinding binding;
    Slider slider;
    Handler handler=new Handler();
    ToggleButton playBtn;
    Button download;
    TextView title;
    ToggleButton repeat;
    float progress=0;
    boolean playing=false, isRepeating=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMusicBinding.inflate(inflater,container, false);

        slider=binding.slider;
        playBtn=binding.play;
        playBtn.setText(null);
        download=binding.dowload;
        title=binding.title;
        repeat=binding.repeat;
        repeat.setText(null);
        slider.setValue(0);
        slider.setValueTo(5);
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
            if(progress<=100){
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
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name");
            String artist = args.getString("artist_name");
            float duration = args.getFloat("duration", 1f);
            String audio = args.getString("audio");
            String albumImage = args.getString("album_image");
            String downloadUrl = args.getString("audiodownload");

            title.setText(name + " - " + artist);

            if (duration > 0f) {
                slider.setEnabled(true);
                slider.setValueFrom(0f);
                slider.setValueTo(duration);
                slider.setValue(0f);
            } else {
                slider.setEnabled(false);
                Log.w("MusicFragment", "Duration error: " + duration);
            }

            //TODO lejátszás
        }
    }
}
