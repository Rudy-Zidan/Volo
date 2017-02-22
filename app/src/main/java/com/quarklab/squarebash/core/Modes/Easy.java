package com.quarklab.squarebash.core.Modes;


/**
 * Created by rudy on 2/16/17.
 */
class Easy extends GameMode {

    public Easy(GameModeListener gmListener) {
        super(gmListener);
    }

    public void execute(buttons tag) {
        switch (tag) {
            case GOOD: addScore(0);
                break;
            case EVIL: endLife();
                break;
            case MEH: reduceScore(0);
                break;
        }
    }
}
