package com.quarklabs.volo.core.shapes;

import com.quarklabs.volo.R;

/**
 * Created by rudy on 3/24/17.
 */

class Oval extends Shape {

    public int getTemplate(buttons tag){
        int templateId = 0;
        switch (tag){
            case GOOD:    templateId = R.drawable.oval_green_button;
                break;
            case EVIL:    templateId = R.drawable.oval_red_button;
                break;
            case MEH:     templateId = R.drawable.oval_warm_button;
                break;
            case RANDOM:  templateId = R.drawable.oval_random;
                break;
        }
        return templateId;
    }
}
