package com.quarklab.volo.core.modes;

/**
 * Created by rudy on 2/18/17.
 */
public interface GameModeListener {

    void nothing();

    void onScoreAdded(int score);
    void onScoreReduced(int score);

    void onLifeLost();
    void onLifeAdded(int n);
}
