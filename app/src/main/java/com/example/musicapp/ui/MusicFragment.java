package com.example.musicapp.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.musicapp.databinding.FragmentMusicBinding;
import com.google.android.material.slider.Slider;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MusicFragment extends Fragment {
    FragmentMusicBinding binding;
    Slider slider;
    Handler handler=new Handler();
    ToggleButton playBtn;
    Button download;
    TextView title, currTime, endTime;
    ToggleButton repeat;
    ImageView album;
    String name, artist, audio, downloadUrl, albumImage;
    MediaPlayer mediaPlayer;
    DownloadManager.Request request;
    DownloadManager manager;
    float progress=0, duration;
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
        endTime=binding.endTime;
        currTime=binding.currTime;
        album=binding.icon;
        slider.setStepSize(1f);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playing){
                    progress=0;
                    handler.postDelayed(progressUpdater, 1000);
                    playing=true;
                    mediaPlayer.start();
                }
                else {
                    handler.removeCallbacks(progressUpdater);
                    playing=false;
                    mediaPlayer.pause();
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
                    Log.d("helyek","progress: "+progress+" actual: "+mediaPlayer.getCurrentPosition());
                    progress=value;
                    Log.d("progress",Math.round(progress)+"");
                    mediaPlayer.seekTo(Math.round(progress)*1000);
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .followRedirects(false)
                        .build();

                Request redirectRequest = new Request.Builder()
                        .url(downloadUrl)
                        .build();

                client.newCall(redirectRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isRedirect() || response.code() == 301 || response.code() == 302) {
                            String realUrl = response.header("Location");

                            DownloadManager.Request realRequest = new DownloadManager.Request(Uri.parse(realUrl));
                            realRequest.setTitle("Downloading: " + name + " - " + artist);
                            realRequest.setDescription("Downloading");
                            realRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            realRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".mp3");
                            realRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                            DownloadManager realManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            realManager.enqueue(realRequest);
                        } else if (response.isSuccessful()) {
                            DownloadManager.Request fallbackRequest = new DownloadManager.Request(Uri.parse(downloadUrl));
                            fallbackRequest.setTitle("Downloading: " + name + " - " + artist);
                            fallbackRequest.setDescription("Downloading");
                            fallbackRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            fallbackRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".mp3");
                            fallbackRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                            DownloadManager fallbackManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            fallbackManager.enqueue(fallbackRequest);
                        } else {
                            Log.e("Download", "Failed to download: " + response.code());
                        }
                    }
                });
            }
        });
        return binding.getRoot();
    }
    private Runnable progressUpdater=new Runnable() {
        @Override
        public void run() {
            progress+=1f;
            if(progress<=duration){
                slider.setValue(progress);
                handler.postDelayed(this,1000);
                currTime.setText(formatTime(progress));
            }
            else if(isRepeating) {
                progress = 0;
                slider.setValue(progress);
                handler.postDelayed(this,1000);
                currTime.setText(formatTime(0f));
                if(!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
            else {
                handler.removeCallbacks(this);
            }

        }
    };
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            name = args.getString("name");
            artist = args.getString("artist_name");
            duration = args.getFloat("duration");
            audio = args.getString("audio");
            albumImage = args.getString("album_image");
            downloadUrl = args.getString("audiodownload");

            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(name);
            title.setText(name + " - " + artist);
            Glide.with(getContext()).load(albumImage).into(album);

            endTime.setText(formatTime(duration));

            request=new DownloadManager.Request(Uri.parse(downloadUrl));
            request.setTitle("Downloading: "+name+" - "+artist);
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name+".mp3");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            manager=(DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);


            if (duration > 0f) {
                slider.setEnabled(true);
                slider.setValueFrom(0f);
                Log.d("slider",duration+"");
                slider.setValueTo(duration);
                slider.setValue(0f);
            } else {
                slider.setEnabled(false);
                Log.w("MusicFragment", "Duration error: " + duration);
            }
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if(audio!=null){
                try {
                    mediaPlayer.setDataSource(audio);
                    mediaPlayer.prepare();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            //TODO lejátszás
        }
    }
    public String formatTime(float duration){
        int total=(int) duration;
        int min=total/60;
        int sec=total%60;
        return String.format("%d:%02d",min, sec);
    }
}
