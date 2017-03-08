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
        this.preferences = context.getSharedPreferences("setting",context.MODE_PRIVATE);
        if(!this.preferences.contains("sound")){
            this.preferences.edit().putBoolean("sound",true);
        }
        if(!this.preferences.contains("score")){
            this.preferences.edit().putLong("score",0);
        }
        if(!this.preferences.contains("facebookUser")){
            this.preferences.edit().putString("facebookUser","");
        }
    }
    public void updateFacebookAccount(String account){
        this.preferences.edit().putString("facebookUser",account).commit();
    }
    public boolean isFacebookAccountExists(){
        if(!this.preferences.getString("facebookUser","").equals(""))
            return true;
        else
            return false;
    }
    public String getFacebookAccount(){
        return this.preferences.getString("facebookUser","");
    }
    public void updateScore(int score){
        long oldScore = this.preferences.getLong("score",0);
        this.preferences.edit().putLong("score",oldScore+score).commit();
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
}
