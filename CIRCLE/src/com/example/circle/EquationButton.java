package com.example.circle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;

import java.util.ArrayList;

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
    ColinView cv;
    public EquationButton(Equation e, ColinView cv) {
        myEq =e;this.cv=cv;
    }

    public void draw(Canvas canvas,int stupidX,int stupidY) {
            ((EqualsEquation)myEq).drawCentered(canvas,x+stupidX,y+stupidY);
    }

    public void tryRevert(Canvas canvas){
        if (!this.equals(cv.history.get(0))) {
            if (lastLongTouch != null && lastLongTouch.started()) {
                if (lastLongTouch.done()) {
                    Log.i("lastLongTouch", "done");
                    cv.animation.add(new DragStarted(cv, 0x7f));
                    revert();
                    lastLongTouch = null;
                } else {
                    cv.drawProgress(canvas, lastLongTouch.percent(), 0x7f);
                    Log.i("lastLongTouch", lastLongTouch.percent() + "");
                }
            }
        }
    }

    LongTouch lastLongTouch=null;
    //long lastTap = 0;
    public void click(MotionEvent event) {
        if (myEq.OnAnyEqualsIncluded(event.getX(), event.getY()).size() !=0){
            currentAlpha = 0xff;
            //TODO act

            if (lastLongTouch==null){
                lastLongTouch= new LongTouch(event);
            }else{
                if (lastLongTouch.outside(event)){
                    lastLongTouch = new LongTouch(event);
                }
            }

//            if (cv.history.indexOf(this) != 0) {
//                long now = System.currentTimeMillis();
//                if (now - lastTap < Algebrator.getAlgebrator().doubleTapSpacing) {
//                    Log.i("", "I was double tapped");
//                    revert();
//                }
//                lastTap = now;
//            }
        }

        if (event.getAction() ==MotionEvent.ACTION_UP){
            lastLongTouch = null;
        }

    }

    private void revert() {
        // update the offset
        cv.offsetX +=x;
        cv.offsetY +=y;

        // and set this back to be the boss
        cv.stupid = myEq.copy();

        // we need to remove all history and including this
        cv.history = new ArrayList<EquationButton>(cv.history.subList(cv.history.indexOf(this),cv.history.size()));

        // update the offsets of the remaining histories
        for (EquationButton eb: cv.history){
            if (eb != this) {
                eb.x -= x;
                eb.y -= y;
            }
        }
        x=0;
        y=0;

    }

    public void update(int stupidX,int stupidY) {
        currentAlpha = (currentAlpha*rate + targetAlpha)/(rate+1);
        x = (x*rate + targetX)/(rate+1);
        y = (y*rate + targetY)/(rate+1);
        myEq.textPaint.setAlpha(currentAlpha);
    }
}
