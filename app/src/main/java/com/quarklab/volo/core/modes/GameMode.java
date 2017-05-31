package com.quarklab.volo.core.modes;

import java.util.Random;

/**
 * Created by rudy on 2/16/17.
 */
public class GameMode {
    private enum modes {Easy,Sudden_Death,No_Score,Double_Tap}
    protected enum buttons {GOOD, EVIL, MEH}
    private modes current;

    protected int score;
    private Random rand;


    protected GameModeListener gmlistener;

    public GameMode(GameModeListener gmListener, int score) {
        this.score = score;
        this.current = modes.Easy;
        this.gmlistener = gmListener;
        this.rand = new Random();
    }

    public void change() {
        int random = this.rand.nextInt(3);
        if (!this.current.equals(modes.values()[random])) {
            this.current = modes.values()[random];
        }else{
            change();
        }
    }

    public void execute(String tag) {
        switch (this.current) {
            case Easy: getEasyMode().execute(getTag(tag));
                break;
            case Sudden_Death: getSuddenDeathMode().execute(getTag(tag));
                break;
            case No_Score:
                break;
            case Double_Tap: getDoubleTabMode().execute(getTag(tag));
                break;
        }
    }

    public String getCurrentGameMode() {
        return this.current.toString();
    }

    private Easy getEasyMode() {
        return new Easy(this.gmlistener, this.score);
    }

    private SuddenDeath getSuddenDeathMode() {
        return new SuddenDeath(this.gmlistener, this.score);
    }

    private DoubleTab getDoubleTabMode() {
        return new DoubleTab(this.gmlistener, this.score);
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

    private int calcScore(int limit, int power, boolean stable) {
        Random rand = new Random();
        int score = 0;
        if(stable){
            limit = 20;
            score = rand.nextInt(limit) + 1;
        }else if(limit > 0){
            score = rand.nextInt(limit);
        }

        if(power !=0){
            score =  power*score;
        }

        return Math.abs(score);
    }

    protected void endLife() {
        this.gmlistener.onLifeLost();
    }

    protected void addScore(int limit, int power) {
        this.gmlistener.onScoreAdded(this.calcScore(limit, power, true));
    }

    protected void reduceScore(int limit, int power) {
        this.gmlistener.onScoreReduced(this.calcScore(limit, power, false));
    }

    protected void nothing() {
        this.gmlistener.nothing();
    }

    public void setUserScore(int score){
        this.score = score;
    }

}
