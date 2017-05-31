package com.quarklabs.volo.core.utilities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;

import com.quarklabs.volo.R;

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
        this.image.setSoundEffectsEnabled(false);
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
}
