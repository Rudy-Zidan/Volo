package com.quarklabs.volo.core.notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quarklabs.volo.R;

/**
 * Created by rudy on 3/18/17.
 */
public class OnBoardNotification {
    private float x;
    private float y;
    private int speed;
    private int textSize;
    private int color;

    private Context context;
    private FrameLayout layout;
    private TextView notificationHolder;


    public OnBoardNotification(Context context){
        this.context = context;
        this.setHolder();
        this.setLayout();
    }

    public OnBoardNotification(Context context,float x, float y){
        this(context);
        this.x = x;
        this.y = y;
        this.color = R.color.white;
    }

    public void setX(float x){this.x = x;}

    public void setY(float y){this.y = y;}

    public void setColor(int color){this.color = color;}

    public void setSpeed(int speed){this.speed = speed;}


    public void notify(String message, int color){
        this.color = color;
        this.execute(message, false);
    }

    public void notify(String message, int speed, int textSize, boolean center){
        this.speed = speed;
        this.textSize = textSize;
        this.execute(message, center);
    }

    private void setLayout(){
        this.layout = (FrameLayout)((Activity)this.context).findViewById(R.id.gameBoardLayout);
    }

    private void setHolder(){
        this.notificationHolder = new TextView(this.context);
    }


    private void execute(String message, boolean center){
        this.removeHolder();
        this.layout.addView(notificationHolder);
        this.setHolderProperties();
        this.notificationHolder.setText(message);
        this.setHolderCoordinates(center);
        this.animateHolder();
    }

    private void setHolderCoordinates(boolean center){
        if(center){
            this.notificationHolder.measure(0, 0);
            int width = this.notificationHolder.getMeasuredWidth();
            this.notificationHolder.setX(this.x - (width/2));
        }else{
            this.notificationHolder.setX(this.x);
        }
        this.notificationHolder.setY(this.y);
    }

    private void setHolderProperties(){
        this.notificationHolder.setTextSize(this.textSize);
        this.notificationHolder.setShadowLayer(1.5f, 0, 0, Color.parseColor("#2F2F2F"));
        this.notificationHolder.setTextColor(this.context.getResources().getColor(this.color));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void animateHolder(){
        this.notificationHolder.animate()
                .alpha(0.f)
                .scaleX(1.f).scaleY(1.2f)
                .setDuration(this.speed)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        notificationHolder.setAlpha(1.f);
                        notificationHolder.setScaleX(1.f);
                        notificationHolder.setScaleY(1.f);
                        removeHolder();
                    }
                })
                .start();
    }

    private void removeHolder(){
        if(this.layout.indexOfChild(notificationHolder) > -1){
            this.layout.removeView(notificationHolder);
        }
    }
}
