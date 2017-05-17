package com.quarklab.volo.core.utilities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quarklab.volo.R;

/**
 * Created by rudy on 5/18/17.
 */

public class Dice extends Utility {

    public Dice(Context context, UtilityListener listener) {
        super(context, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void renderDice(){
        this.image = new ImageView(this.context);
        this.setLayout();
        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.clearAnimation();
                layout.removeView(image);
                listener.onDiceClick(image);
            }
        });
        this.layout.addView(image);
        this.image.setImageResource(R.drawable.dice);
        this.animate();
    }
}
