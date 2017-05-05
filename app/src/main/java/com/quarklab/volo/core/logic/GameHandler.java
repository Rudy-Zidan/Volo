package com.quarklab.volo.core.logic;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import java.util.Random;

/**
 * Created by rudy on 6/21/16.
 */
public class GameHandler {
    private int speed;
    private Handler viewHandler;
    private Handler speedHandler;
    private Handler randomLevelHandler;
    private Handler shapeHandler;
    private Handler utilityHandler;
    private GameEngineListener gameEngineListener;

    public GameHandler(GameEngineListener gameEngineListener) {
        this.gameEngineListener = gameEngineListener;
        this.viewHandler = new Handler();
        this.speedHandler = new Handler();
        this.randomLevelHandler = new Handler();
        this.shapeHandler = new Handler();
        this.utilityHandler = new Handler();
        this.speed = 1000;
    }

    public void start() {
        this.viewHandler.postDelayed(updateData, speed);
        this.speedHandler.postDelayed(updateSpeed, 15000);
        this.randomLevelHandler.postDelayed(changeGameMode, 20000);
        this.shapeHandler.postDelayed(changeShapes, 50000);
        this.utilityHandler.postDelayed(dropUtitlity, getRandomTime(50000, 100000));
    }

    public void end() {
        this.viewHandler.removeCallbacks(updateData);
        this.speedHandler.removeCallbacks(updateSpeed);
        this.randomLevelHandler.removeCallbacks(changeGameMode);
        this.shapeHandler.removeCallbacks(changeShapes);
        this.utilityHandler.removeCallbacks(dropUtitlity);
    }

    private Runnable updateData = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void run() {
            gameEngineListener.changeGameBoard();
            viewHandler.postDelayed(updateData, speed);
        }
    };

    private Runnable updateSpeed = new Runnable() {

        public void run() {
            Random rand = new Random();
            speed = rand.nextInt(1000);
            if (speed < 500) {
                speed = 500;
            }
            int x = Math.abs((1000 - speed));
            gameEngineListener.changeGameScore((x * 10) / 100);
            speedHandler.postDelayed(updateSpeed, 15000);
        }
    };

    private Runnable changeGameMode = new Runnable() {

        public void run() {
            new CountDownTimer(20000, 1) {
                @Override
                public void onTick(long l) {
                    long timeFrame = 5000 - l;
                    if(timeFrame == 0 || timeFrame == 1){
                        gameEngineListener.playTicTocSound();
                    }
                }

                @Override
                public void onFinish() {
                    gameEngineListener.stopTicTocSound();
                    gameEngineListener.changeGameMode();
                    randomLevelHandler.postDelayed(changeGameMode, 20000);
                }
            }.start();
        }
    };

    private Runnable changeShapes = new Runnable() {
        @Override
        public void run() {
            gameEngineListener.changeGameShape();
            shapeHandler.postDelayed(changeShapes, 50000);
        }
    };

    private Runnable dropUtitlity = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            gameEngineListener.claimUtility();
            shapeHandler.postDelayed(dropUtitlity, getRandomTime(50000, 100000));
        }
    };

    private int getRandomTime(int min, int max){
        Random rand = new Random();
        int n = rand.nextInt(max);
        if(n < min){
            n = min;
        }
        return n;
    }

}
