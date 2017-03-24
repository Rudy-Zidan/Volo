package com.quarklab.volo.core.shapes;

import com.quarklab.volo.R;

/**
 * Created by rudy on 3/24/17.
 */

class Rectangle extends Shape {
    public int getTemplate(buttons tag){
        int templateId = 0;
        switch (tag){
            case GOOD:    templateId = R.drawable.rectangle_green_button;
                break;
            case EVIL:    templateId = R.drawable.rectangle_red_button;
                break;
            case MEH:     templateId = R.drawable.rectangle_warm_button;
                break;
            case RANDOM:  templateId = R.drawable.rectangle_random;
                break;
        }
        return templateId;
    }
}
