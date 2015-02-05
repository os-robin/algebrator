package com.example.circle;

import android.graphics.BlurMaskFilter;
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
    int bkgCurrentAlpha = 0x0;
    int bkgTargetAlpha = 0x0;
    final int rate = 10;
    ColinView cv;
    public EquationButton(Equation e, ColinView cv) {
        myEq =e;this.cv=cv;
    }

    public void draw(Canvas canvas,int stupidX,int stupidY) {
        drawBkg(canvas, x + stupidX, y + stupidY);
        ((EqualsEquation)myEq).drawCentered(canvas,x+stupidX,y+stupidY);

    }

    // x and y are the center of the equation
    public void drawBkg(Canvas canvas,float x,float y){

        //TODO scale by dpi
        float buffer = 10;

        float middle = myEq.measureWidth() - ( myEq.get(0).measureWidth() + myEq.get(1).measureWidth());
        float leftEnd = x - (middle/2) - myEq.get(0).measureWidth() -buffer;
        float rightEnd = x + (middle/2) + myEq.get(1).measureWidth() +buffer;
        float topEnd = y  - myEq.measureHeightUpper() -buffer;
        float bottomEnd = y + myEq.measureHeightLower()+buffer;

        Paint temp = new Paint();
        //TODO scale by dpi
        temp.setMaskFilter(new BlurMaskFilter(32, BlurMaskFilter.Blur.NORMAL));

        temp.setColor(Algebrator.getAlgebrator().highLight);
        temp.setAlpha(bkgCurrentAlpha);

        canvas.drawRect(leftEnd,topEnd,rightEnd,bottomEnd, temp);

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
        if (inBox(event) && !cv.history.get(0).equals(this)){
            targetAlpha = 0xff;
            Log.d("highlighting ",myEq.toString());

            if (lastLongTouch==null){
                lastLongTouch= new LongTouch(event);
            }else{
                if (lastLongTouch.outside(event)){
                    lastLongTouch = new LongTouch(event);
                }
            }
        }

        if (event.getAction() ==MotionEvent.ACTION_UP){
            lastLongTouch = null;
        }

    }

    private boolean inBox(MotionEvent event) {
        float stupidX = cv.stupid.lastPoint.get(0).x;
        float stupidY = cv.stupid.lastPoint.get(0).y;

        float middle = myEq.measureWidth() - ( myEq.get(0).measureWidth() + myEq.get(1).measureWidth());
        float leftEnd = (x+stupidX) - (middle/2) - myEq.get(0).measureWidth();
        float rightEnd = (x+stupidX) + (middle/2) + myEq.get(1).measureWidth();
        float topEnd = (y+stupidY)  - myEq.measureHeightUpper();
        float bottomEnd = (y+stupidY) + myEq.measureHeightLower();
        if (event.getX() < rightEnd && event.getX() > leftEnd && event.getY() > topEnd && event.getY() < bottomEnd){
            return true;
        }
        return false;
    }

    private void revert() {
        // update the offset
        cv.offsetX +=x;
        cv.offsetY +=y;

        // we are moving from draw to drawCentered
        // we need to update
        float myCenter = myEq.x;
        float stupidCenter = cv.stupid.x;
        cv.offsetX -=  (-myCenter + stupidCenter);

        // and set this back to be the boss
        cv.stupid = myEq.copy();
        cv.stupid.updateLocation();

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
        bkgCurrentAlpha = 0x0;
        bkgTargetAlpha = 0x0;
    }

    public void update(int stupidX,int stupidY) {
        currentAlpha = (currentAlpha*rate + targetAlpha)/(rate+1);
        bkgCurrentAlpha = (bkgCurrentAlpha*rate + bkgTargetAlpha)/(rate+1);
        if (lastLongTouch == null){
            bkgTargetAlpha = 0x00;
        }else{
            bkgTargetAlpha = 0xff;
        }
        x = (x*rate + targetX)/(rate+1);
        y = (y*rate + targetY)/(rate+1);
        myEq.textPaint.setAlpha(currentAlpha);
    }
}
