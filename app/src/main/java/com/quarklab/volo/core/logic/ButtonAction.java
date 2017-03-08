package com.quarklab.volo.core.logic;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.quarklab.volo.GameBoard;

/**
 * Created by rudy on 6/20/16.
 */
public class ButtonAction {
    private GameBoard gameBoard;
    public void execute(final Context context, Button button){
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gameBoard = (GameBoard)context;
                gameBoard.getGameEngine().actionHandler(v);
            }
        });
    }
}
