package com.example.musicapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicapp.data.MusicResponse;
import com.example.musicapp.data.MusicResult;
import com.google.android.material.slider.Slider;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    NavHostFragment navHostFragment;
    AppBarConfiguration appBarConfiguration;
    Button url;
    Toolbar toolbar;
    MediaPlayer player;
    String audioUrl;
    boolean playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);


        setContentView(R.layout.activity_main);


        toolbar=findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        navHostFragment=(NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController=navHostFragment.getNavController();

        appBarConfiguration=new AppBarConfiguration.Builder(R.id.homeFragment,R.id.musicFragment).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


    }
    @Override
    protected void onStart(){
        super.onStart();
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://api.jamendo.com/v3.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MusicClient client=retrofit.create(MusicClient.class);

        Call<MusicResponse> call=client.getResponse("1ba61b99","the");
        call.enqueue(new Callback<MusicResponse>() {
            @Override
            public void onResponse(Call<MusicResponse> call, Response<MusicResponse> response) {
                if(!response.isSuccessful()) {
                    Log.w("MainActivity", "response code: " + response.code());
                }
                Log.d("response","success");
                MusicResponse data=response.body();

                audioUrl=data.results.get(0).audio;

            }

            @Override
            public void onFailure(Call<MusicResponse> call, Throwable t) {
                Log.e("response","failure");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

}