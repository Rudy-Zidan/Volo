package com.quarklab.squarebash.core.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.quarklab.squarebash.R;
import com.quarklab.squarebash.core.logic.ButtonAction;
import com.quarklab.squarebash.core.visualization.PhoneScreen;

/**
 * Created by rudy on 6/20/16.
 */
public class ButtonAdapter extends BaseAdapter {
    private Context context;
    private PhoneScreen screen;
    private ButtonAction buttonAction;
    public ButtonAdapter(Context context){
        this.context = context;
        this.screen = new PhoneScreen(this.context);
        this.buttonAction = new ButtonAction();
    }
    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button btn;
        if (convertView == null) {
            btn = new Button (this.context);
//            int buttonHeight = this.screen.getHeightPX()/4;
//            int buttonWidth = this.screen.getWidthPX()/3;
//            btn.setHeight(100);
//            btn.setWidth(100);

        }else{
            btn = (Button) convertView;
        }
        btn.setSoundEffectsEnabled(false);
        btn.setBackgroundResource(R.drawable.block);
        btn.setId(position);
        this.buttonAction.execute(this.context,btn);
        return btn;
    }
}
