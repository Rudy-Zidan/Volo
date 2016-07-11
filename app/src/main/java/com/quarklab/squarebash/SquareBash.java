package com.quarklab.squarebash;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.quarklab.squarebash.core.authentication.Facebook;
import com.quarklab.squarebash.core.http.SquareBashAPI;
import com.quarklab.squarebash.core.preference.Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SquareBash extends Activity {
    public Setting setting;
    private Facebook facebook;
    public CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FacebookSdk.sdkInitialize(getApplicationContext());
        this.facebook = new Facebook();
        setContentView(R.layout.activity_square_bash);
        Button play = (Button) findViewById(R.id.play);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                facebook.authenticate(SquareBash.this);
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

        Button trophy = (Button)findViewById(R.id.trophy);

        trophy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(setting.isFacebookAccountExists()){
                    try {
                        JSONObject account = new JSONObject(setting.getFacebookAccount());
                        JSONObject data = new JSONObject();
                        data.put("id",Long.parseLong(account.get("id").toString()));
                        data.put("score",setting.getScore());
                        JSONObject x = SquareBashAPI.post("/player/update_score",data.toString());
                        if(x.has("save") && x.getBoolean("save")){
                            //TODO Open leadersboard activity.
                        }else{
                            //TODO alert user the connection failed.
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void displayGameBoard(){
        Intent gameBoardIntent= new Intent(SquareBash.this,GameBoard.class);
        startActivity(gameBoardIntent);
    }
    private void HandleSoundButtonBG(Button btn){
        if(this.setting.playSound()){
            btn.setBackgroundDrawable( getResources().getDrawable(R.drawable.speaker) );
        }else{
            btn.setBackgroundDrawable( getResources().getDrawable(R.drawable.speakermute) );
        }
    }

    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.quarklab.squarebash",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }
}
