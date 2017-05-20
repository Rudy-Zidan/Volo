package com.quarklab.volo.core.utilities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quarklab.volo.R;

/**
 * Created by rudy on 4/15/17.
 */

class Gift extends Utility{

    public Gift(Context context, UtilityListener listener) {
        super(context, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void renderGift(){
        this.image = new ImageView(this.context);
        this.image.setSoundEffectsEnabled(false);
        this.setLayout();
        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.clearAnimation();
                layout.removeView(image);
                listener.onGiftClick(image);
            }
        });
        this.layout.addView(image);
        this.image.setImageResource(R.drawable.gift);
        this.animate();
    }
}
