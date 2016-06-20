package com.quarklab.squarebash.core.multimedia;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import com.quarklab.squarebash.R;

/**
 * Created by rudy on 6/20/16.
 */
public class SoundManager {
    private Context context;
    private BackgroundSound backgroundMusic;
    public  SoundManager(Context context){
        this.backgroundMusic = new BackgroundSound();
        this.context = context;
    }

    public void playSound(int music){
        MediaPlayer player = MediaPlayer.create(this.context,music);
        player.setVolume(100,100);
        player.start();
    }

    public void startBackgroundSound(){
        this.backgroundMusic.execute(null);
    }
    public void stopBackgroundSound(){
        this.backgroundMusic.cancel(true);
    }
    public class BackgroundSound extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            MediaPlayer player = MediaPlayer.create(context,R.raw.background);
            player.setLooping(true); // Set looping
            player.setVolume(25,25);
            player.start();
            return null;
        }
    }
}
