package com.quarklab.volo.core.logic;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quarklab.volo.R;
import com.quarklab.volo.GameBoard;
import com.quarklab.volo.core.Modes.GameMode;
import com.quarklab.volo.core.Modes.GameModeListener;
//import com.quarklab.volo.core.TTS.Speaker;

import java.util.Locale;
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
    private static int toastSpeed;
    private static boolean ended;
    private SmallBang smallBang;
    private static Toast toast;
    private static View toastAppear;
    private FrameLayout frameLayout;

    private float buttonX;
    private float buttonY;
    private String color;
    //private static Speaker speaker;

    private ImageView heartIcon;

    private TextView scoreText;
    private TextView lifesText;

    private Typeface typeface;

    public GameEngine(Context context) {
        this.context = context;
        this.gameBoard = (GameBoard) this.context;
        this.currentScore = 0;
        this.lifes = 4;
        this.gameHandler = new GameHandler();
        this.toastSpeed = 100;

        this.initGameMode();

        ended = false;
        smallBang = SmallBang.attach2Window((Activity)this.context);
        LayoutInflater li = gameBoard.getLayoutInflater();
        toastAppear = li.inflate(R.layout.toast, (LinearLayout) gameBoard.findViewById(R.id.custom_toast_layout_id));
        toast = new Toast(context);
        toast.setView(toastAppear);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,Gravity.CENTER,Gravity.CENTER);
        //this.speaker = new Speaker(this.context);

        this.scoreText = (TextView) ((Activity)this.context).findViewById(R.id.score);

        this.lifesText = (TextView) ((Activity)this.context).findViewById(R.id.lifes);
        this.heartIcon = (ImageView) ((Activity)this.context).findViewById(R.id.heart);

        this.frameLayout = (FrameLayout)((Activity)this.context).findViewById(R.id.gameBoardLayout);
    }

    public static void startGame() {
        gameHandler.start();
    }

    public void stopGame() {
        this.gameHandler.end();
    }

    public void actionHandler(View button) {
        this.buttonX = (button.getX() + (button.getWidth() / 2)) - 25;
        this.buttonY = button.getY();
        switch (button.getTag().toString()) {
            case "good": good(button);
                break;
            case "evil": evil(button);
                break;
            case "meh": meh(button);
                break;
            case "random": random(button);
                break;
        }

    }

    public static void changeGameBoard() {
        gameBoard.getRenderEngine().renderButton();
    }

    public static void changeGameMode() {
        gameMode.change();
        String text = gameMode.getCurrentGameMode();
        //speaker.speak(text);
        showMessage(text);
    }

    public static void changeScore(int value) {
        currentScore = value;
    }

    private void good(View button) {
        this.color = "#579B00";
        int[] colors ={0XFF86AC41,0XFF99AC41,0XFF22AC41,0XFF00AC41,0XFF20AC50,0XFF82AC20};
        this.animate(button,colors,16);
    }

    private void evil(View button) {
        this.color = "#FF3232";
        int[] colors ={0XFFCE5A57,0XFFCE5A20,0XFFFF726E,0XFFB64F4C,0XFFB62B27,0XFFB46260};
        this.animate(button,colors,16);
    }

    private void meh(View button) {
        this.color = "#FFE13C";
        int[] colors ={0XFFE1B16A,0XFFE1B103,0XFFFDC97A, 0XFFFFB33F, 0XFFF0A93E,0XFFFF9A01};
        this.animate(button,colors,16);
    }

    private void random(View button) {
        this.color = "#876DD1";
        int[] colors ={0XFFFF5A00,0XFFFF7B32,0XFFFF8B4C,0XFFF25500, 0XFFF06215,0XFFDD6B2D};
        this.animate(button,colors,16);
    }

    private static void showMessage(String msg) {
        TextView text = (TextView) toastAppear.findViewById(R.id.toast_text);
        text.setText(msg);
        toast.show();
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

    public static void showReplayDialog() {
        if(!gameBoard.backClicked){
            //resetButtons();
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

    private void animate(final View button, int[] colors, int dots) {
        int extraSpace = 50;
        int radius = button.getWidth()/2 + extraSpace;
        smallBang.setColors(colors);
        smallBang.setDotNumber(dots);
        smallBang.bang(button,radius,new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                String tag = button.getTag().toString();
                if(tag.equals("random")) {
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

    private static void resetButtons() {
        GridView gameBoard = (GridView) ((Activity)context).findViewById(R.id.GameBoard);
        int count = gameBoard.getChildCount();
        for(int i = 0 ; i <count ; i++){
            Button child = (Button)gameBoard.getChildAt(i);
            child.setBackgroundResource(R.drawable.block);
            child.setTag("");
        }
    }

    private void updateScoreText(int score) {
        this.gameBoard.setting.updateScore(score);
        this.scoreText.setText(""+Numbers.format(score));
    }

    private void initGameMode() {
        this.gameMode = new GameMode(new GameModeListener() {
            @Override
            public void nothing() {
                gameBoard.soundManager.playSound(R.raw.gameover);
            }

            @Override
            public void onScoreAdded(int score) {
                scoreAdded(score);
            }

            @Override
            public void onScoreReduced(int score) {
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
            animateHeartIcon();
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
        NotifyScoreOnBoard("- "+score);
    }

    private void scoreAdded(int score) {
        currentScore += score;
        gameBoard.soundManager.playSound(R.raw.score);
        updateScoreText(currentScore);
        NotifyScoreOnBoard("+ "+score);
    }

    private void addLife(int n) {
        lifes+=n;
        lifesText.setText(""+lifes);
        animateHeartIcon();
        this.gameBoard.soundManager.stopTicTocSound();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void NotifyScoreOnBoard(String text){
        final TextView scoreHint = new TextView(this.context);
        this.frameLayout.addView(scoreHint);
        AssetManager am = this.context.getAssets();
        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "KBZipaDeeDooDah.ttf"));
        scoreHint.setTextColor(Color.parseColor(this.color));
        scoreHint.setX(this.buttonX);
        scoreHint.setY(this.buttonY);
        scoreHint.setText(text);
        scoreHint.setTypeface(typeface);
        scoreHint.animate()
                .alpha(0.f)
                .scaleX(1.f).scaleY(1.2f)
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        scoreHint.setAlpha(1.f);
                        scoreHint.setScaleX(1.f);
                        scoreHint.setScaleY(1.f);
                        frameLayout.removeView(scoreHint);
                    }
                })
                .start();

    }

}
