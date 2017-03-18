package com.quarklab.volo.core.Notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quarklab.volo.R;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by rudy on 3/18/17.
 */
public class OnBoardNotification {
    private float x;
    private float y;
    private int speed;
    private int textSize;
    private String color;
    private Context context;
    private Typeface typeface;
    private FrameLayout layout;
    private TextView notificationHolder;


    public OnBoardNotification(Context context){
        this.context = context;
        this.setHolder();
        this.setLayout();
        this.setTypeface();
    }
    public OnBoardNotification(Context context,float x, float y){
        this(context);
        this.x = x;
        this.y = y;
        this.color = "#FFF";
    }

    public void setX(float x){this.x = x;}

    public void setY(float y){this.y = y;}

    public void setColor(String color){this.color = color;}

    public void setSpeed(int speed){this.speed = speed;}


    public void notify(String message, String color){
        this.color = color;
        this.execute(message);
    }

    public void notify(String message, int speed, int textSize){
        this.speed = speed;
        this.textSize = textSize;
        this.execute(message);
    }

    private void setLayout(){
        this.layout = (FrameLayout)((Activity)this.context).findViewById(R.id.gameBoardLayout);
    }

    private void setTypeface(){
        AssetManager am = context.getAssets();
        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "KBZipaDeeDooDah.ttf"));
    }

    private void setHolder(){
        this.notificationHolder = new TextView(this.context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void execute(String message){
        this.layout.addView(notificationHolder);
        this.notificationHolder.setTextSize(this.textSize);
        this.notificationHolder.setShadowLayer(1.5f, 0, 0, Color.parseColor("#2F2F2F"));
        this.notificationHolder.setTextColor(Color.parseColor(this.color));
        this.notificationHolder.setX(this.x);
        this.notificationHolder.setY(this.y);
        this.notificationHolder.setText(message);
        this.notificationHolder.setTypeface(this.typeface);
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
                        layout.removeView(notificationHolder);
                    }
                })
                .start();
    }
}
