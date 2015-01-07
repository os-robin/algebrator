package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.circle.SuperView;

import java.util.ArrayList;

/**
 * Created by Colin on 1/6/2015.
 */
public class WritingEquation extends Equation {

    public WritingEquation(SuperView o){
        super(o);
    }

    @Override
    public Equation copy(){
        Equation result = new WritingEquation(owner);
        for (Equation e:this){
            result.add(e.copy());
        }
        return result;
    }

    /**
     * x,y is the center of the equation to be drawn
     */
    @Override
    protected void privateDraw(Canvas canvas, float x, float y){
        lastPoint = new ArrayList<Point>();
        float totalWidth = measureWidth();
        float currentX = 0;
        Paint temp = getPaint();
        if (parentheses) {
            drawParentheses(canvas, x, y, temp);
            currentX+= PARN_WIDTH_ADDITION/2;
        }
        Rect out = new Rect();
        textPaint.getTextBounds(display, 0, display.length(), out);
        float h = out.height();
        for (int i = 0; i < size(); i++) {
            float currentWidth = get(i).measureWidth();
            float currentHeight = get(i).measureHeight();
            get(i).draw(canvas,
                    x - (totalWidth / 2) + currentX + (currentWidth / 2), y);
            currentX += currentWidth;
        }
    }

    @Override
    public float measureWidth(){
        float totalWidth = 0;

        for (int i = 0; i < size(); i++) {
            totalWidth += get(i).measureWidth();
        }

        if (parentheses) {
            totalWidth += PARN_WIDTH_ADDITION;
        }

        return totalWidth;
    }

}
