package com.quarklab.squarebash;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.quarklab.squarebash.core.adapter.ListAdapter;
import com.quarklab.squarebash.core.http.SquareBashAPI;
import com.quarklab.squarebash.core.preference.Setting;

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
    private MoPubView moPubView;

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
            JSONObject account = new JSONObject(this.setting.getFacebookAccount());
            JSONObject data = new JSONObject();
            data.put("id",Long.parseLong(account.get("id").toString()));
            JSONArray rankResponse = SquareBashAPI.post(getString(R.string.leader_board_api),
                    data.toString());
            this.prepareAdapterData(rankResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prepareAdapterData(JSONArray data){
        int count = data.length();
        if(count>=1){
            this.userId = new String[count];
            this.scores = new String[count];
            this.players = new String[count];
            for (int i = 0; i < count; i++) {
                try {
                    JSONObject player = data.getJSONObject(i);
                    this.players[i] = player.getString("name");
                    this.scores[i] = player.getString("score");
                    this.userId[i] = player.getString("facebookId");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setAds() {

        moPubView = (MoPubView) findViewById(R.id.adviewMopub);
        moPubView.setAdUnitId("df6c5194fadc4046ac36b4cfba32a8fd");
        moPubView.loadAd();
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                Toast.makeText(getApplicationContext(), "Ad Loaded", Toast.LENGTH_SHORT);
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                Toast.makeText(getApplicationContext(), "Ad Failed", Toast.LENGTH_SHORT);
            }

            @Override
            public void onBannerClicked(MoPubView banner) {

            }

            @Override
            public void onBannerExpanded(MoPubView bannesr) {

            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {

            }
        });

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5665008338785349~2646543711");
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
