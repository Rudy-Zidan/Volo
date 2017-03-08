package com.quarklab.volo.core.visualization;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;

import java.util.Random;

/**
 * Created by rudy on 6/20/16.
 */
public class PhoneScreen {
    private DisplayMetrics metrics;

    public PhoneScreen(Context context){
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        this.metrics = new DisplayMetrics();
        display.getMetrics(this.metrics);
    }

    public int getHeightPX(){
        return this.metrics.heightPixels - (int)(this.metrics.heightPixels * 0.05);
    }

    public int getWidthPX(){
        return this.metrics.widthPixels  - (int)(this.metrics.widthPixels * 0.15);
    }

    public int convertSpToPixels(float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.metrics);
        return px;
    }

    public float getRandomX() {
        Random rand = new Random();
        return rand.nextInt(this.getWidthPX())+50;
    }

    public float getRandomY() {
        Random rand = new Random();
        float y = rand.nextInt(this.getHeightPX())+1;
        int marginTop = this.convertSpToPixels(25);

        if( y < (marginTop*2)) {
            y += marginTop;
        }
        return y;
    }

    public int getSize() {
        int total = (this.getWidthPX()+this.getHeightPX())/(3*4);
        Random rand = new Random();
        return rand.nextInt(total)+100;
    }
}
