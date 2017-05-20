package com.quarklab.volo.core.utilities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quarklab.volo.R;

import java.util.Random;


/**
 * Created by rudy on 4/15/17.
 */

class Bomb extends Utility{

    private enum Types {Speed_Up, Speed_Down, Green, Gray, Red}
    public Bomb(Context context, UtilityListener listener) {
        super(context, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void renderBomb(){
        this.image = new ImageView(this.context);
        this.image.setSoundEffectsEnabled(false);
        this.setLayout();
        this.setImageTags();
        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.clearAnimation();
                layout.removeView(image);
                listener.onBombClick(image);
            }
        });
        this.layout.addView(image);
        this.image.setImageResource(R.drawable.bomb);
        this.animate();
    }

    private void setImageTags(){
        Random rand = new Random();
        int typeIndex = rand.nextInt(Types.values().length);
        this.image.setTag(Types.values()[typeIndex].toString());
    }
}
