package com.example.ayaelbeltagye.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity  implements DetailsInterface{
    boolean isTablet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FrameLayout f= (FrameLayout) findViewById(R.id.frame);
        if(null==f){
            isTablet=false;
        }else{
            isTablet=true;
        }}
    @Override
    public void openDetails(Movie movie) {
        if (isTablet) {
            DetailsActivityFragment movieDetailsFragment = new DetailsActivityFragment();
            Bundle b = new Bundle();
            b.putSerializable("movie", movie);
            movieDetailsFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, movieDetailsFragment).commit();
        } else {
            Bundle b = new Bundle();
            b.putSerializable("movie", movie);
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtras(b);
            startActivity(intent);

        }
    }

}
