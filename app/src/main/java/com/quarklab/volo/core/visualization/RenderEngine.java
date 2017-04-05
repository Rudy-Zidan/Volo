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
import com.quarklab.volo.core.shapes.Shape;

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

    public int getScreenWidth(){
       return this.screen.getNormalWidthPX();
    }

    public int getScreenHeight(){
        return this.screen.getNormalHeightPX();
    }

    public void renderButton(Shape shape) {

        if(this.frameLayout.indexOfChild(this.button) > -1) {
            this.frameLayout.removeView(this.button);
        }

        this.button = new Button(this.context);

        this.button.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.button.setStateListAnimator(null);
        }

        this.setButtonLayout();

        this.buttonColors(shape);

        this.buttonAction.execute(this.context,this.button);

        this.frameLayout.addView(this.button);
//        this.button.animate()
//                    .rotation(360)
//                    .setDuration(500)
//                    .start();
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

    private void buttonColors(Shape shape) {
        switch (this.rand.nextInt(4)){
            case 0:
                this.button.setTag("good");
                this.button.setBackgroundResource(shape.getTemplate(this.button.getTag().toString()));
                break;
            case 1:
                this.button.setTag("evil");
                this.button.setBackgroundResource(shape.getTemplate(this.button.getTag().toString()));
                break;
            case 2:
                this.button.setTag("meh");
                this.button.setBackgroundResource(shape.getTemplate(this.button.getTag().toString()));
                break;
            case 3:
                this.randomTrigger--;
                if(randomTrigger <= 0) {
                    this.button.setTag("oval_random");
                    this.button.setBackgroundResource(shape.getTemplate(this.button.getTag().toString()));
                    this.setRandomTrigger();
                }else{
                    this.button.setTag("good");
                    this.button.setBackgroundResource(shape.getTemplate(this.button.getTag().toString()));
                }
                break;
        }
    }

    private void setRandomTrigger() {
        this.randomTrigger = this.rand.nextInt(20)+10;
    }
}
