package com.quarklab.volo.core.multimedia;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.quarklab.volo.GameBoard;
import com.quarklab.volo.core.sounds.BackgroundSound;
import com.quarklab.volo.core.sounds.TicTocSound;

/**
 * Created by rudy on 6/20/16.
 */
public class SoundManager {
    private Context context;
    private BackgroundSound backgroundMusic;
    private TicTocSound ticTocSound;
    private GameBoard gameBoard;
    public  SoundManager(Context context){
        this.context = context;
        this.gameBoard = (GameBoard) context;
    }

    public void playSound(int music){
        if(this.gameBoard.setting.playSound()) {
            MediaPlayer player = MediaPlayer.create(this.context, music);
            player.setVolume(1f, 1f);
            player.start();
        }
    }
    public void startTicTocSound() {
        if(this.gameBoard.setting.playSound()){
            this.ticTocSound = new TicTocSound(this.context);
            this.backgroundMusic.setVolume(0.3f, 0.3f);
            this.ticTocSound.execute();
        }
    }

    public void stopTicTocSound(){
        if(this.gameBoard.setting.playSound() && this.ticTocSound != null &&this.ticTocSound.isPlaying()) {
            if(this.backgroundMusic.isPlaying()){
                this.backgroundMusic.setVolume(1f, 1f);
            }
            this.ticTocSound.stop();
        }
    }

    public void startBackgroundSound(){
        if(this.gameBoard.setting.playSound()){
            this.backgroundMusic = new BackgroundSound(this.context);
            this.backgroundMusic.execute();
        }

    }

    public void stopBackgroundSound(){
        if(this.gameBoard.setting.playSound() && this.backgroundMusic != null) {
            this.backgroundMusic.stop();
        }
    }
}
