package com.quarklabs.volo.core.sounds;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.quarklabs.volo.R;

/**
 * Created by rudy on 3/11/17.
 */
public class BombSound extends AsyncTask<Void, Void, Void> {

    private MediaPlayer player;
    private Context context;
    private boolean isPlaying;

    public BombSound(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        this.player = MediaPlayer.create(context, R.raw.bomb);
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.player.setLooping(true); // Set looping
        this.player.setVolume(1f,1f);
        this.player.start();
        this.isPlaying = true;
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void stop(){
        if(this.player != null){
            this.player.release();
            this.cancel(true);
            this.isPlaying = false;
            this.player = null;
        }
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }
}
