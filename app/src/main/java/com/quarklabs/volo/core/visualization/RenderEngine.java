package com.quarklabs.volo.core.visualization;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.quarklabs.volo.R;
import com.quarklabs.volo.core.logic.ButtonAction;
import com.quarklabs.volo.core.shapes.Shape;

import java.util.Random;

/**
 * Created by rudy on 6/20/16.
 */
public class RenderEngine {
    private Context context;
    private Activity activity;
    private FrameLayout frameLayout;
    private Button button;
    private ButtonAction buttonAction;
    private Random rand;
    private int defaultColor;

    private PhoneScreen screen;

    public RenderEngine(Context context){
        this.context = context;
        this.setUpActivityScreen();
        this.rand = new Random();
        this.defaultColor = -1;
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

    public int getScreenWidth(){
       return this.screen.getNormalWidthPX();
    }

    public int getScreenHeight(){
        return this.screen.getNormalHeightPX();
    }

    public void changeDefaultColor(Shape shape, String tag){
        this.defaultColor = this.getTagIndex(tag);
        this.buttonColors(shape);
    }

    public void resetDefaultColor(Shape shape){
        this.defaultColor = -1;
        this.buttonColors(shape);
    }

    public void renderButton(Shape shape) {

        if(this.frameLayout.indexOfChild(this.button) > -1) {
            this.frameLayout.removeView(this.button);
            this.button = null;
        }

        this.button = new Button(this.context);
        this.button.setSoundEffectsEnabled(false);

        this.button.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.button.setStateListAnimator(null);
        }

        this.setButtonLayout();

        this.buttonColors(shape);

        this.buttonAction.execute(this.context,this.button);

        this.frameLayout.addView(this.button);
    }

    private void setButtonLayout() {

        double x = this.screen.getRandomX();
        double y = this.screen.getRandomY();

        float percantage = ((float)(rand.nextInt(20)+1) / 100);
        int width = this.screen.getWidthSize(percantage);
        int height = this.screen.getHeightSize(percantage);

        this.button.setWidth(width);
        this.button.setHeight(height);

        double totalHeight = (y + height);
        if ( totalHeight > this.screen.getHeightPX()){
            y -= (totalHeight - this.screen.getHeightPX())+this.screen.marginHeight();
        }else if(totalHeight == this.screen.getHeightPX()){
            y -= this.screen.marginHeight();
        }else if(totalHeight < this.screen.getHeightPX() && (this.screen.getHeightPX() - totalHeight) <= this.screen.marginHeight()) {
            y -= this.screen.marginHeight();
        }

        this.button.setY((float)y);

        double totalWidth = (x + width);
        if ( totalWidth > this.screen.getWidthPX()){
            x -= (totalWidth - this.screen.getWidthPX())+this.screen.marginWidth();
        }else if(totalWidth == this.screen.getWidthPX()){
            x -= this.screen.marginWidth();
        }else if(totalWidth < this.screen.getWidthPX() && (this.screen.getWidthPX() - totalWidth) <= this.screen.marginWidth()) {
            x -= this.screen.marginWidth();
        }
        this.button.setX((float)x);
    }

    private void buttonColors(Shape shape) {
        int target = this.rand.nextInt(3);
        if(this.defaultColor > -1){
            target = this.defaultColor;
        }
        switch (target){
            case 0:

                this.button.setTag(R.id.Tag,"good");
                this.button.setBackgroundResource(shape.getTemplate(this.button.getTag(R.id.Tag).toString()));
                break;
            case 1:
                this.button.setTag(R.id.Tag,"evil");
                this.button.setBackgroundResource(shape.getTemplate(this.button.getTag(R.id.Tag).toString()));
                break;
            case 2:
                this.button.setTag(R.id.Tag,"meh");
                this.button.setBackgroundResource(shape.getTemplate(this.button.getTag(R.id.Tag).toString()));
                break;
        }
        this.button.setTag(R.id.Value, target);
    }

    private int getTagIndex(String tag){
        int index = -1;
        switch (tag){
            case "Green": index = 0;
                break;
            case "Red":   index = 1;
                break;
            case "Gray":  index = 2;
                break;
        }
        return index;
    }
}
