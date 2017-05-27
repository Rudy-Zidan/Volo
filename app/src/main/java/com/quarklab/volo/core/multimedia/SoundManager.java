package com.quarklab.volo.core.multimedia;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.quarklab.volo.GameBoard;
import com.quarklab.volo.core.sounds.BackgroundSound;
import com.quarklab.volo.core.sounds.BombSound;
import com.quarklab.volo.core.sounds.TicTocSound;

/**
 * Created by rudy on 6/20/16.
 */
public class SoundManager {
    private Context context;
    private BackgroundSound backgroundMusic;
    private TicTocSound ticTocSound;
    private BombSound bombSound;
    private GameBoard gameBoard;
    private MediaPlayer mPlayer;
    public  SoundManager(Context context){
        this.context = context;
        this.gameBoard = (GameBoard) context;
    }

    public void playSound(int music){
        if(this.gameBoard.setting.playSound()) {
            this.mPlayer = MediaPlayer.create(this.context, music);
            this.mPlayer.setVolume(1f, 1f);
            this.mPlayer.start();
            this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopMediaPlayer();
                }
            });
        }
    }
    public void startTicTocSound() {
        if(this.gameBoard.setting.playSound()){
            if(this.ticTocSound != null){
                this.stopTicTocSound();
                this.ticTocSound = null;
            }
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

    public void startBombSound() {
        if(this.gameBoard.setting.playSound()){
            if(this.bombSound != null){
                this.stopBombSound();
                this.bombSound = null;
            }
            this.bombSound = new BombSound(this.context);
            this.backgroundMusic.setVolume(0.3f, 0.3f);
            this.bombSound.execute();
        }
    }

    public void stopBombSound(){
        if(this.gameBoard.setting.playSound() && this.bombSound != null &&this.bombSound.isPlaying()) {
            if(this.backgroundMusic.isPlaying()){
                this.backgroundMusic.setVolume(1f, 1f);
            }
            this.bombSound.stop();
        }
    }

    public void startBackgroundSound(){
        if(this.gameBoard.setting.playSound()){
            this.backgroundMusic = new BackgroundSound(this.context);
            this.backgroundMusic.execute();
        }
    }

    public void stopBackgroundSound(){
        if(this.gameBoard.setting.playSound() && this.backgroundMusic != null && this.backgroundMusic.isPlaying()) {
            this.backgroundMusic.stop();
        }
    }

    private void stopMediaPlayer(){
        if(this.mPlayer != null){
            this.mPlayer.release();
            this.mPlayer = null;
        }
    }
}
