package com.quarklab.volo.core.logic;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quarklab.volo.R;
import com.quarklab.volo.GameBoard;
import com.quarklab.volo.core.modes.GameMode;
import com.quarklab.volo.core.modes.GameModeListener;
import com.quarklab.volo.core.notification.OnBoardNotification;
import com.quarklab.volo.core.shapes.Shape;
import com.quarklab.volo.core.utilities.Utility;
import com.quarklab.volo.core.utilities.UtilityListener;

import java.util.Random;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

/**
 * Created by rudy on 6/20/16.
 */
public class GameEngine {
    private static GameMode gameMode;
    private static Context context;
    private static GameBoard gameBoard;
    private static int scoreNumber;
    private static int currentUserScore;
    private static int currentScoreRate;
    private static int lifes;
    private static GameHandler gameHandler;
    private static boolean ended;
    private SmallBang smallBang;
    private static Toast toast;
    private static View toastAppear;
    private int scoreContinousTimes;
    private long startClickedGreen;

    private TextView scoreText;
    private static TextView lifesText;

    private static OnBoardNotification onBoardNotification;
    private static Utility utility;
    private GameModeListener gameModeListener;

    private static Shape shape;

    private TextView timerText;
    private CountDownTimer lifeTimer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public GameEngine(Context context) {
        GameEngine.context = context;
        gameBoard = (GameBoard) GameEngine.context;
        gameHandler = new GameHandler();

        this.initGameModeListener();
        this.initGameMode();
        this.initializeUtility();

        ended = false;
        smallBang = SmallBang.attach2Window((Activity) GameEngine.context);
        LayoutInflater li = gameBoard.getLayoutInflater();
        toastAppear = li.inflate(R.layout.toast, (LinearLayout) gameBoard.findViewById(R.id.custom_toast_layout_id));
        toast = new Toast(context);
        toast.setView(toastAppear);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,Gravity.CENTER,Gravity.CENTER);

        this.scoreText = (TextView) ((Activity) GameEngine.context).findViewById(R.id.score);

        lifesText = (TextView) ((Activity) GameEngine.context).findViewById(R.id.lifes);
        this.timerText = (TextView) ((Activity) GameEngine.context).findViewById(R.id.timerText);

        onBoardNotification = new OnBoardNotification(GameEngine.context);

        shape = new Shape();

