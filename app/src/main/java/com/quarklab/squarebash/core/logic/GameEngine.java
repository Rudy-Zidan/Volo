package com.quarklab.squarebash.core.logic;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quarklab.squarebash.R;
import com.quarklab.squarebash.GameBoard;
import com.quarklab.squarebash.SquareBash;

import java.util.Random;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

/**
 * Created by rudy on 6/20/16.
 */
public class GameEngine {
    private enum gameMode {Easy,Sudden_death,No_Score,Double_tap}
    private static gameMode currentGameMode;
    private static Context context;
    private static GameBoard gameBoard;
    private static int scoreNumber;
    private static int score;
    private static GameHandler gameHandler;
    private static int toastSpeed;
    private static boolean ended;
    private SmallBang smallBang;
    private static Toast toast;
    private static View toastAppear;

    public GameEngine(Context context){
        this.context = context;
        this.currentGameMode = gameMode.Easy;
        this.gameBoard = (GameBoard) this.context;
        this.scoreNumber = 0;
        this.score = 10;
        this.gameHandler = new GameHandler();
        this.toastSpeed = 100;
        ended = false;
        smallBang = SmallBang.attach2Window((Activity)this.context);
        LayoutInflater li = gameBoard.getLayoutInflater();
        toastAppear = li.inflate(R.layout.toast, (LinearLayout) gameBoard.findViewById(R.id.custom_toast_layout_id));
        toast = new Toast(context);
        toast.setView(toastAppear);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,Gravity.CENTER,Gravity.CENTER);
    }
    public static void startGame(){
        gameHandler.start();
    }
    public void stopGame(){
        this.gameHandler.end();
    }
    public void actionHandler(View button){
        switch (button.getTag().toString()){
            case "good": good(button);
                break;
            case "evil": evil(button);
                break;
            case "meh": meh(button);
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
            random = rand.nextInt(3) + 1;
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

    private void good(View button){
        int[] colors ={0XFF86AC41,0XFF99AC41,0XFF22AC41,0XFF00AC41,0XFF20AC50,0XFF82AC20};
        this.animate(button,colors,16);
        switch (this.currentGameMode){
            case Easy: addScore(0);
                break;
            case Double_tap: addScore(2);
                break;
            case Sudden_death: endGame();
                break;
            case No_Score: nothing();
                break;
        }
    }

    private void evil(View button){
        int[] colors ={0XFFCE5A57,0XFFCE5A20,0XFFFF726E,0XFFB64F4C,0XFFB62B27,0XFFB46260};
        this.animate(button,colors,16);
        switch (this.currentGameMode){
            case Easy: endGame();
                break;
            case Double_tap: addScore(2);
                break;
            case Sudden_death: addScore(0);
                break;
            case No_Score: endGame();
                break;
        }
    }

    private void meh(View button){
        int[] colors ={0XFFE1B16A,0XFFE1B103,0XFFFDC97A, 0XFFFFB33F, 0XFFF0A93E,0XFFFF9A01};
        this.animate(button,colors,16);
        switch (this.currentGameMode){
            case Easy: nothing();
                break;
            case Double_tap: nothing();
                break;
            case Sudden_death: nothing();
                break;
            case No_Score: nothing();
                break;
        }
    }

    private static void showMessage(String msg){
        TextView text = (TextView) toastAppear.findViewById(R.id.toast_text);
        text.setText(msg);
        toast.show();
    }

    private void addScore(int power){
        this.scoreNumber += power !=0 ? power*this.score : this.score;
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
        if(!gameBoard.backClicked){
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
    }

    public static boolean isEnded(){
        return ended;
    }

    private void animate(View button, int[] colors, int dots){
        smallBang.setColors(colors);
        smallBang.setDotNumber(dots);
        smallBang.bang(button,300,new SmallBangListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }
}
