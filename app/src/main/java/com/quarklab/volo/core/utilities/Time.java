package com.quarklab.volo.core.utilities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quarklab.volo.R;

/**
 * Created by rudy on 5/8/17.
 */

public class Time extends Utility {

    public Time(Context context, UtilityListener listener) {
        super(context, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void renderTime(){
        this.image = new ImageView(this.context);
        this.setLayout();
        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.clearAnimation();
                layout.removeView(image);
                listener.onTimeClick(image);
            }
        });
        this.layout.addView(image);
        this.image.setImageResource(R.drawable.stopwatch);
        this.animate();
    }

    private void setLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.WIDTH, this.HEIGHT);
        image.setX(this.screen.getRandomX());
        image.setY(this.screen.convertSpToPixels(60));
        image.setLayoutParams(layoutParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void animate(){
        image.animate()
                .translationY(this.screen.getHeightPX())
                .setDuration(2500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        layout.removeView(image);
                    }
                });
    }
}
