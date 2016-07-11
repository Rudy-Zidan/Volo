package com.quarklab.squarebash.core.preference;

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
            this.preferences.edit().putInt("score",0);
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
    public void updateScore(int score){
        int oldScore = this.preferences.getInt("score",0);
        this.preferences.edit().putInt("score",oldScore+score).commit();
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
