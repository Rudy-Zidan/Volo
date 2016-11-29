package com.quarklab.squarebash;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
