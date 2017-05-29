package com.quarklab.volo.core.modes;

/**
 * Created by rudy on 2/16/17.
 */
class DoubleTab extends GameMode{

    public DoubleTab(GameModeListener gmListener, int score) {
        super(gmListener, score);
    }

    public void execute(buttons tag) {
        switch (tag) {
            case GOOD: addScore(0, 2);
                break;
            case EVIL: addScore(0, 2);
                break;
            case MEH: reduceScore(this.score, 2);
                break;
        }
    }
}
