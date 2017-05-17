package com.quarklab.volo.core.logic;

/**
 * Created by rudy on 5/5/17.
 */

public interface GameEngineListener {
    void changeGameBoard();
    void changeGameScore(int score);
    void changeGameShape();
    void claimTimeUtility();
    void claimDiceUtility();
    void claimUtility();
    void animateGameHolder();
}
