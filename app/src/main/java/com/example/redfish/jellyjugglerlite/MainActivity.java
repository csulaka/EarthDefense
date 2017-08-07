package com.example.redfish.jellyjugglerlite;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPreferences;
    private ImageButton playButton;
    private ImageButton scoreButton;
    private ImageButton exitButton;
    private ImageButton optionsButton;
    private FragmentManager fm;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("JellyJuggler",MODE_PRIVATE);

        BackgroundMusic.musicPlaying(this);

        if(sharedPreferences.getBoolean("musicEnable",true))
            BackgroundMusic.unmuteMusic();
        else
            BackgroundMusic.muteMusic();
        playButton = (ImageButton) findViewById(R.id.playButton);
        scoreButton = (ImageButton) findViewById(R.id.scoreButton);
        exitButton = (ImageButton) findViewById(R.id.exitButton);
        optionsButton = (ImageButton) findViewById(R.id.optionsButton);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if(sharedPreferences.getBoolean("adsEnable",true))
            mAdView.setVisibility(View.VISIBLE);
        else
            mAdView.setVisibility(View.INVISIBLE);
        optionsButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        scoreButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==playButton){

            startActivity(new Intent(MainActivity.this,GameActivity.class));
            finish();
        }
        if(view==scoreButton){
            FragmentManager fm = getFragmentManager();
            HighScoreFragment dialogFragment = new HighScoreFragment ();
            dialogFragment.show(fm, "Sample Fragment");
        }
        if(view==optionsButton){
            FragmentManager fm = getFragmentManager();
            OptionsFragment dialogFragment = new OptionsFragment ();
            dialogFragment.show(fm, "Sample Fragment");

        }
        if(view==exitButton){
            finish();
            BackgroundMusic.stopMusic();
            System.exit(0);
        }
    }
    @Override
    public void onBackPressed() {
        BackgroundMusic.stopMusic();
        MainActivity.this.finish();
    }
}

