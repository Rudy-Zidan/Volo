package com.quarklab.squarebash.core.logic;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;

import java.util.Random;

/**
 * Created by rudy on 6/21/16.
 */
public class GameHandler {
    private int speed;
    private Handler viewHandler;
    private Handler speedHandler;
    private Handler randomLevelHandler;

    public GameHandler(){
        this.viewHandler = new Handler();
        this.speedHandler = new Handler();
        this.randomLevelHandler = new Handler();
        this.speed = 1000;
    }
    public void start(){
        this.viewHandler.postDelayed(updateData,speed);
        this.speedHandler.postDelayed(updateSpeed,15000);
        this.randomLevelHandler.postDelayed(changeGameMode,20000);
    }
    public void end(){
        this.viewHandler.removeCallbacks(updateData);
        this.speedHandler.removeCallbacks(updateSpeed);
        this.randomLevelHandler.removeCallbacks(changeGameMode);
        GameEngine.showReplayDialog();
    }
    private Runnable updateData = new Runnable(){
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void run(){
            GameEngine.changeGameBoard();
            viewHandler.postDelayed(updateData,speed);
        }
    };

    private Runnable updateSpeed = new Runnable() {

        public void run() {
            Random rand = new Random();
            speed = rand.nextInt((1000 - 150) + 1)+150;
            int x = Math.abs((1000 - speed));
            GameEngine.changeScore((x*20)/100);
            speedHandler.postDelayed(updateSpeed,15000);
        }
    };

    private Runnable changeGameMode = new Runnable() {

        public void run() {
            GameEngine.changeGameMode();
            randomLevelHandler.postDelayed(changeGameMode,20000);
        }
    };
}
