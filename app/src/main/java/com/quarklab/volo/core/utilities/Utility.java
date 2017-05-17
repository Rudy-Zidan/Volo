package com.quarklab.volo.core.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quarklab.volo.R;
import com.quarklab.volo.core.logic.GameEngine;
import com.quarklab.volo.core.visualization.PhoneScreen;

import java.util.Random;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;


/**
 * Created by rudy on 4/15/17.
 */

public class Utility {
    protected Context context;
    protected FrameLayout layout;
    protected PhoneScreen screen;
    protected UtilityListener listener;
    protected SmallBang smallBang;
    protected ImageView image;

    protected final int WIDTH = 200;
    protected final int HEIGHT = 200;
    private enum types {
        GIFT(0), BOMB(1);
        private final int type;
        types(int type) {this.type = type;}
        public int getValue() {return this.type;}
    }

    public Utility(Context context, UtilityListener listener){
        this.context = context;
        this.layout = (FrameLayout)((Activity)this.context).findViewById(R.id.gameBoardLayout);
        this.screen = new PhoneScreen(this.context);
        this.listener = listener;
        this.smallBang = SmallBang.attach2Window((Activity) this.context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void fallOneDown(){
        Random rand = new Random();
        int giftOrBomb = rand.nextInt(2);
//        if(giftOrBomb == types.GIFT.getValue()) {
//            new Gift(this.context, this.listener).renderGift();
//        }else{
            new Bomb(this.context, this.listener).renderBomb();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void callTimeToFall() {
        new Time(this.context, this.listener).renderTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void callDiceToFall() {
        new Dice(this.context, this.listener).renderDice();
    }

    protected void setLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.WIDTH, this.HEIGHT);
        this.image.setX(this.screen.getRandomX());
        this.image.setY(this.screen.convertSpToPixels(60));
        this.image.setLayoutParams(layoutParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void animate(){
        this.image.animate()
                .translationY(this.screen.getHeightPX())
                .setDuration(2500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        layout.removeView(image);
                    }
                });
    }
}
