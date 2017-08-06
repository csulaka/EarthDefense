package com.example.redfish.jellyjugglerlite;

import android.app.DialogFragment;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Redfish on 8/5/2017.
 */

public class OptionsFragment extends DialogFragment {

    private CheckBox soundCheckbox;
    private CheckBox musicCheckbox;
    private ImageButton dismiss;
    public interface PreferencesListener {
        void onFinishUserDialog(boolean musicEnable, boolean soundEnable, boolean adsEnable);
    }
    //TODO Make Preference Sticky
    //TODO Add Admob?
    public OptionsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_options, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        soundCheckbox=(CheckBox) rootView.findViewById(R.id.soundCheckbox);
        musicCheckbox=(CheckBox) rootView.findViewById(R.id.musicCheckbox);

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