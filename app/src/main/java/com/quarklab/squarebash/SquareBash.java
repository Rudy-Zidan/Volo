package com.quarklab.squarebash;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.quarklab.squarebash.core.http.SquareBashAPI;
import com.quarklab.squarebash.core.preference.Setting;

import org.json.JSONArray;


public class SquareBash extends Activity {
    private Setting setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_square_bash);
        Button play = (Button) findViewById(R.id.play);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
        Button sound = (Button)findViewById(R.id.sound);
        this.setting = new Setting(this);
        this.HandleSoundButtonBG(sound);

        sound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setting.changeSound();
                HandleSoundButtonBG((Button)v);
            }
        });
    }
    private void login(){
        JSONArray result = SquareBashAPI.get("/players","");
    }
    private void HandleSoundButtonBG(Button btn){
        if(this.setting.playSound()){
            btn.setBackgroundDrawable( getResources().getDrawable(R.drawable.speaker) );
        }else{
            btn.setBackgroundDrawable( getResources().getDrawable(R.drawable.speakermute) );
        }
    }
}
