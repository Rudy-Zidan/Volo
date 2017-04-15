package com.quarklab.volo.core.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.FrameLayout;

import com.quarklab.volo.R;
import com.quarklab.volo.core.visualization.PhoneScreen;

import java.util.Random;

/**
 * Created by rudy on 4/15/17.
 */

public class Utility {
    protected Context context;
    protected FrameLayout layout;
    protected PhoneScreen screen;
    private enum types {
        GIFT(0), BOMB(1);
        private final int type;
        types(int type) {this.type = type;}
        public int getValue() {return this.type;}
    }

    public Utility(Context context){
        this.context = context;
        this.layout = (FrameLayout)((Activity)this.context).findViewById(R.id.gameBoardLayout);
        this.screen = new PhoneScreen(this.context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void fallOneDown(){
        Random rand = new Random();
        int giftOrBomb = rand.nextInt(2);
        if(giftOrBomb == types.GIFT.getValue()) {
            new Gift(this.context).renderGift();
        }else{
            new Bomb(this.context).renderBomb();
        }
    }
}
