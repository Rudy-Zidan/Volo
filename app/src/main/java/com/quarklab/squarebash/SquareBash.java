package com.quarklab.squarebash;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.quarklab.squarebash.core.http.SquareBashAPI;
import com.quarklab.squarebash.core.preference.Setting;

import org.json.*;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.entity.mime.Header;


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
        SquareBashAPI.get("/players",null,new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.print(response.toString());
                //startActivity(new Intent(SquareBash.this, GameBoard.class));
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
