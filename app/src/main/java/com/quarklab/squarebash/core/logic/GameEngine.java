package com.quarklab.squarebash.core.logic;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.quarklab.squarebash.R;
import com.quarklab.squarebash.GameBoard;
import com.quarklab.squarebash.SquareBash;

import java.util.Random;

/**
 * Created by rudy on 6/20/16.
 */
public class GameEngine {
    private enum gameMode {Easy,Sudden_death,No_Score}
    private static gameMode currentGameMode;
    private static Context context;
    private static GameBoard gameBoard;
    private static int scoreNumber;
    private static int score;
    private static GameHandler gameHandler;
    private static int toastSpeed;
    private static boolean ended;

    public GameEngine(Context context){
        this.context = context;
        this.currentGameMode = gameMode.Easy;
        this.gameBoard = (GameBoard) this.context;
        this.scoreNumber = 0;
        this.score = 10;
        this.gameHandler = new GameHandler();
        this.toastSpeed = 100;
        ended = false;
    }
    public static void startGame(){
        gameHandler.start();
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
            child.setBackgroundResource(R.drawable.block);
            child.setTag("");
        }
        Button target = (Button)gameBoard.getChildAt(index);
        switch (r.nextInt(3)){
            case 0:
                target.setBackgroundResource(R.drawable.green_button);
                target.setTag("good");
                break;
            case 1:
                target.setBackgroundResource(R.drawable.red_button);
                target.setTag("evil");
                break;
            case 2:
                target.setBackgroundResource(R.drawable.warm_button);
                target.setTag("meh");
                break;
        }
    }

    public static void changeGameMode() {
        Random rand = new Random();
        int random = rand.nextInt(10);
        if (random % 2 == 0) {
            if (!currentGameMode.equals(gameMode.Easy)) {
                currentGameMode = gameMode.Easy;
            }else{
                changeGameMode();
            }
        } else {
            random = rand.nextInt(2) + 1;
            if (!currentGameMode.equals(gameMode.values()[random])) {
                currentGameMode = gameMode.values()[random];
            }else{
                changeGameMode();
            }
        }
        showMessage(currentGameMode.name().replace("_", " "));
    }

    public static void changeScore(int value){
        score = value;
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
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }

    private void addScore(){
        this.scoreNumber += this.score;
        this.gameBoard.setting.updateScore(this.scoreNumber);
        TextView score = (TextView) ((Activity)this.context).findViewById(R.id.score);
        score.setText(""+this.scoreNumber);
        this.gameBoard.soundManager.playSound(R.raw.score);
    }

    private void nothing(){
        this.gameBoard.soundManager.playSound(R.raw.lose);
    }

    private void endGame(){
        this.gameBoard.soundManager.playSound(R.raw.gameover);
        this.stopGame();
    }

    public static void showReplayDialog(){
        ended = true;
        final Dialog dialog = new Dialog(context);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.replay_dialog);

        TextView score = (TextView) dialog.findViewById(R.id.dialog_score);
        score.setText(gameBoard.setting.getScore()+"");

        Button again = (Button) dialog.findViewById(R.id.btn_replay);
        Button no = (Button) dialog.findViewById(R.id.btn_no);

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreNumber = 0;
                TextView score = (TextView) ((Activity)context).findViewById(R.id.score);
                score.setText(""+scoreNumber);
                dialog.dismiss();
                startGame();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameBoardIntent= new Intent(context,SquareBash.class);
                context.startActivity(gameBoardIntent);
            }
        });


        dialog.show();
    }

    public static boolean isEnded(){
        return ended;
    }
}
