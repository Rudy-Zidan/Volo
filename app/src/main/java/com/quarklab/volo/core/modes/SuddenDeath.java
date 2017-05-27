package com.quarklab.volo.core.modes;

/**
 * Created by rudy on 2/16/17.
 */
class SuddenDeath extends GameMode{

    public SuddenDeath(GameModeListener gmListener) {
        super(gmListener);
    }

    public void execute(buttons tag) {
        switch (tag) {
            case GOOD: endLife();
                break;
            case EVIL: addScore(0, 0);
                break;
            case MEH: nothing();
                break;
        }
    }
}
