package com.example.musicapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    NavHostFragment navHostFragment;
    AppBarConfiguration appBarConfiguration;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        navHostFragment=(NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController=navHostFragment.getNavController();

        appBarConfiguration=new AppBarConfiguration.Builder(R.id.SongListFragment).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


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