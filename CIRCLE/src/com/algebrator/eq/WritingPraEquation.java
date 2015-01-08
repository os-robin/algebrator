package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.circle.SuperView;

import java.util.ArrayList;

/**
 * Created by Colin on 1/7/2015.
 */
public class WritingPraEquation extends WritingLeafEquation {
    public boolean left;


    public WritingPraEquation(boolean left, SuperView emilyView) {
        super((left?"(":")"),emilyView);
        this.left = left;
    }

    @Override
    public Equation copy() {
        Equation result = new WritingPraEquation(this.left, this.owner);
        return result;
    }

    @Override
    public void privateDraw(Canvas canvas, float x, float y){
        lastPoint = new ArrayList<Point>();
        float totalWidth = measureWidth();
        float currentX = 0;
        Paint temp = getPaint();
        drawParentheses(canvas,x,y,temp,left);
    }

    protected void drawParentheses(Canvas canvas, float x, float y, Paint temp, boolean left) {
        Paint ptemp = new Paint(temp);
        ptemp.setStrokeWidth(3);
        float h = measureHeight();
        if(left) {
            //left side
            canvas.drawLine(x + 3, y - (h / 2) + 3, x + 9, y - (h / 2) + 3, ptemp);
            canvas.drawLine(x + 3, y - (h / 2) + 3, x + 3, y + (h / 2) - 3, ptemp);
            canvas.drawLine(x + 3, y + (h / 2) - 3, x + 9, y + (h / 2) - 3, ptemp);
        }else {

            //right side
            canvas.drawLine(x - 3, y - (h / 2) + 3, x - 9, y - (h / 2) + 3, ptemp);
            canvas.drawLine(x - 3, y - (h / 2) + 3, x - 3, y + (h / 2) - 3, ptemp);
            canvas.drawLine(x - 3, y + (h / 2) - 3, x - 9, y + (h / 2) - 3, ptemp);
        }
    }

    public float measureHeight(){
        float totalHeight = myHeight;

        // if left
        // move left through parent
        // match our height to the tallest thing left of us that is:
        // not a open
        boolean go = true;
        int depth = 1;
        Equation current = this;
        if (left){
            current = current.right();
            while (go && depth !=0){
                if (current != null){
                    if (current instanceof WritingPraEquation){
                        if (((WritingPraEquation)current).left){
                            if (depth == 1){
                                totalHeight = Math.max(totalHeight,current.measureHeight()+ PARN_HEIGHT_ADDITION);
                            }
                            depth++;
                        }else{
                            depth--;
                            if (depth==0){
                                go = false;
                            }
                        }
                    }else{
                        if (depth == 1){
                            totalHeight = Math.max(totalHeight,current.measureHeight()+ PARN_HEIGHT_ADDITION);
                        }
                    }
                    current = current.right();
                }else{
                    go = false;
                }
            }
        }else{
            current = current.left();
            while (go && depth !=0){
                if (current != null){
                    if (current instanceof WritingPraEquation){
                        if (!((WritingPraEquation)current).left){
                            depth++;
                        }else{
                            depth--;
                            if (depth == 0){
                                totalHeight= current.measureHeight();
                                go = false;
                            }
                        }
                    }
                    current = current.left();
                }else{
                    go = false;
                    Log.e("","this does not have a matching left para");
                }
            }
        }

        return totalHeight;
    }
}
