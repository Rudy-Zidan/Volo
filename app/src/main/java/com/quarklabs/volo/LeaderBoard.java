package com.quarklabs.volo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
//import com.mopub.mobileads.MoPubErrorCode;
//import com.mopub.mobileads.MoPubView;
import com.quarklabs.volo.core.adapter.ListAdapter;
import com.quarklabs.volo.core.http.SquareBashAPI;
import com.quarklabs.volo.core.preference.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaderBoard extends Activity {
    private Setting setting;
    private String[] userId;
    private String[] scores;
    private String[] players;
    private ListView list;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_leader_board);
        this.setting = new Setting(this);
        this.getRank();
        ListAdapter adapter = new
                ListAdapter(LeaderBoard.this, this.players, this.userId, this.scores);
        list=(ListView)findViewById(R.id.leaderBoardList);
        list.setAdapter(adapter);
        this.setAds();
    }

    private void getRank(){
        try {
            if(setting.isFacebookAccountExists()){
            JSONObject account = new JSONObject(this.setting.getFacebookAccount());
            JSONObject data = new JSONObject();
            data.put("id", Long.parseLong(account.get("id").toString()));
            data.put("token", setting.getToken());
            JSONArray rankResponse = SquareBashAPI.post(getString(R.string.leader_board_api),
                    data.toString());
            if(rankResponse == null){
                rankResponse = new JSONArray();
                JSONObject dummy = new JSONObject();
                dummy.put("name", "Me");
                dummy.put("score", this.setting.getScore());
                dummy.put("facebookId", "");
                rankResponse.put(dummy);
            }
            this.prepareAdapterData(rankResponse);
            } else {
                JSONObject dummy = new JSONObject();
                dummy.put("name", "Me");
                dummy.put("score", this.setting.getScore());
                dummy.put("facebookId", "");
                JSONArray rank = new JSONArray();
                rank.put(dummy);
                this.prepareAdapterData(rank);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prepareAdapterData(JSONArray data){
        int count = data.length();
        this.userId = new String[count];
        this.scores = new String[count];
        this.players = new String[count];
        for (int i = 0; i < count; i++) {
            try {
                JSONObject player = data.getJSONObject(i);
                this.players[i] = player.getString("name");
                this.scores[i] = player.getString("score");
                if(this.setting.getFacebookId().equals(player.getString("facebook_id"))){
                    this.setting.setScore(Integer.parseInt(this.scores[i]));
                }
                this.userId[i] = player.getString("facebook_id");
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5665008338785349~7222118517");
        this.mAdView = (AdView) findViewById(R.id.adView);

        AdRequest.Builder adBuilder = new AdRequest.Builder().addTestDevice("65294B7A3AF3B393B68BBC6026A955D3");
        AdRequest adRequest = adBuilder.build();

        this.mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(this.mAdView != null){
            this.mAdView.destroy();
        }
        Runtime.getRuntime().gc();
        finish();
    }
    public void onResume() {
        super.onResume();
        if(this.mAdView != null){
            this.mAdView.resume();
        }
    }
    public void onPause() {
        super.onPause();
        if(this.mAdView != null){
            this.mAdView.pause();
        }
    }
}