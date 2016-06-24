package com.quarklab.squarebash.core.logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.quarklab.squarebash.R;
import com.quarklab.squarebash.GameBoard;

import java.util.Random;

/**
 * Created by rudy on 6/20/16.
 */
public class GameEngine {
    private enum gameMode {Easy,Sudden_death,No_Score}
    private static gameMode currentGameMode;
    private static Context context;
    private GameBoard gameBoard;
    private int scoreNumber;
    private GameHandler gameHandler;
    private static int toastSpeed;

    public GameEngine(Context context){
        this.context = context;
        this.currentGameMode = gameMode.Easy;
        this.gameBoard = (GameBoard) this.context;
        this.scoreNumber = 0;
        this.gameHandler = new GameHandler();
        this.toastSpeed = 100;
    }
    public void startGame(){
        this.gameHandler.start();
    }
    public void stopGame(){
        this.gameHandler.end();
    }
    public void actionHandler(View button){
        switch (button.getTag().toString()){
            case "good": good();
                break;
            case "evil": evil();
                break;
            case "meh": meh();
                break;
        }
    }

    public static void changeGameBoard(){
        GridView gameBoard = (GridView) ((Activity)context).findViewById(R.id.GameBoard);
        int count = gameBoard.getChildCount();
        Random r = new Random();
        int index = r.nextInt(count);
        for(int i = 0 ; i <count ; i++){
            Button child = (Button)gameBoard.getChildAt(i);
            child.setBackgroundColor(Color.WHITE);
            child.setTag("");
        }
        Button target = (Button)gameBoard.getChildAt(index);
        switch (r.nextInt(3)){
            case 0:
                target.setBackgroundColor(Color.parseColor("#00b400"));
                target.setTag("good");
                break;
            case 1:
                target.setBackgroundColor(Color.parseColor("#d40000"));
                target.setTag("evil");
                break;
            case 2:
                target.setBackgroundColor(Color.parseColor("#ffff00"));
                target.setTag("meh");
                break;
        }
    }

    public static void changeGameMode() {
        Random rand = new Random();
        int random = rand.nextInt(10);
        if (random % 2 == 0) {
            if (!currentGameMode.equals(gameMode.Easy))
                currentGameMode = gameMode.Easy;
        } else {
            random = rand.nextInt(2) + 1;
            if (!currentGameMode.equals(gameMode.values()[random]))
                currentGameMode = gameMode.values()[random];
        }
        showMessage(currentGameMode.name().replace("_", " "));
    }

    private void good(){
        switch (this.currentGameMode){
            case Easy: addScore();
                break;
            case Sudden_death: endGame();
                break;
            case No_Score: nothing();
                break;
        }
    }

    private void evil(){
        switch (this.currentGameMode){
            case Easy: endGame();
                break;
            case Sudden_death: addScore();
                break;
            case No_Score: endGame();
                break;
        }
    }

    private void meh(){
        switch (this.currentGameMode){
            case Easy: nothing();
                break;
            case Sudden_death: nothing();
                break;
            case No_Score: nothing();
                break;
        }
    }

    private static void showMessage(String msg){
        LayoutInflater inflater =  ((Activity)context).getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.toast,
                (ViewGroup) ((Activity)context).findViewById(R.id.custom_toast_layout_id));
        TextView text = (TextView) toastLayout.findViewById(R.id.text);
        text.setText(msg);
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(toastSpeed);
        toast.setView(toastLayout);
        toast.show();
    }

    private void addScore(){
        showMessage("Nice :)");
        this.scoreNumber += 10;
        TextView score = (TextView) ((Activity)this.context).findViewById(R.id.score);
        score.setText("Score: " + this.scoreNumber);
        this.gameBoard.soundManager.playSound(R.raw.score);
    }

    private void nothing(){
        showMessage("Oh you missed !!!");
        this.gameBoard.soundManager.playSound(R.raw.lose);
    }

    private void endGame(){
        showMessage("Game Over");
        this.gameBoard.soundManager.playSound(R.raw.gameover);
        this.stopGame();
    }
}
