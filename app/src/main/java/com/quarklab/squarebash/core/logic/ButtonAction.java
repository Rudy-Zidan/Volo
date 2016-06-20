package com.quarklab.squarebash.core.logic;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.quarklab.squarebash.SquareBash;

/**
 * Created by rudy on 6/20/16.
 */
public class ButtonAction {
    private SquareBash squareBash;
    public void execute(final Context context, Button button){
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                squareBash = (SquareBash)context;
                squareBash.getGameEngine().actionHandler(v);
            }
        });
    }
}
