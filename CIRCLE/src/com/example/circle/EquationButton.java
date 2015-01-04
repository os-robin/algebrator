package com.example.circle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;

/**
 * Created by Colin on 1/3/2015.
 */
public class EquationButton extends Button {
    Equation myEq;
    float x=0;
    float y=0;
    float targetX;
    float targetY;
    int currentAlpha = 0;
    int targetAlpha =0xff;
    final int rate = 10;
    public EquationButton(Equation e) {
        myEq =e;
    }

    public void draw(Canvas canvas,int stupidX,int stupidY) {
        currentAlpha = (currentAlpha*rate + targetAlpha)/(rate+1);
        x = (x*rate + targetX)/(rate+1);
        y = (y*rate + targetY)/(rate+1);
        myEq.textPaint.setAlpha(currentAlpha);
        ((EqualsEquation)myEq).drawCentered(canvas,x+stupidX,y+stupidY);
    }

    public void click(MotionEvent event) {
        if (myEq.onAny(event.getX(),event.getY()).size() !=0){
            bkgPaint.setColor(highlightColor);
            //TODO act
        }
    }
}
