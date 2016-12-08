package com.quarklab.squarebash;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.quarklab.squarebash.core.logic.GameEngine;
import com.quarklab.squarebash.core.multimedia.SoundManager;
import com.quarklab.squarebash.core.preference.Setting;
import com.quarklab.squarebash.core.visualization.RenderEngine;


public class GameBoard extends Activity {
    public  SoundManager soundManager;
    private RenderEngine renderEngine;
    private GameEngine gameEngine;
    public Setting setting;
    private AdView mAdView;

    public boolean backClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setting = new Setting(this);
        this.renderEngine = new RenderEngine(this);
        this.soundManager = new SoundManager(this);
        this.gameEngine = new GameEngine(this);
        this.renderEngine.render(R.id.GameBoard);
        this.backClicked = false;

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5665008338785349~2646543711");

        this.mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.mAdView.loadAd(adRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.backClicked = true;
        if(this.mAdView != null){
            this.mAdView.destroy();
        }
        finish();
    }
    public void onResume() {
        super.onResume();
        if(!this.gameEngine.isEnded()){
            this.gameEngine.startGame();
            this.soundManager.startBackgroundSound();
        }
        if(this.mAdView != null){
            this.mAdView.resume();
        }
    }

    public void onPause() {
        super.onPause();
        if(this.mAdView != null){
            this.mAdView.pause();
        }
        this.gameEngine.stopGame();
        this.soundManager.stopBackgroundSound();
    }

    public GameEngine getGameEngine(){
        return this.gameEngine;
    }
}
