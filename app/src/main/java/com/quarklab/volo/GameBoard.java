package com.quarklab.volo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.quarklab.volo.core.logic.GameEngine;
import com.quarklab.volo.core.multimedia.SoundManager;
import com.quarklab.volo.core.preference.Setting;
import com.quarklab.volo.core.visualization.RenderEngine;

import java.util.Locale;


public class GameBoard extends Activity {
    public  SoundManager soundManager;
    private RenderEngine renderEngine;
    private GameEngine gameEngine;
    public Setting setting;

    public boolean backClicked;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setting = new Setting(this);
        this.renderEngine = new RenderEngine(this);
        this.soundManager = new SoundManager(this);
        this.gameEngine = new GameEngine(this);
        this.renderEngine.renderButton(this.gameEngine.getShape());
        this.backClicked = false;
        this.setFontToTextViews();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.backClicked = true;
        finish();
    }
    public void onResume() {
        super.onResume();
        if(!this.gameEngine.isEnded()){
            this.gameEngine.startGame();
        }
    }

    public void onPause() {
        super.onPause();
        this.gameEngine.stopGame();
        this.gameEngine = null;
        this.renderEngine = null;
        this.setting = null;
    }

    public GameEngine getGameEngine(){
        return this.gameEngine;
    }

    public RenderEngine getRenderEngine() {return this.renderEngine;}

    private void setFontToTextViews(){
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "KBZipaDeeDooDah.ttf"));

        TextView heart = (TextView)findViewById(R.id.lifes);
        heart.setTypeface(typeface);

        TextView score = (TextView)findViewById(R.id.score);
        score.setTypeface(typeface);

        TextView timer = (TextView)findViewById(R.id.timerText);
        timer.setTypeface(typeface);
    }
}
