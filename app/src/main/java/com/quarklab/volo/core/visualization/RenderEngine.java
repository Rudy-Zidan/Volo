package com.quarklab.volo.core.visualization;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.quarklab.volo.R;
import com.quarklab.volo.core.adapter.ButtonAdapter;
import com.quarklab.volo.core.logic.ButtonAction;

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
    private int randomTrigger;
    private Random rand;

    private PhoneScreen screen;

    public RenderEngine(Context context){
        this.context = context;
        this.setUpActivityScreen();
        this.rand = new Random();
        this.setRandomTrigger();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.button.setStateListAnimator(null);
            }
        }else if(this.frameLayout.indexOfChild(this.button) > -1) {
            this.frameLayout.removeView(this.button);
        }

        this.setButtonLayout();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.button.setBackground( this.context.getResources().getDrawable(R.drawable.green_button));
        } else {
            this.button.setBackgroundDrawable( this.context.getResources().getDrawable(R.drawable.green_button) );
        }

        this.buttonColors();

        this.buttonAction.execute(this.context,this.button);

        this.frameLayout.addView(this.button);
    }

    private void setButtonLayout() {

        float x = this.screen.getRandomX();
        float y = this.screen.getRandomY();

        int blockSize = this.screen.getSize();

        this.button.setWidth(blockSize);
        this.button.setHeight(blockSize);

        float totalHeight = (y + blockSize);
        if ( totalHeight > this.screen.getHeightPX()){
            y -= (totalHeight - this.screen.getHeightPX());
        }

        this.button.setY(y);

        float totalWidth = (x + blockSize);
        if ( totalWidth > this.screen.getWidthPX()){
            x -= (totalWidth - this.screen.getWidthPX());
        }

        this.button.setX(x);
    }

    private void buttonColors() {
        switch (this.rand.nextInt(4)){
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
            case 3:
                this.randomTrigger--;
                if(randomTrigger <= 0) {
                    this.button.setBackgroundResource(R.drawable.random);
                    this.button.setTag("random");
                    this.setRandomTrigger();
                }
                break;
        }
    }

    private void setRandomTrigger() {
        this.randomTrigger = this.rand.nextInt(20)+10;
    }
}
