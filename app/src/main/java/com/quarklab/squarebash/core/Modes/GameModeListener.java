package com.quarklab.squarebash.core.Modes;

/**
 * Created by rudy on 2/18/17.
 */
public interface GameModeListener {

    void nothing();

    void onScoreAdded(int score);
    void onScoreReduced(int score);
    void onLifeLost();
}
