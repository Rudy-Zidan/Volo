package com.quarklabs.volo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.quarklabs.volo.core.logic.GameEngine;
import com.quarklabs.volo.core.multimedia.SoundManager;
import com.quarklabs.volo.core.preference.Setting;
import com.quarklabs.volo.core.visualization.RenderEngine;



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
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.backClicked = true;
        this.gameEngine.stopGame();
        Runtime.getRuntime().gc();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.setting.setCurrentTime(0);
        this.gameEngine = null;
        this.renderEngine = null;
        this.setting = null;
    }

    public GameEngine getGameEngine(){
        return this.gameEngine;
    }

    public RenderEngine getRenderEngine() {return this.renderEngine;}
}
