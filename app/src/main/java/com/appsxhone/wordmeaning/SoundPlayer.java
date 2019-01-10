package com.appsxhone.wordmeaning;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Sameer on 03-Mar-16.
 */
public class SoundPlayer {

    private MediaPlayer mediaPlayer;


    SoundPlayer(Context mContext) {
        mediaPlayer = MediaPlayer.create(mContext, R.raw.bg_sound);
        mediaPlayer.start();
    }

    void pauseSound(Context mContext){
        mediaPlayer.pause();
    }

    void resumeSound(Context mContext){
        mediaPlayer.start();
    }

    void stopSound(Context mContext){
        mediaPlayer.stop();
    }



}