        this.scoreContinousTimes = 0;
        this.startClickedGreen = 0;
        this.startLifeTimeCounter();
    }

    public static void startGame() {
        lifes = 4;
        lifesText.setText(""+lifes);
        currentUserScore = 0;
        gameBoard.soundManager.startBackgroundSound();
        gameHandler.start();
    }

    public void stopGame() {
        gameBoard.soundManager.stopTicTocSound();
        gameBoard.soundManager.stopBackgroundSound();
        gameHandler.end();
    }

    public void actionHandler(View button) {
        onBoardNotification.setX((button.getX() + (button.getWidth() / 2)) - 25);
        onBoardNotification.setY(button.getY());
        switch (button.getTag().toString()) {
            case "good": good(button);
                break;
            case "evil": evil(button);
                break;
            case "meh": meh(button);
                break;
            case "oval_random": random(button);
                break;
        }

    }

    public static void changeGameBoard() {
        gameBoard.getRenderEngine().renderButton(shape);
    }

    public static void changeGameMode() {
        gameMode.change();
        gameBoard.soundManager.playSound(R.raw.mod_change);
        String text = gameMode.getCurrentGameMode();
        centerNotification(text);
    }

    public static void changeGameShape() {
        shape.changeShape();
    }

    public static void changeScore(int value) {
        currentScoreRate = value;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void claimUtility() {
        utility.fallOneDown();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initializeUtility(){
         this.utility = new Utility(GameEngine.context, new UtilityListener() {
            @Override
            public void onGiftClick(ImageView image) {
                giftAction();
            }

            @Override
            public void onBombClick(ImageView image) {
                bombAction();
            }

        });
    }

    private void giftAction(){
        Random rand = new Random();
        switch (rand.nextInt(2)) {
            case 0: int score = rand.nextInt(100)+1;
                    scoreAdded(score);
                    gameBoard.soundManager.playSound(R.raw.coins);
                    centerNotification("+ "+score);
                break;
            case 1: addLife(1);
                break;
        }
    }

    private void bombAction(){

        Random rand = new Random();
        int totalSeconds = rand.nextInt(10000);
        if(totalSeconds < 5000){
            totalSeconds = 5000;
        }
        int interval = 1000;

        gameBoard.getRenderEngine().changeDefaultColor(shape);

        new CountDownTimer(totalSeconds, interval) {
            boolean isStarted = false;

            @Override
            public void onTick(long l) {
                if(!isStarted){
                    playTicToc();
                }
                timerText.setText(""+Math.round(l/1000.0));
                isStarted = true;
            }

            @Override
            public void onFinish() {
                gameBoard.getRenderEngine().resetDefaultColor(shape);
                timerText.setText(" ");
                isStarted = false;
            }
        }.start();

    }

    private void good(View button) {
        onBoardNotification.setColor(R.color.green);
        int[] colors ={0XFF86AC41,0XFF99AC41,0XFF22AC41,0XFF00AC41,0XFF20AC50,0XFF82AC20};
        this.animate(button,colors,16);
    }

    private void evil(View button) {
        onBoardNotification.setColor(R.color.red);
        int[] colors ={0XFFCE5A57,0XFFCE5A20,0XFFFF726E,0XFFB64F4C,0XFFB62B27,0XFFB46260};
        this.animate(button,colors,16);
    }

    private void meh(View button) {
        onBoardNotification.setColor(R.color.warm);
        int[] colors ={0XFFE1B16A,0XFFE1B103,0XFFFDC97A, 0XFFFFB33F, 0XFFF0A93E,0XFFFF9A01};
        this.animate(button,colors,16);
    }

    private void random(View button) {
        onBoardNotification.setColor(R.color.skyblue);
        int[] colors ={0XFF7ec0ee,0XFF8ac6ef,0XFF97ccf1,0XFF71acd6, 0XFF8ac6ef,0XFF7ec0ee};
        this.animate(button,colors,16);
    }

    public static void showReplayDialog() {
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
                dialog.dismiss();
                ((Activity)context).onBackPressed();
                }
            });

            dialog.show();
        }
    }

    public static boolean isEnded() {
        return ended;
    }

    public Shape getShape(){return shape;}

    public static void playTicToc(){
        gameBoard.soundManager.startTicTocSound();
    }

    public static void stopTicToc(){
        gameBoard.soundManager.stopTicTocSound();
    }

    private void animate(final View button, int[] colors, int dots) {
        int extraSpace = 50;
        int radius = button.getWidth()/2 + extraSpace;
        smallBang.setColors(colors);
        smallBang.setDotNumber(dots);
        smallBang.bang(button,radius,new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                String tag = button.getTag().toString();
                if(tag.equals("oval_random")) {
                    Random rand = new Random();
                    switch (rand.nextInt(4)) {
                        case 0: gameModeListener.onScoreAdded(0);
                            break;
                        case 1: gameModeListener.onScoreReduced(0);
                            break;
                        case 2: gameModeListener.onLifeLost();
                            break;
                        case 3: gameModeListener.onLifeAdded(1);
                            break;
                    }
                } else {
                    gameMode.execute(button.getTag().toString());
                }
            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }

    private void updateScoreText(int score) {
        gameBoard.setting.updateScore(score);
        this.scoreText.setText(""+Numbers.format(score));
    }

    private void initGameMode() {
        gameMode = new GameMode(this.gameModeListener);
    }

    private void initGameModeListener(){
        this.gameModeListener = new GameModeListener() {
            @Override
            public void nothing() {
                gameBoard.soundManager.playSound(R.raw.over);
            }

            @Override
            public void onScoreAdded(int score) {
                if(startClickedGreen == 0){
                    startClickedGreen = System.currentTimeMillis();
                }
                scoreContinousTimes+=1;
                onBoardNotification.notify("+ "+score, 500, 18, false);
                scoreAdded(score + currentScoreRate);
                gameBoard.soundManager.playSound(R.raw.score);
            }

            @Override
            public void onScoreReduced(int score) {
                scoreContinousTimes = 0;
                reduceScore(score + currentScoreRate);
                gameBoard.soundManager.playSound(R.raw.score_lost);
            }

            @Override
            public void onLifeLost() {
                reduceLife();
            }

            @Override
            public void onLifeAdded(int n){
                addLife(n);
            }
        };
    }

    private void reduceLife() {
        if(lifes > 0) {
            lifes--;
            lifesText.setText(""+lifes);
            gameBoard.soundManager.playSound(R.raw.life_lost);
        }
        if(lifes == 0) {
            gameBoard.soundManager.playSound(R.raw.over);
            this.lifeTimer.cancel();
            stopGame();
        }
    }

    private void reduceScore(int score) {
        currentUserScore -= score;
        if(currentUserScore < 0){
            currentUserScore = 0;
        }
        updateScoreText(currentUserScore);
        onBoardNotification.notify("- "+score, 500, 18, false);
    }

    private void scoreAdded(int score) {
        currentUserScore += score;
        updateScoreText(currentUserScore);
        long lastClickGreen = System.currentTimeMillis();
        double firstAndLastClickDiff = (lastClickGreen - this.startClickedGreen) / 1000.0;
        if (this.scoreContinousTimes == 3 && firstAndLastClickDiff <= 10.0 ) {
            this.scoreContinousTimes = 0;
            this.startClickedGreen = 0;
            gameBoard.soundManager.playSound(R.raw.sweet);
            centerNotification("Strike");
        }else if(firstAndLastClickDiff > 10.0){
            this.scoreContinousTimes = 0;
            this.startClickedGreen = 0;
        }
    }

    private void addLife(int n) {
        lifes+=n;
        lifesText.setText(""+lifes);
        gameBoard.soundManager.playSound(R.raw.life_added);
        centerNotification("+1 Life");
    }

    private static void centerNotification(String msg){
        onBoardNotification.setX((gameBoard.getRenderEngine().getScreenWidth()/2));
        onBoardNotification.setY(gameBoard.getRenderEngine().getScreenHeight()/2);
        onBoardNotification.setColor(R.color.white);
        onBoardNotification.notify(msg, 1500, 30, true);
    }

    private void startLifeTimeCounter() {
        Random rand = new Random();
        int minutes = rand.nextInt(4);
        if(minutes < 1){
            minutes = 1;
        }
        int interval = 1000;
        long minutesInMilliseconds = minutes * 60000;
        timerText.setText(minutes+":00");
        this.lifeTimer = new CountDownTimer(minutesInMilliseconds, interval) {
            boolean isStarted = false;

            @Override
            public void onTick(long l) {
                long minutes = Math.round(l / 60000);
                long seconds = Math.round((l % 60000) / 1000);
                String time =  minutes + ":" + (seconds < 10 ? '0' : "") + seconds;
                timerText.setText(time);
                if(!isStarted && minutes == 0 && seconds == 5){
                    timerText.setTextColor(context.getResources().getColor(R.color.red));
                    playTicToc();
                }
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00");
                stopGame();
            }
        };
        this.lifeTimer.start();
    }
}
