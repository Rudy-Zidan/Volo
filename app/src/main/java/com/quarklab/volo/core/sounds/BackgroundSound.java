package com.quarklab.volo.core.sounds;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.quarklab.volo.R;

/**
 * Created by rudy on 3/11/17.
 */
public class BackgroundSound extends AsyncTask<Void, Void, Void> {
    private MediaPlayer player;
    private Context context;
    private boolean isPlaying;

    public BackgroundSound(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        this.player = MediaPlayer.create(context, R.raw.gamebackground);
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
        this.player.stop();
        this.player.release();
        this.cancel(true);
        this.isPlaying = false;
    }

    public void setVolume(float left, float right) {
        this.player.setVolume(left,right);
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }
}
