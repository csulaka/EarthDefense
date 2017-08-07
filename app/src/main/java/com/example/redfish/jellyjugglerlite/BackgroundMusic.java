package com.example.redfish.jellyjugglerlite;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Redfish on 8/6/2017.
 */

public class BackgroundMusic {
    public static MediaPlayer music;
    public static void musicPlaying(Context context) {
        music = MediaPlayer.create(context, R.raw.music);
        music.setLooping(true);
        music.start();
    }
    public static void stopMusic() {
        if (music != null) {
            if (music.isPlaying()) {
                music.stop();
            }
        }
    }
}
