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
    private Handler timeUtilityHandler;
    private GameEngineListener gameEngineListener;
    private boolean lockSpeed;

    public GameHandler(GameEngineListener gameEngineListener) {
        this.gameEngineListener = gameEngineListener;
        this.viewHandler = new Handler();
        this.speedHandler = new Handler();
        this.randomLevelHandler = new Handler();
        this.shapeHandler = new Handler();
        this.utilityHandler = new Handler();
        this.timeUtilityHandler = new Handler();
        this.speed = 1000;
        this.lockSpeed = false;
    }

    public void start() {
        this.viewHandler.postDelayed(updateData, speed);
        this.speedHandler.postDelayed(updateSpeed, 15000);
        this.randomLevelHandler.postDelayed(changeGameMode, 20000);
        this.shapeHandler.postDelayed(changeShapes, 50000);
        this.utilityHandler.postDelayed(dropUtitlity, getRandomTime(50000, 100000));
        this.timeUtilityHandler.postDelayed(timeUtitlity, getRandomTime(10000, 50000));
    }

    public void end() {
        this.viewHandler.removeCallbacks(updateData);
        this.speedHandler.removeCallbacks(updateSpeed);
        this.randomLevelHandler.removeCallbacks(changeGameMode);
        this.shapeHandler.removeCallbacks(changeShapes);
        this.utilityHandler.removeCallbacks(dropUtitlity);
        this.timeUtilityHandler.removeCallbacks(timeUtitlity);
    }

    public void speedUpGameSpeed(){
        lockSpeed = true;
        viewHandler.removeCallbacksAndMessages(null);
        speed = speed - (int)(speed*0.25);
        viewHandler.postDelayed(updateData, speed);
    }

    public void slowUpGameSpeed(){
        lockSpeed = true;
        viewHandler.removeCallbacks(updateData);
        speed = speed + (int)(speed*0.50);
        viewHandler.postDelayed(updateData, speed);
    }

    public void unlockGameSpeed(){
        lockSpeed = false;
        viewHandler.removeCallbacks(updateData);
        speed = getRandomTime(500, 1000);
        viewHandler.postDelayed(updateData, speed);
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
            if(!lockSpeed){
                speed = getRandomTime(500, 1000);
            }
            int x = Math.abs((1000 - speed));
            gameEngineListener.changeGameScore((x * 10) / 100);
            speedHandler.postDelayed(updateSpeed, 15000);
        }
    };

    private Runnable changeGameMode = new Runnable() {

        public void run() {
            gameEngineListener.changeGameMode();
            randomLevelHandler.postDelayed(changeGameMode, 20000);
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
            utilityHandler.postDelayed(dropUtitlity, getRandomTime(50000, 100000));
        }
    };

    private Runnable timeUtitlity = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            gameEngineListener.claimTimeUtility();
            timeUtilityHandler.postDelayed(timeUtitlity, getRandomTime(10000, 50000));
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
