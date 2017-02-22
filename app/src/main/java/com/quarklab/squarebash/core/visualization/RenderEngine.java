package com.quarklab.squarebash.core.visualization;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.quarklab.squarebash.R;
import com.quarklab.squarebash.core.adapter.ButtonAdapter;
import com.quarklab.squarebash.core.logic.ButtonAction;

import java.util.Random;

/**
 * Created by rudy on 6/20/16.
 */
public class RenderEngine {
    private Context context;
    private Activity activity;
    private GridView grid;
    private FrameLayout frameLayout;
    private Button button;
    private ButtonAction buttonAction;

    private PhoneScreen screen;

    public RenderEngine(Context context){
        this.context = context;
        this.setUpActivityScreen();
    }
    private void setUpActivityScreen(){
        this.activity = ((Activity)this.context);
        this.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.activity.setContentView(R.layout.activity_game_board);
        this.frameLayout = (FrameLayout)((Activity)this.context).findViewById(R.id.gameBoardLayout);
        this.screen = new PhoneScreen(this.context);
        this.buttonAction = new ButtonAction();

    }
    public void render(int resource){
        this.grid = (GridView) ((Activity)context).findViewById (resource);
        this.grid.setAdapter(new ButtonAdapter(this.context));
    }

    public void renderButton() {
        if(this.button == null) {
            this.button = new Button(this.context);
            this.button.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        }

        this.setButtonLayout();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.button.setBackground( this.context.getResources().getDrawable(R.drawable.green_button));
        } else {
            this.button.setBackgroundDrawable( this.context.getResources().getDrawable(R.drawable.green_button) );
        }

        this.buttonColors();

        this.buttonAction.execute(this.context,this.button);

        if(this.frameLayout.indexOfChild(this.button) == -1) {
            this.frameLayout.addView(this.button);
        }
    }

    private void setButtonLayout() {

        float x = this.screen.getRandomX();
        float y = this.screen.getRandomY();

        this.button.setWidth(this.screen.getRandomWidth());
        this.button.setHeight(this.screen.getRandomHeight());

        float totalHeight = (y + this.button.getHeight());
        if ( totalHeight > this.screen.getHeightPX()){
            y -= (totalHeight - this.screen.getHeightPX());
        }

        this.button.setY(y);

        float totalWidth = (x+this.button.getWidth());
        if ( totalWidth > this.screen.getWidthPX()){
            x -= (totalWidth - this.screen.getWidthPX());
        }

        this.button.setX(x);
    }

    private void buttonColors() {
        Random r = new Random();
        switch (r.nextInt(3)){
            case 0:
                this.button.setBackgroundResource(R.drawable.green_button);
                this.button.setTag("good");
                break;
            case 1:
                this.button.setBackgroundResource(R.drawable.red_button);
                this.button.setTag("evil");
                break;
            case 2:
                this.button.setBackgroundResource(R.drawable.warm_button);
                this.button.setTag("meh");
                break;
        }
    }
}
