package com.example.musicapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {
    Slider slider;
    Handler handler=new Handler();
    Button playBtn, download;
    TextView title;
    ToggleButton repeat;
    float progress=0;
    boolean playing=false, isRepeating=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.fragment_music);
        slider=findViewById(R.id.slider);
        playBtn=findViewById(R.id.play);
        title=findViewById(R.id.title);
        repeat=findViewById(R.id.repeat);
        download=findViewById(R.id.dowload);

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
                    handler.postDelayed(progressUpdater, 1000);
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
        slider.setValue(0);
        slider.setValueTo(5);
        slider.setStepSize(0.1f);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if(fromUser){
                    progress=value;
                }
            }
        });
    }
    private Runnable progressUpdater=new Runnable() {
        @Override
        public void run() {
            progress+=1f;
            if(progress<=100){
                slider.setValue(progress);
                handler.postDelayed(this,1000);
            }
            else if(isRepeating) {
                progress = 0;
                slider.setValue(progress);
                handler.postDelayed(this,1000);
            }
            else {
                handler.removeCallbacks(this);
                title.setText("Playing");
            }
            Log.d("progress","progress: "+slider.getValue());

        }
    };
}