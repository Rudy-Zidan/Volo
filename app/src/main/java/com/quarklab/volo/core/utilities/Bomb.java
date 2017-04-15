package com.quarklab.volo.core.utilities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quarklab.volo.R;

/**
 * Created by rudy on 4/15/17.
 */

class Bomb extends Utility{
    private ImageView image;
    public Bomb(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void renderBomb(){
        this.image = new ImageView(this.context);
        this.setLayout();
        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        this.layout.addView(image);
        this.image.setImageResource(R.drawable.gift);
        this.animate();
    }

    private void setLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
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
