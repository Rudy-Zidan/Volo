package com.quarklabs.volo.core.visualization;

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

    public int getNormalHeightPX(){
        return this.metrics.heightPixels;
    }

    public int getNormalWidthPX(){
        return this.metrics.widthPixels;
    }

    public int getHeightPX(){
        return this.metrics.heightPixels - (int)(this.metrics.heightPixels * 0.1);
    }

    public int getWidthPX(){
        return this.metrics.widthPixels  - (int)(this.metrics.widthPixels * 0.1);
    }

    public int convertSpToPixels(float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.metrics);
        return px;
    }

    public double getRandomX() {
        Random rand = new Random();
        double x = rand.nextInt(this.getWidthPX())+1;
        double margin = this.marginWidth();
        if( x < (margin)) {
            x += margin;
        }
        return x;
    }

    public float getRandomY() {
        Random rand = new Random();
        float y = rand.nextInt(this.getHeightPX())+1;
        float margin = this.marginHeight();

        if( y < (margin)) {
            y += margin;
        }
        return y;
    }

    public int getWidthSize(float percentage) {
        int total = (int)(this.getNormalWidthPX() * percentage);
        Random rand = new Random();
        int width = rand.nextInt(total);
        return width;
    }

    public int getHeightSize(float percentage) {
        int total = (int)(this.getNormalHeightPX() * percentage);
        Random rand = new Random();
        int height = rand.nextInt(total);
        return height;
    }

    public float marginHeight(){
        return (float)(this.metrics.heightPixels * 0.1);
    }

    public float marginWidth(){
        return (float)(this.metrics.widthPixels * 0.1);
    }

    public int getBlockSize(float percentage){
        return (int)((this.getHeightPX() + this.getWidthPX()) * percentage);
    }
}
