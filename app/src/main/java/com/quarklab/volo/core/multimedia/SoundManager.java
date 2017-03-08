package com.quarklab.volo.core.multimedia;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.quarklab.volo.GameBoard;
import com.quarklab.volo.R;

/**
 * Created by rudy on 6/20/16.
 */
public class SoundManager {
    private Context context;
    private BackgroundSound backgroundMusic;
    private GameBoard gameBoard;
    public  SoundManager(Context context){
        this.context = context;
        this.gameBoard = (GameBoard) context;
    }

    public void playSound(int music){
        if(this.gameBoard.setting.playSound()) {
            MediaPlayer player = MediaPlayer.create(this.context, music);
            player.setVolume(100, 100);
            player.start();
        }
    }

    public void startBackgroundSound(){
        if(this.gameBoard.setting.playSound()){
            this.backgroundMusic = new BackgroundSound();
            this.backgroundMusic.execute();
        }

    }
    public void stopBackgroundSound(){
        if(this.gameBoard.setting.playSound()) {
            this.backgroundMusic.stop();
        }
    }
    public class BackgroundSound extends AsyncTask<Void, Void, Void> {
        private MediaPlayer player;

        @Override
        protected void onPreExecute() {
            this.player = MediaPlayer.create(context, R.raw.background);
        }

        @Override
        protected Void doInBackground(Void... params) {
            this.player.setLooping(true); // Set looping
            this.player.setVolume(25,25);
            this.player.start();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        protected void stop(){
            this.player.stop();
            this.player.release();
            this.cancel(true);
        }
    }
}
