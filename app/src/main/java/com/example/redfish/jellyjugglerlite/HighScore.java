package com.example.redfish.jellyjugglerlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Redfish on 8/5/2017.
 */

public class HighScore extends AppCompatActivity{

    TextView hiScore1,hiScore2,hiScore3,hiScore4;
    ImageButton backButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

//        backButton=(ImageButton) findViewById(R.id.backButton);
        hiScore1 = (TextView) findViewById(R.id.textView);
        hiScore2 = (TextView) findViewById(R.id.textView2);
        hiScore3 = (TextView) findViewById(R.id.textView3);
        hiScore4 = (TextView) findViewById(R.id.textView4);

        sharedPreferences  = getSharedPreferences(GameView.PREFS_NAME, Context.MODE_PRIVATE);


        hiScore1.setText("1.\t\t\t"+sharedPreferences.getInt("hiscore1",0));
        hiScore2.setText("2.\t\t\t"+sharedPreferences.getInt("hiscore2",0));
        hiScore3.setText("3.\t\t\t"+sharedPreferences.getInt("hiscore3",0));
        hiScore4.setText("4.\t\t\t"+sharedPreferences.getInt("hiscore4",0));
    }

}