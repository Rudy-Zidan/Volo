package com.quarklab.squarebash.core.logic;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quarklab.squarebash.R;
import com.quarklab.squarebash.GameBoard;
import com.quarklab.squarebash.SquareBash;
import com.quarklab.squarebash.core.TTS.Speaker;

import java.util.Locale;
import java.util.Random;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

/**
 * Created by rudy on 6/20/16.
 */
public class GameEngine {
    private enum gameMode {Easy,Sudden_death,No_Score,Double_tap}
    private int lifes;
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
    private static Speaker speaker;

    private ImageView heartIcon;

    private TextView scoreText;
    private TextView lifesText;

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
        this.speaker = new Speaker(this.context);

        this.lifes = 0;
        this.setLifes(4);

        this.scoreText = (TextView) ((Activity)this.context).findViewById(R.id.score);

        this.lifesText = (TextView) ((Activity)this.context).findViewById(R.id.lifes);
        this.heartIcon = (ImageView) ((Activity)this.context).findViewById(R.id.heart);
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
        resetButtons();
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
        String text = currentGameMode.name().replace("_", " ");
        speaker.speak(text);
        showMessage(text);
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
            case Sudden_death: endLife();
                break;
            case No_Score: nothing();
                break;
        }
    }

    private void evil(View button){
        int[] colors ={0XFFCE5A57,0XFFCE5A20,0XFFFF726E,0XFFB64F4C,0XFFB62B27,0XFFB46260};
        this.animate(button,colors,16);
        switch (this.currentGameMode){
            case Easy: endLife();
                break;
            case Double_tap: addScore(2);
                break;
            case Sudden_death: addScore(0);
                break;
            case No_Score: endLife();
                break;
        }
    }

    private void meh(View button){
        int[] colors ={0XFFE1B16A,0XFFE1B103,0XFFFDC97A, 0XFFFFB33F, 0XFFF0A93E,0XFFFF9A01};
        this.animate(button,colors,16);
        switch (this.currentGameMode){
            case Easy: reduceScore(0);
                break;
            case Double_tap: reduceScore(2);
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
        this.scoreNumber += this.calcScore(power);
        this.updateScoreText();
    }

    private void reduceScore(int power){
        this.scoreNumber -= this.calcScore(power);
        this.updateScoreText();
    }

    private void nothing(){
        this.gameBoard.soundManager.playSound(R.raw.lose);
    }

    private void endLife(){
        if(this.lifes > 0) {
            this.lifes -- ;
            this.lifesText.setText(""+this.lifes);
            this.animateHeartIcon();
        }
        if(this.lifes == 0) {
            this.gameBoard.soundManager.playSound(R.raw.gameover);
            this.stopGame();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void animateHeartIcon() {
        this.heartIcon.animate()
            .alpha(0.f)
            .scaleX(1.5f).scaleY(1.5f)
            .setDuration(300)
            .withEndAction(new Runnable() {
                @Override
                public void run() {
                    heartIcon.setAlpha(1.f);
                    heartIcon.setScaleX(1.f);
                    heartIcon.setScaleY(1.f);
                }
            })
            .start();
    }

    public static void showReplayDialog(){
        if(!gameBoard.backClicked){
            resetButtons();
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
                dialog.dismiss();
                speaker.shutdown();
                ((Activity)context).onBackPressed();
                }
            });

            dialog.show();
        }
    }

    public static boolean isEnded(){
        return ended;
    }

    private void animate(View button, int[] colors, int dots){
        int extraSpace = 50;
        int radius = button.getWidth()/2 + extraSpace;
        smallBang.setColors(colors);
        smallBang.setDotNumber(dots);
        smallBang.bang(button,radius,new SmallBangListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }


    private static void resetButtons() {
        GridView gameBoard = (GridView) ((Activity)context).findViewById(R.id.GameBoard);
        int count = gameBoard.getChildCount();
        for(int i = 0 ; i <count ; i++){
            Button child = (Button)gameBoard.getChildAt(i);
            child.setBackgroundResource(R.drawable.block);
            child.setTag("");
        }
    }

    private void setLifes(int n){
        this.lifes += n;
    }

    private int calcScore(int power) {
        int score = this.score;
        if(power !=0){
            score =  power*score;
        }
        return score;
    }

    private void updateScoreText(){
        this.gameBoard.setting.updateScore(this.scoreNumber);
        this.scoreText.setText(""+this.scoreNumber);
        this.gameBoard.soundManager.playSound(R.raw.score);
    }

}
