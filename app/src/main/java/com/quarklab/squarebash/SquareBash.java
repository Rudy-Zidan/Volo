package com.quarklab.squarebash;

import android.app.Activity;
import android.os.Bundle;

import com.quarklab.squarebash.core.logic.GameEngine;
import com.quarklab.squarebash.core.multimedia.SoundManager;
import com.quarklab.squarebash.core.visualization.RenderEngine;


public class SquareBash extends Activity {
    public SoundManager soundManager;
    private RenderEngine renderEngine;
    private GameEngine gameEngine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.renderEngine = new RenderEngine(this);
        this.soundManager = new SoundManager(this);
        this.gameEngine = new GameEngine(this);
        this.renderEngine.render(R.id.GameBoard);
    }

    public void onResume() {
        super.onResume();
        this.gameEngine.startGame();
        this.soundManager.startBackgroundSound();
    }

    public void onPause() {
        super.onPause();
        this.gameEngine.stopGame();
        this.soundManager.stopBackgroundSound();
    }

    public GameEngine getGameEngine(){
        return this.gameEngine;
    }


}
