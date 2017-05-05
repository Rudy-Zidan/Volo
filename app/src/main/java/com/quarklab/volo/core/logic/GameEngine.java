package com.quarklab.volo.core.logic;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private GameMode gameMode;
    private Context context;
    private GameBoard gameBoard;
    private GameHandler gameHandler;
    private SmallBang smallBang;
    private OnBoardNotification onBoardNotification;
    private Utility utility;
    private GameModeListener gameModeListener;
    private Shape shape;
    private TextView timerText;
    private CountDownTimer lifeTimer;

    private TextView scoreText;
    private TextView lifesText;

    private int scoreNumber;
    private int currentUserScore;
    private int currentScoreRate;
    private int lifes;
    private boolean ended;
    private int scoreContinousTimes;
    private long startClickedGreen;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public GameEngine(Context context) {
        this.context = context;
        this.gameBoard = (GameBoard) this.context;
        this.gameHandler = new GameHandler(this.initializeListener());
        this.smallBang = SmallBang.attach2Window((Activity) this.context);
        this.onBoardNotification = new OnBoardNotification(this.context);
        this.shape = new Shape();

        this.initGameModeListener();
        this.initGameMode();
        this.initializeUtility();

        this.scoreText = (TextView) ((Activity) this.context).findViewById(R.id.score);
        this.lifesText = (TextView) ((Activity) this.context).findViewById(R.id.lifes);
        this.timerText = (TextView) ((Activity) this.context).findViewById(R.id.timerText);

        this.scoreContinousTimes = 0;
        this.startClickedGreen = 0;
        this.ended = false;
    }

    public void startGame() {
        this.lifes = 4;
        this.lifesText.setText(""+lifes);
        this.currentUserScore = 0;
        this.gameBoard.soundManager.startBackgroundSound();
        this.gameHandler.start();
        this.startLifeTimeCounter();
    }

    public void stopGame() {
        this.gameBoard.soundManager.stopTicTocSound();
        this.gameBoard.soundManager.stopBackgroundSound();
        this.gameHandler.end();
        this.showReplayDialog();
    }

    public void actionHandler(View button) {
        this.onBoardNotification.setX((button.getX() + (button.getWidth() / 2)) - 25);
        this.onBoardNotification.setY(button.getY());
        switch (button.getTag().toString()) {
            case "good": this.good(button);
                break;
            case "evil": this.evil(button);
                break;
            case "meh": this.meh(button);
                break;
            case "oval_random": this.random(button);
                break;
        }

    }

    public void changeGameBoard() {
        this.gameBoard.getRenderEngine().renderButton(shape);
    }

    public void changeGameMode() {
        this.gameMode.change();
        this.gameBoard.soundManager.playSound(R.raw.mod_change);
        String text = this.gameMode.getCurrentGameMode();
        this.centerNotification(text);
    }

    public void changeGameShape() {
        this.shape.changeShape();
    }

    public void changeScore(int value) {
        this.currentScoreRate = value;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void claimUtility() {
        this.utility.fallOneDown();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initializeUtility(){
         this.utility = new Utility(this.context, new UtilityListener() {
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
                    this.scoreAdded(score);
                    this.gameBoard.soundManager.playSound(R.raw.coins);
                    this.centerNotification("+ "+score);
                break;
            case 1: this.addLife(1);
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

        this.gameBoard.getRenderEngine().changeDefaultColor(shape);

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
                stopTicToc();
            }
        }.start();
    }

    private void good(View button) {
        this.onBoardNotification.setColor(R.color.green);
        int[] colors ={0XFF86AC41,0XFF99AC41,0XFF22AC41,0XFF00AC41,0XFF20AC50,0XFF82AC20};
        this.animate(button,colors,16);
    }

    private void evil(View button) {
        this.onBoardNotification.setColor(R.color.red);
        int[] colors ={0XFFCE5A57,0XFFCE5A20,0XFFFF726E,0XFFB64F4C,0XFFB62B27,0XFFB46260};
        this.animate(button,colors,16);
    }

    private void meh(View button) {
        this.onBoardNotification.setColor(R.color.warm);
        int[] colors ={0XFFE1B16A,0XFFE1B103,0XFFFDC97A, 0XFFFFB33F, 0XFFF0A93E,0XFFFF9A01};
        this.animate(button,colors,16);
    }

    private void random(View button) {
        this.onBoardNotification.setColor(R.color.skyblue);
        int[] colors ={0XFF7ec0ee,0XFF8ac6ef,0XFF97ccf1,0XFF71acd6, 0XFF8ac6ef,0XFF7ec0ee};
        this.animate(button,colors,16);
    }

    public void showReplayDialog() {
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

    public boolean isEnded() {
        return this.ended;
    }

    public Shape getShape(){return this.shape;}

    public void playTicToc(){
        this.gameBoard.soundManager.startTicTocSound();
    }

    public void stopTicToc(){
        this.gameBoard.soundManager.stopTicTocSound();
    }

    private void animate(final View button, int[] colors, int dots) {
        int extraSpace = 50;
        int radius = button.getWidth() / 2 + extraSpace;
        this.smallBang.setColors(colors);
        this.smallBang.setDotNumber(dots);
        this.smallBang.bang(button, radius, new SmallBangListener() {
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
        this.gameBoard.setting.updateScore(score);
        this.scoreText.setText(""+Numbers.format(score));
    }

    private void initGameMode() {
        this.gameMode = new GameMode(this.gameModeListener);
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
            this.lifes--;
            this.lifesText.setText(""+lifes);
            this.gameBoard.soundManager.playSound(R.raw.life_lost);
        }
        if(lifes == 0) {
            this.gameBoard.soundManager.playSound(R.raw.over);
            this.lifeTimer.cancel();
            this.stopGame();
        }
    }

    private void reduceScore(int score) {
        this.currentUserScore -= score;
        if(this.currentUserScore < 0){
            this.currentUserScore = 0;
        }
        this.updateScoreText(this.currentUserScore);
        this.onBoardNotification.notify("- "+score, 500, 18, false);
    }

    private void scoreAdded(int score) {
        this.currentUserScore += score;
        this.updateScoreText(currentUserScore);
        long lastClickGreen = System.currentTimeMillis();
        double firstAndLastClickDiff = (lastClickGreen - this.startClickedGreen) / 1000.0;
        if (this.scoreContinousTimes == 3 && firstAndLastClickDiff <= 10.0 ) {
            this.scoreContinousTimes = 0;
            this.startClickedGreen = 0;
            this.gameBoard.soundManager.playSound(R.raw.sweet);
            this.centerNotification("Strike");
        }else if(firstAndLastClickDiff > 10.0){
            this.scoreContinousTimes = 0;
            this.startClickedGreen = 0;
        }
    }

    private void addLife(int n) {
        this.lifes+=n;
        this.lifesText.setText(""+lifes);
        this.gameBoard.soundManager.playSound(R.raw.life_added);
        this.centerNotification("+1 Life");
    }

    private void centerNotification(String msg){
        this.onBoardNotification.setX((this.gameBoard.getRenderEngine().getScreenWidth()/2));
        this.onBoardNotification.setY(this.gameBoard.getRenderEngine().getScreenHeight()/2);
        this.onBoardNotification.setColor(R.color.white);
        this.onBoardNotification.notify(msg, 1500, 30, true);
    }

    private void startLifeTimeCounter() {
        Random rand = new Random();
        int minutes = rand.nextInt(4);
        if(minutes < 1){
            minutes = 1;
        }
        int interval = 1000;
        long minutesInMilliseconds = minutes * 60000;
        this.timerText.setText(minutes+":00");
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

    private GameEngineListener initializeListener(){
        final GameEngine engine = this;
        return new GameEngineListener() {
            @Override
            public void changeGameBoard() {
                engine.changeGameBoard();
            }

            @Override
            public void changeGameScore(int score) {
                engine.changeScore(score);
            }

            @Override
            public void changeGameMode() {
                engine.changeGameMode();
            }

            @Override
            public void changeGameShape() {
                engine.changeGameShape();
            }

            @Override
            public void playTicTocSound() {
                engine.playTicToc();
            }

            @Override
            public void stopTicTocSound() {
                engine.stopTicToc();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void claimUtility() {
                engine.claimUtility();
            }
        };
    }
}
