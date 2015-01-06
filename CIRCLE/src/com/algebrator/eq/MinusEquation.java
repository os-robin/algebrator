package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Colin on 1/4/2015.
 */
public class MinusEquation extends Equation {

    public MinusEquation(SuperView owner2) {
        super(owner2);

        display ="-";

        myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
        myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;
    }

    @Override
    public void integrityCheck() {
        if (size()!=1){
            Log.e("ic", "this should always be size 1");
        }
    }

    @Override
    public Equation copy() {
        Equation result = new MinusEquation(this.owner);
        result.add(get(0).copy());
        return result;
    }

    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        if (!(parent instanceof AddEquation) || (parent.indexOf(this) == 0)) {
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

            // draw the minus sign
            Point point = new Point();
            point.x = (int) (x - (totalWidth / 2) + currentX + (myWidth / 2));
            point.y = (int) (y + (h / 2));
            canvas.drawText(getDisplay(0), point.x, point.y, temp);

            lastPoint.add(point);
            currentX += myWidth;

            //draw the contence
            float currentWidth = get(0).measureWidth();
            float currentHeight = get(0).measureHeight();
            get(0).draw(canvas,
                        x - (totalWidth / 2) + currentX + (currentWidth / 2), y);
            currentX += currentWidth;
        }else{
            super.privateDraw(canvas,x,y);
        }
    }

    @Override
    public float measureWidth() {
        // if we are not the first element in an add equation
        if (!(parent instanceof AddEquation) || (parent.indexOf(this) == 0)) {
            return myWidth + super.measureWidth();
        }else{
            return super.measureWidth();
        }
    }

    @Override
    public HashSet<Equation> on(float x, float y) {
        Log.i("yo,yo", x + "," + y);
        HashSet<Equation> result = new HashSet<Equation>();
        for (int i = 0; i < lastPoint.size(); i++) {
            if (x < lastPoint.get(i).x + myWidth / 2
                    && x > lastPoint.get(i).x - myWidth / 2
                    && y < lastPoint.get(i).y + myHeight / 2
                    && y > lastPoint.get(i).y - myHeight / 2) {
                result.add(this);
            }
        }
        return result;
    }

    @Override
    public void remove(){
        super.remove();
        if (size() ==0){
            add(new NumConstEquation(1,owner));
        }
    }

    @Override
    public void tryOperator(ArrayList<Equation> eqs) {
        if (parent instanceof MinusEquation){
            parent.replace(get(0));
        }else
        if (get(0) instanceof MinusEquation){
            replace(get(0).get(0));
        }else{
            if (get(0).size()>1){
                for (Equation e: get(0)){
                    Equation minus = new MinusEquation(owner);
                    e.replace(minus);
                    minus.add(e);
                }
                replace(get(0));
            }
        }
    }
}
