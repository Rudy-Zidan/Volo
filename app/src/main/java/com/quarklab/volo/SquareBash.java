package com.quarklab.volo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.quarklab.volo.core.authentication.Facebook;
import com.quarklab.volo.core.http.SquareBashAPI;
import com.quarklab.volo.core.logic.Numbers;
import com.quarklab.volo.core.preference.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


public class SquareBash extends Activity {
    public Setting setting;
    private Facebook facebook;
    public CallbackManager callbackManager;
    private LinearLayout linearLoader;
    private LinearLayout linearActions;

    private Typeface typeface;

    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FacebookSdk.sdkInitialize(getApplicationContext());
        this.facebook = new Facebook();
        setContentView(R.layout.activity_square_bash);
        ImageButton play = (ImageButton) findViewById(R.id.play);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    facebook.authenticate(SquareBash.this);
                }else{
                    displayGameBoard();
                }
            }
        });
        ImageButton sound = (ImageButton)findViewById(R.id.sound);
        this.setting = new Setting(this);
        this.HandleSoundButtonBG(sound);

        sound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setting.changeSound();
                HandleSoundButtonBG((ImageButton)v);
            }
        });

        ImageButton trophy = (ImageButton)findViewById(R.id.trophy);

        this.linearLoader = (LinearLayout) findViewById(R.id.loader);
        this.linearActions = (LinearLayout) findViewById(R.id.actions);

        trophy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                linearActions.setVisibility(View.GONE);
                linearLoader.setVisibility(View.VISIBLE);
                if(setting.isFacebookAccountExists() && isNetworkAvailable()){
                    getFacebookFriends();
                }else{
                    displayLeaderBoard();
                }
            }
        });
        printKeyHash();
        this.showUserScore();

        AssetManager am = getApplicationContext().getAssets();
        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "chlorinr.ttf"));
        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(this.typeface);
    }
    public void displayGameBoard(){
        Intent gameBoardIntent= new Intent(SquareBash.this,GameBoard.class);
        startActivity(gameBoardIntent);
    }

    public void displayLeaderBoard(){
        Intent leaderBoardIntent= new Intent(SquareBash.this,LeaderBoard.class);
        startActivity(leaderBoardIntent);
    }

    private void HandleSoundButtonBG(ImageButton btn){
        if(this.setting.playSound()){
            btn.setImageDrawable( getResources().getDrawable(R.drawable.speaker) );
        }else{
            btn.setImageDrawable( getResources().getDrawable(R.drawable.speakermute) );
        }
    }

    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.quarklab.volo",
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

    private void getFacebookFriends(){
        final String[] result = {""};
        AccessToken token = AccessToken.getCurrentAccessToken();
        if(token!=null){
            GraphRequest request = GraphRequest.newMyFriendsRequest(
                token,
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray objects, GraphResponse response) {
                    result[0] = objects.toString();
                    try {
                        JSONObject account = new JSONObject(setting.getFacebookAccount());
                        JSONObject data = new JSONObject();
                        data.put("id",Long.parseLong(account.get("id").toString()));
                        data.put("score",setting.getScore());
                        data.put("friends",objects);
                        JSONObject x = SquareBashAPI.postObject(getString(R.string.update_score_api),
                                data.toString());
                        if(x.getBoolean("status")){
                            //TODO Open leadersboard a                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ctivity.
                            displayLeaderBoard();
                        }else{
                            linearActions.setVisibility(View.VISIBLE);
                            linearLoader.setVisibility(View.GONE);
                            alertUser();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                }
            );
            Bundle parameters = new Bundle();
            request.setParameters(parameters);
            request.executeAsync();
        }
    }
    private void alertUser(){
        Toast.makeText(getApplicationContext(),R.string.server_problem,
                Toast.LENGTH_SHORT).show();
    }

    private void showUserScore(){
        TextView score = (TextView)findViewById(R.id.scoreText);
        score.setText(Numbers.format(this.setting.getScore())+"");
        score.setTypeface(this.typeface);
    }

    @Override
    protected void onResume() {
        super.onResume();
        linearActions.setVisibility(View.VISIBLE);
        linearLoader.setVisibility(View.GONE);
        this.showUserScore();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
