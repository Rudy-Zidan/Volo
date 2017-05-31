package com.quarklabs.volo.core.shapes;

import java.util.Random;

/**
 * Created by rudy on 3/24/17.
 */

public class Shape {
    protected enum buttons {GOOD, EVIL, MEH, RANDOM}
    private   enum shapes{Oval, Rectangle}
    private shapes currentShape;
    public Shape() {
        this.currentShape = shapes.Oval;
    }
    public int getTemplate(String tag){
        int templateId = 0;
        switch (currentShape){
            case Oval: templateId = new Oval().getTemplate(getTag(tag));
                break;
            case Rectangle: templateId = new Rectangle().getTemplate(getTag(tag));
                break;
        }
        return templateId;
    }

    private Shape.buttons getTag(String buttonTag) {
        Shape.buttons tag = null;
        switch (buttonTag) {
            case "good": tag = Shape.buttons.GOOD;
                break;
            case "evil": tag =  Shape.buttons.EVIL;
                break;
            case "meh": tag =  Shape.buttons.MEH;
                break;
            case "random": tag =  Shape.buttons.RANDOM;
                break;
        }
        return tag;
    }

    public void changeShape(){
        Random rand = new Random();
        int shapeIndex = rand.nextInt(shapes.values().length);
        if(this.currentShape.equals(shapes.values()[shapeIndex])){
            changeShape();
        }else{
            this.currentShape = shapes.values()[shapeIndex];
        }
    }

    public String getCurrentShape() {
        return currentShape.toString();
    }
}
