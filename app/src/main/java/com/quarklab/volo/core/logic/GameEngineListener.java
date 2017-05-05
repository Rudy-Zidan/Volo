package com.quarklab.volo.core.logic;

/**
 * Created by rudy on 5/5/17.
 */

public interface GameEngineListener {
    void changeGameBoard();
    void changeGameScore(int score);
    void changeGameMode();
    void changeGameShape();
    void playTicTocSound();
    void stopTicTocSound();
    void claimUtility();
}
