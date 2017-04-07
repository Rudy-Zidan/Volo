package com.quarklab.volo.core.logic;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

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
import com.quarklab.volo.core.TTS.Speaker;
import com.quarklab.volo.core.modes.GameMode;
import com.quarklab.volo.core.modes.GameModeListener;
import com.quarklab.volo.core.notification.OnBoardNotification;
import com.quarklab.volo.core.shapes.Shape;
//import com.quarklab.volo.core.TTS.Speaker;

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
    private static int currentScore;
    private static int lifes;
    private static GameHandler gameHandler;
    private static boolean ended;
    private SmallBang smallBang;
    private static Toast toast;
    private static View toastAppear;
    private static Speaker speaker;
    private int scoreContinousTimes;

    private TextView scoreText;
    private static TextView lifesText;

    private static OnBoardNotification onBoardNotification;

    private static Shape shape;

    public GameEngine(Context context) {
        GameEngine.context = context;
        gameBoard = (GameBoard) GameEngine.context;
        gameHandler = new GameHandler();

        this.initGameMode();

        ended = false;
        smallBang = SmallBang.attach2Window((Activity) GameEngine.context);
        LayoutInflater li = gameBoard.getLayoutInflater();
        toastAppear = li.inflate(R.layout.toast, (LinearLayout) gameBoard.findViewById(R.id.custom_toast_layout_id));
        toast = new Toast(context);
        toast.setView(toastAppear);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,Gravity.CENTER,Gravity.CENTER);
        this.speaker = new Speaker(this.context);

        this.scoreText = (TextView) ((Activity) GameEngine.context).findViewById(R.id.score);

        lifesText = (TextView) ((Activity) GameEngine.context).findViewById(R.id.lifes);

        onBoardNotification = new OnBoardNotification(GameEngine.context);

        shape = new Shape();

        this.scoreContinousTimes = 0;
    }

    public static void startGame() {
        lifes = 4;
        lifesText.setText(""+lifes);
        currentScore = 0;
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
        gameBoard.soundManager.playSound(R.raw.mode_change);
        String text = gameMode.getCurrentGameMode();
        //speaker.speak(text);
        onBoardNotification.setX((gameBoard.getRenderEngine().getScreenWidth()/2));
        onBoardNotification.setY(gameBoard.getRenderEngine().getScreenHeight()/2);
        onBoardNotification.setColor(R.color.white);
        onBoardNotification.notify(text, 1500, 30, true);
    }

    public static void changeGameShape() {
        shape.changeShape();
    }

    public static void changeScore(int value) {
        currentScore = value;
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
                //speaker.shutdown();
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
                        case 0: scoreAdded(rand.nextInt(20)+10);
                            break;
                        case 1: reduceScore(rand.nextInt(20)+10);
                            break;
                        case 2: reduceLife();
                            break;
                        case 3: addLife(1);
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
        gameMode = new GameMode(new GameModeListener() {
            @Override
            public void nothing() {
                gameBoard.soundManager.playSound(R.raw.gameover);
            }

            @Override
            public void onScoreAdded(int score) {
                scoreContinousTimes+=1;
                scoreAdded(score);
            }

            @Override
            public void onScoreReduced(int score) {
                scoreContinousTimes = 0;
                reduceScore(score);
            }

            @Override
            public void onLifeLost() {
                reduceLife();
            }
        });
    }

    private void reduceLife() {
        if(lifes > 0) {
            lifes--;
            lifesText.setText(""+lifes);
        }
        if(lifes == 1) {
            gameBoard.soundManager.startTicTocSound();
        }
        if(lifes == 0) {
            gameBoard.soundManager.playSound(R.raw.lose);
            stopGame();
        }
    }

    private void reduceScore(int score) {
        currentScore -= score;
        if(currentScore < 0){
            currentScore = 0;
        }
        updateScoreText(currentScore);
        onBoardNotification.notify("- "+score, 500, 18, false);
    }

    private void scoreAdded(int score) {
        currentScore += score;
        gameBoard.soundManager.playSound(R.raw.score);
        updateScoreText(currentScore);
        onBoardNotification.notify("+ "+score, 500, 18, false);
        if (this.scoreContinousTimes == 3) {
            this.scoreContinousTimes = 0;
            this.speaker.speak("Fantastic", 0.5f);
        }
    }

    private void addLife(int n) {
        lifes+=n;
        lifesText.setText(""+lifes);
        gameBoard.soundManager.stopTicTocSound();
    }
}
