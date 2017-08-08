package com.example.redfish.jellyjugglerlite;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Redfish on 8/5/2017.
 */

public class HighScoreFragment extends DialogFragment {

    TextView hiScore1,hiScore2,hiScore3,hiScore4;
    ImageButton backButton;

    SharedPreferences sharedPreferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_high_score, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        backButton=(ImageButton) rootView.findViewById(R.id.backButton);
        hiScore1 = (TextView) rootView.findViewById(R.id.textView);
        hiScore2 = (TextView) rootView.findViewById(R.id.textView2);
        hiScore3 = (TextView) rootView.findViewById(R.id.textView3);
        hiScore4 = (TextView) rootView.findViewById(R.id.textView4);

        sharedPreferences  = this.getActivity().getSharedPreferences("JellyJuggler", Context.MODE_PRIVATE);


        hiScore1.setText(" 1.\t\t\t\t\t\t\t\t\t\t"+sharedPreferences.getInt("hiscore1",0));
        hiScore2.setText(" 2.\t\t\t\t\t\t\t\t\t\t"+sharedPreferences.getInt("hiscore2",0));
        hiScore3.setText(" 3.\t\t\t\t\t\t\t\t\t\t"+sharedPreferences.getInt("hiscore3",0));
        hiScore4.setText(" 4.\t\t\t\t\t\t\t\t\t\t"+sharedPreferences.getInt("hiscore4",0));

       backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }
}