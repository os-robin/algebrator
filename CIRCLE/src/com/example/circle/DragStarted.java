package com.example.circle;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Colin on 1/21/2015.
 */
public class DragStarted extends Animation {
    float currentAlpha=0xff;

    public DragStarted(SuperView owner){
        super(owner);
    }
    public DragStarted(SuperView owner, float currentAlpha){
        super(owner);
        this.currentAlpha = currentAlpha;
    }

    @Override
    public void draw(Canvas canvas) {
        if (currentAlpha>1) {
            owner.drawProgress(canvas, 1f, currentAlpha);
            currentAlpha = currentAlpha/1.05f;
        }else{
            remove();
        }
    }
}
