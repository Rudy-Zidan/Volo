package com.quarklab.volo.core.Modes;

/**
 * Created by rudy on 2/16/17.
 */
class DoubleTab extends GameMode{

    public DoubleTab(GameModeListener gmListener) {
        super(gmListener);
    }

    public void execute(buttons tag) {
        switch (tag) {
            case GOOD: addScore(2);
                break;
            case EVIL: addScore(2);
                break;
            case MEH: reduceScore(2);
                break;
        }
    }
}
