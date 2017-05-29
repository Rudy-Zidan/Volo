package com.quarklab.volo.core.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rudy on 6/25/16.
 */
public class Setting {
    private SharedPreferences preferences;
    private Context context;

    public Setting(Context context){
        this.context = context;
        this.preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        if(!this.preferences.contains("sound")){
            this.preferences.edit().putBoolean("sound",true);
        }
        if(!this.preferences.contains("score")){
            this.preferences.edit().putLong("score",0);
        }
        if(!this.preferences.contains("facebookUser")){
            this.preferences.edit().putString("facebookUser","");
        }
        if(!this.preferences.contains("facebook_id")){
            this.preferences.edit().putString("facebook_id","");
        }

        if(!this.preferences.contains("token")){
            this.preferences.edit().putString("token","");
        }
    }

    public void setFacebookId(String id){
        this.preferences.edit().putString("facebook_id",id).commit();
    }

    public String getFacebookId(){
        return this.preferences.getString("facebook_id","");
    }

    public void updateFacebookAccount(String account){
        this.preferences.edit().putString("facebookUser",account).commit();
    }
    public boolean isFacebookAccountExists(){
        return !this.preferences.getString("facebookUser", "").equals("");
    }
    public String getFacebookAccount(){
        return this.preferences.getString("facebookUser","");
    }
    public void updateScore(int score){
        long oldScore = this.preferences.getLong("score",0);
        long total_score = oldScore + score;
        this.preferences.edit().putLong("score", total_score).commit();
    }
    public void updateToReduceScore(int score){
        long oldScore = this.preferences.getLong("score",0);
        long total_score = oldScore - score;
        this.preferences.edit().putLong("score", total_score).commit();
    }
    public void setScore(int score){
        this.preferences.edit().putLong("score",score).commit();
    }
    public long getScore(){
        return this.preferences.getLong("score",0);
    }
    public void changeSound(){
        if(this.preferences.getBoolean("sound",true)){
            this.commitSound(false);
        }else{
            this.commitSound(true);
        }
    }
    private void commitSound(boolean value){
        this.preferences.edit().putBoolean("sound",value).commit();
    }

    public boolean playSound(){
        return this.preferences.getBoolean("sound",true);
    }

    public void setToken(String token){
        this.preferences.edit().putString("token",token).commit();
    }

    public String getToken(){
        return this.preferences.getString("token","");
    }

    public void setCurrentTime(long millisec){
        this.preferences.edit().putLong("time", millisec).commit();
    }

    public Long getCurrentTime(){
        return this.preferences.getLong("time", 0);
    }

    public void setGameMode(String gameMode){
        this.preferences.edit().putString("mode", gameMode).commit();
    }

    public String getGameMode(){
        return this.preferences.getString("mode", "");
    }
}
