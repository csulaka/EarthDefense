package com.example.redfish.jellyjugglerlite;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Redfish on 8/5/2017.
 */

public class OptionsFragment extends DialogFragment {

    private CheckBox soundCheckbox;
    private CheckBox musicCheckbox;
    private CheckBox adsCheckbox;
    private ImageButton dismiss;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    //TODO Make Preference Sticky
    //TODO Add Admob?
    public OptionsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_options, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        preferences = this.getActivity().getSharedPreferences(GameView.PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        //TODO Stick Prefs
        soundCheckbox=(CheckBox) rootView.findViewById(R.id.soundCheckbox);
        soundCheckbox.setChecked(preferences.getBoolean("soundEnable",true));
        soundCheckbox.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener(){
                                                      @Override
                                                      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                            editor.putBoolean("soundEnable",soundCheckbox.isChecked());
                                                          editor.apply();
                                                      }
                                                  }

        );
        musicCheckbox=(CheckBox) rootView.findViewById(R.id.musicCheckbox);
        musicCheckbox.setChecked(preferences.getBoolean("musicEnable",true));
        musicCheckbox.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener(){
                                                      @Override
                                                      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                          editor.putBoolean("musicEnable",musicCheckbox.isChecked());

                                                          if(musicCheckbox.isChecked())
                                                            BackgroundMusic.unmuteMusic();
                                                          else
                                                              BackgroundMusic.muteMusic();
                                                          editor.apply();
                                                      }
                                                  }


        );
        adsCheckbox=(CheckBox) rootView.findViewById(R.id.adsCheckbox);
        adsCheckbox.setChecked(preferences.getBoolean("adsEnable",true));
        adsCheckbox.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener(){
                                                      @Override
                                                      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                          editor.putBoolean("adsEnable",adsCheckbox.isChecked());

                                                          if(adsCheckbox.isChecked())
                                                              getActivity().findViewById(R.id.adView).setVisibility(View.VISIBLE);
                                                          else
                                                              getActivity().findViewById(R.id.adView).setVisibility(View.INVISIBLE);
                                                          editor.apply();
                                                      }
                                                  }
        );
        dismiss = (ImageButton) rootView.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }



}