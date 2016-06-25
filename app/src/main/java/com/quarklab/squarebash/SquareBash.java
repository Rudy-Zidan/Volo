package com.quarklab.squarebash;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.quarklab.squarebash.core.preference.Setting;

public class SquareBash extends Activity {
    private Setting setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_square_bash);
        Button play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SquareBash.this, GameBoard.class));
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
    private void HandleSoundButtonBG(Button btn){
        if(this.setting.playSound()){
            btn.setBackgroundDrawable( getResources().getDrawable(R.drawable.speaker) );
        }else{
            btn.setBackgroundDrawable( getResources().getDrawable(R.drawable.speakermute) );
        }
    }
}
