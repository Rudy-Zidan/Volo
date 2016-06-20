package com.quarklab.squarebash.core.visualization;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.WindowManager;
import android.widget.GridView;

import com.quarklab.squarebash.R;
import com.quarklab.squarebash.core.adapter.ButtonAdapter;

/**
 * Created by rudy on 6/20/16.
 */
public class RenderEngine {
    private Context context;
    private Activity activity;
    private GridView grid;
    public RenderEngine(Context context){
        this.context = context;
        this.setUpActivityScreen();
    }
    private void setUpActivityScreen(){
        this.activity = ((Activity)this.context);
        this.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.activity.setContentView(R.layout.activity_square_bash);
    }
    public void render(int resource){
        this.grid = (GridView) ((Activity)context).findViewById (resource);
        this.grid.setAdapter(new ButtonAdapter(this.context));
    }
}
