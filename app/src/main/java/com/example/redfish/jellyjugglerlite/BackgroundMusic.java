package com.example.redfish.jellyjugglerlite;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Redfish on 8/6/2017.
 */

public class BackgroundMusic {
    public static MediaPlayer music;
    public static void musicPlaying(Context context) {
        if(music==null) {
            music = MediaPlayer.create(context, R.raw.music);
            music.setLooping(true);
        }
        startMusic();
    }
    public static void muteMusic(){
        if(music!=null){
            music.setVolume(0,0);
        }
    }
    public static void unmuteMusic(){
        if(music!=null){
            music.setVolume(1,1);
        }
    }
    public static void startMusic(){
        if(music!=null&&!music.isPlaying())
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
