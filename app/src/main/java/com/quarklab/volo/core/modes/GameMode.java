package com.quarklab.volo.core.modes;

import java.util.Random;

/**
 * Created by rudy on 2/16/17.
 */
public class GameMode {
    private enum modes {Easy,SuddenDeath,NoScore,DoubleTap}
    protected enum buttons {GOOD, EVIL, MEH}
    private modes current;

    private int scoreRate;
    private int score;
    private int lifes;
    private Random rand;


    protected GameModeListener gmlistener;

    public GameMode(GameModeListener gmListener) {
        this.scoreRate = 10;
        this.score = 0;
        this.lifes = 4;
        this.current = modes.Easy;
        this.gmlistener = gmListener;
        this.rand = new Random();
    }

    public void change() {
        int random = this.rand.nextInt(10);
        if (random % 2 == 0) {
            if (!this.current.equals(modes.Easy)) {
                this.current = modes.Easy;
            }else{
                change();
            }
        } else {
            random = this.rand.nextInt(3) + 1;
            if (!this.current.equals(modes.values()[random])) {
                this.current = modes.values()[random];
            }else{
                change();
            }
        }
    }

    public void execute(String tag) {
        switch (this.current) {
            case Easy: getEasyMode().execute(getTag(tag));
                break;
            case SuddenDeath: getSuddenDeathMode().execute(getTag(tag));
                break;
            case NoScore:
                break;
            case DoubleTap: getDoubleTabMode().execute(getTag(tag));
                break;
        }
    }

    public String getCurrentGameMode() {
        return this.current.toString();
    }

    private Easy getEasyMode() {
        return new Easy(this.gmlistener);
    }

    private SuddenDeath getSuddenDeathMode() {
        return new SuddenDeath(this.gmlistener);
    }

    private DoubleTab getDoubleTabMode() {
        return new DoubleTab(this.gmlistener);
    }

    private buttons getTag(String buttonTag) {
        buttons tag = null;
        switch (buttonTag) {
            case "good": tag = buttons.GOOD;
                break;
            case "evil": tag =  buttons.EVIL;
                break;
            case "meh": tag =  buttons.MEH;
                break;
        }
        return tag;
    }

    private int calcScore(int power) {
        int score = this.scoreRate;
        if(power !=0){
            score =  power*score;
        }
        return score;
    }

    private void setLifes(int n) {
        this.lifes += n;
    }

    protected void endLife() {
        this.gmlistener.onLifeLost();
    }

    protected void addScore(int power) {
        this.gmlistener.onScoreAdded(this.calcScore(power));
    }

    protected void reduceScore(int power) {
        this.gmlistener.onScoreReduced(this.calcScore(power));
    }

    protected void nothing() {
        this.gmlistener.nothing();
    }


}
