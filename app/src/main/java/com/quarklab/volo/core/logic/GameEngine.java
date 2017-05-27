package com.quarklab.volo.core.logic;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quarklab.volo.R;
import com.quarklab.volo.GameBoard;
import com.quarklab.volo.core.callback.AnimationDrawableCallback;
import com.quarklab.volo.core.modes.GameMode;
import com.quarklab.volo.core.modes.GameModeListener;
import com.quarklab.volo.core.notification.OnBoardNotification;
import com.quarklab.volo.core.shapes.Shape;
import com.quarklab.volo.core.utilities.Utility;
import com.quarklab.volo.core.utilities.UtilityListener;

import java.util.Random;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;


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
    private TextView gameModeText;
    private ImageView gameModeIcon;
    private LinearLayout gameModeHolder;

    private int scoreNumber;
    private int currentUserScore;
    private int currentScoreRate;
    private long currentTimeInMilliSec;
    private int lifes;
    private boolean ended;
    private int scoreContinousTimes;
    private long startClickedGreen;
    private boolean gameEnding = false;

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
        this.gameModeText = (TextView) ((Activity) this.context).findViewById(R.id.game_mode_text);
        this.gameModeIcon = (ImageView) ((Activity) this.context).findViewById(R.id.game_mode_icon);
        this.gameModeHolder = (LinearLayout) ((Activity) this.context).findViewById(R.id.gameModeHolder);
    }

    public void startGame() {
        this.scoreContinousTimes = 0;
        this.startClickedGreen = 0;
        this.currentTimeInMilliSec = this.gameBoard.setting.getCurrentTime();
        this.currentUserScore = 0;
        this.lifes = 4;
        this.ended = false;
        if(this.gameMode.getCurrentGameMode() != this.gameBoard.setting.getGameMode()){
            this.changeGameMode(false);
        }
        String text = (this.gameMode.getCurrentGameMode()).replace("_", " ");
        this.gameModeText.setText(text);
        this.lifesText.setText(String.valueOf(lifes));
        this.gameBoard.soundManager.startBackgroundSound();
        this.gameHandler.start();
        this.startLifeTimeCounter();
    }

    public void stopGame() {
        this.gameBoard.soundManager.stopTicTocSound();
        this.gameBoard.soundManager.stopBackgroundSound();
        this.gameHandler.end();
        if(this.lifeTimer != null){
            this.lifeTimer.cancel();
            this.lifeTimer = null;
        }
    }

    public void actionHandler(View button) {
        this.onBoardNotification.setX((button.getX() + (button.getWidth() / 2)) - 25);
        this.onBoardNotification.setY(button.getY());
        switch (button.getTag(R.id.Tag).toString()) {
            case "good": this.good(button);
                break;
            case "evil": this.evil(button);
                break;
            case "meh": this.meh(button);
                break;
        }

    }

    private void changeGameBoard() {
        this.gameBoard.getRenderEngine().renderButton(shape);
    }

    private void changeGameMode(boolean playSound) {
        this.gameMode.change();
        if(playSound){
            this.gameBoard.soundManager.playSound(R.raw.mod_change);
        }
        String gameModeText = this.gameMode.getCurrentGameMode();
        this.gameBoard.setting.setGameMode(gameModeText);
        String text = gameModeText.replace("_", " ");
        this.gameModeText.setText(text);
        this.setGameModeIcon();
        this.centerNotification(text, R.color.white);
    }

    private void changeGameShape() {
        this.shape.changeShape();
    }

    private void changeScore(int value) {
        this.currentScoreRate = value;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void claimUtility() {
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
                bombAction(image.getTag().toString());
            }

             @Override
             public void onTimeClick(ImageView image) { addTime(); }

             @Override
             public void onDiceClick(ImageView image) {
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
             }

         });
    }

    private void giftAction(){
        Random rand = new Random();
        switch (rand.nextInt(2)) {
            case 0: int score = rand.nextInt(100)+1;
                    this.scoreAdded(score);
                    this.gameBoard.soundManager.playSound(R.raw.coins);
                    this.centerNotification("+ "+score, R.color.white);
                break;
            case 1: this.addLife(1);
                break;
        }
    }

    private void bombAction(String tag){
        final String imageTag = tag.replace("_", " ");
        Random rand = new Random();
        int totalSeconds = rand.nextInt(10000);
        if(totalSeconds < 5000){
            totalSeconds = 5000;
        }
        int interval = 1000;

        centerNotification(imageTag+" Bomb", R.color.white);

        if(imageTag.equals("Green") || imageTag.equals("Gray") || imageTag.equals("Red")){
            this.gameBoard.getRenderEngine().changeDefaultColor(shape, imageTag);
        }else if(imageTag.equals("Speed_Up")){
            gameHandler.speedUpGameSpeed();
        }else{
            gameHandler.slowUpGameSpeed();
        }
        new CountDownTimer(totalSeconds, interval) {
            boolean isStarted = false;

            @Override
            public void onTick(long l) {
                timerText.setTextColor(context.getResources().getColor(R.color.red));
                if(!isStarted){
                    gameBoard.soundManager.startBombSound();
                }
                timerText.setText("00:0"+String.valueOf(Math.round(l/1000)));
                isStarted = true;
            }

            @Override
            public void onFinish() {
                if(imageTag.equals("Green") || imageTag.equals("Gray") || imageTag.equals("Red")) {
                    gameBoard.getRenderEngine().resetDefaultColor(shape);
                }else{
                    gameHandler.unlockGameSpeed();
                }
                timerText.setTextColor(context.getResources().getColor(R.color.red));
                timerText.setText("00:00");
                isStarted = false;
                gameBoard.soundManager.stopBombSound();
            }
        }.start();
    }

    private void good(View button) {
        this.onBoardNotification.setColor(R.color.darkGreen);
        int[] colors ={0XFF0a9c30,0XFF077123,0XFF0a9c30,0XFF077123,0XFF0a9c30,0XFF077123};
        this.animate(button,colors,16);
    }

    private void evil(View button) {
        this.onBoardNotification.setColor(R.color.darkRed);
        int[] colors ={0XFF7a0a0a,0XFFCE5A20,0XFF7a0a0a,0XFFB64F4C,0XFF7a0a0a,0XFFB46260};
        this.animate(button,colors,16);
    }

    private void meh(View button) {
        this.onBoardNotification.setColor(R.color.darkGrey);
        int[] colors ={0XFF2e4053,0XFF1b2530,0XFF2e4053, 0XFF1b2530, 0XFF2e4053,0XFF1b2530};
        this.animate(button,colors,16);
    }

    private void showReplayDialog() {
        if(!gameBoard.backClicked){
            ended = true;
            final Dialog dialog = new Dialog(context);

            dialog.setCancelable(false);
            dialog.setContentView(R.layout.replay_dialog);

            TextView score = (TextView) dialog.findViewById(R.id.dialog_score);
            score.setText(String.valueOf(gameBoard.setting.getScore()));

            Button again = (Button) dialog.findViewById(R.id.btn_replay);
            Button no = (Button) dialog.findViewById(R.id.btn_no);

            again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scoreNumber = 0;
                    TextView score = (TextView) ((Activity)context).findViewById(R.id.score);
                    score.setText(String.valueOf(scoreNumber));
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

    private void playTicToc(){
        this.gameBoard.soundManager.startTicTocSound();
    }

    private void stopTicToc(){
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
                gameMode.execute(button.getTag(R.id.Tag).toString());
            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }

    private void updateScoreText(int score) {
        this.gameBoard.setting.updateScore(score);
        this.scoreText.setText(String.valueOf(Numbers.format(score)));
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
            this.lifesText.setText(String.valueOf(lifes));
            this.gameBoard.soundManager.playSound(R.raw.life_lost);
        }
        this.centerNotification("-1 life", R.color.white);
        if(lifes == 0) {
            this.gameBoard.soundManager.playSound(R.raw.over);
            this.lifeTimer.cancel();
            this.stopGame();
            this.gameBoard.setting.setGameMode("");
            this.showReplayDialog();
        }
    }

    private void reduceScore(int score) {
        this.currentUserScore -= score;
        if(this.currentUserScore < 0){
            this.currentUserScore = 0;
        }
        this.updateScoreText(this.currentUserScore);
        this.centerNotification("-"+score, R.color.white);
    }

    private void scoreAdded(int score) {
        this.currentUserScore += score;
        this.gameMode.setUserScore(this.currentUserScore);
        this.updateScoreText(currentUserScore);
        long lastClickGreen = System.currentTimeMillis();
        double firstAndLastClickDiff = (lastClickGreen - this.startClickedGreen) / 1000.0;
        if (this.scoreContinousTimes == 3 && firstAndLastClickDiff <= 10.0 ) {
            this.scoreContinousTimes = 0;
            this.startClickedGreen = 0;
            this.gameBoard.soundManager.playSound(R.raw.sweet);
            this.centerNotification("Strike", R.color.white);
        }else if(firstAndLastClickDiff > 10.0){
            this.scoreContinousTimes = 0;
            this.startClickedGreen = 0;
        }
    }

    private void addLife(int n) {
        this.lifes+=n;
        this.lifesText.setText(String.valueOf(lifes));
        this.gameBoard.soundManager.playSound(R.raw.life_added);
        this.centerNotification("+1 Life", R.color.white);
    }

    private void centerNotification(String msg, int color){
        this.onBoardNotification.setX((this.gameBoard.getRenderEngine().getScreenWidth()/2));
        this.onBoardNotification.setY(this.gameBoard.getRenderEngine().getScreenHeight()/2);
        this.onBoardNotification.setColor(color);
        this.onBoardNotification.notify(msg, 1000, 30, true);
    }

    private void startLifeTimeCounter() {
        int interval = 1000;
        if(this.currentTimeInMilliSec == 0){
            Random rand = new Random();
            int minutes = rand.nextInt(4);
            if(minutes < 1){
                minutes = 1;
            }
            long minutesInMilliseconds = minutes * 60000;
            this.timerText.setText(String.format("%s:00", String.valueOf(minutes)));
            this.startTimeCounter(minutesInMilliseconds, interval);
        }else{
            this.startTimeCounter(this.currentTimeInMilliSec, interval);
        }
    }

    private void startTimeCounter(long milliseconds, long interval){
        if(this.lifeTimer != null){
            this.lifeTimer.cancel();
        }

        if(gameEnding){
            this.stopTicToc();
            this.gameEnding = false;
        }

        this.lifeTimer = new CountDownTimer(milliseconds, interval) {
            boolean isStarted = false;
            @Override
            public void onTick(long l) {
                currentTimeInMilliSec = l;
                gameBoard.setting.setCurrentTime(currentTimeInMilliSec);
                long minutes = Math.round(l / 60000);
                long seconds = Math.round((l % 60000) / 1000);
                String time =  minutes + ":" + (seconds < 10 ? '0' : "") + seconds;
                timerText.setText(time);
                if(seconds <= 5 && minutes == 0){
                    gameEnding = true;
                    timerText.setTextColor(context.getResources().getColor(R.color.red));
                    if(!isStarted ){
                        playTicToc();
                        isStarted = true;
                    }
                }else{
                    timerText.setTextColor(context.getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onFinish() {
                timerText.setText(R.string.default_time);
                stopGame();
                gameBoard.setting.setCurrentTime(0);
                showReplayDialog();
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
            public void changeGameShape() {
                engine.changeGameShape();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void claimTimeUtility() {
                engine.utility.callTimeToFall();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void claimDiceUtility() {
                engine.utility.callDiceToFall();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void claimUtility() {
                engine.claimUtility();
            }

            @Override
            public void animateGameHolder() {
                engine.animateGameHolder();
            }
        };
    }

    private void addTime(){
        Random rand = new Random();
        int millisec = rand.nextInt(25000);
        if(millisec < 5000){
            millisec = 5000;
        }

        long totalMilliSec = this.currentTimeInMilliSec + millisec;
        int sec = millisec / 1000;
        centerNotification("+ "+sec+" Sec", R.color.white);
        this.startTimeCounter(totalMilliSec, 1000);
        this.gameBoard.soundManager.playSound(R.raw.time);
    }

    private void animateGameHolder(){
        gameModeText.setTextColor(context.getResources().getColor(R.color.white));

        setGameModeHolderBackground(R.drawable.game_mode_transition);
        AnimationDrawable animate = (AnimationDrawable)gameModeHolder.getBackground();

        animate.setCallback(new AnimationDrawableCallback(animate, gameModeHolder) {
            @Override
            public void onAnimationComplete() {
                gameModeText.setTextColor(context.getResources().getColor(R.color.black));
                setGameModeHolderBackground(R.drawable.mode_layout);
                changeGameMode(true);
                stopTicToc();
            }
        });
        playTicToc();
        this.gameModeText.setText("Picking!");
        this.gameModeIcon.setImageResource(R.drawable.loading);
        animate.start();
    }

    private void setGameModeHolderBackground(int res){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            gameModeHolder.setBackgroundResource(res);
        }else{
            gameModeHolder.setBackground(this.context.getResources().getDrawable(res));
        }
    }

    private void setGameModeIcon(){
        switch (this.gameMode.getCurrentGameMode()){
            case "Easy": this.gameModeIcon.setImageResource(R.drawable.easy);
            break;
            case "Sudden_Death": this.gameModeIcon.setImageResource(R.drawable.skull);
            break;
            case "No_Score": this.gameModeIcon.setImageResource(R.drawable.noscore);
            break;
            case "Double_Tap": this.gameModeIcon.setImageResource(R.drawable.doubletab);
            break;
        }
    }
}
