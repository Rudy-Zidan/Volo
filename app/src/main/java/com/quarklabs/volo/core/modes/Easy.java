package com.quarklabs.volo.core.modes;


/**
 * Created by rudy on 2/16/17.
 */
class Easy extends GameMode {

    public Easy(GameModeListener gmListener, int score) {
        super(gmListener, score);
    }

    public void execute(buttons tag) {
        switch (tag) {
            case GOOD: addScore(0, 0);
                break;
            case EVIL: endLife();
                break;
            case MEH: reduceScore(this.score, 0);
                break;
        }
    }
}
