package com.quarklab.squarebash.core.visualization;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by rudy on 6/20/16.
 */
public class PhoneScreen {
    private Display display;
    private DisplayMetrics metrics;

    public PhoneScreen(Context context){
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        this.metrics = new DisplayMetrics();
        display.getMetrics(this.metrics);
    }

    public int getHeightPX(){
        return this.metrics.heightPixels;
    }

    public int getWidthPX(){
        return this.metrics.widthPixels;
    }
}
