package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

import java.util.ArrayList;

/**
 * Created by Colin on 1/10/2015.
 */
public class PowerEquation extends Operation {

    public PowerEquation(SuperView owner){
        super(owner);

        display = "^";
        myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
        myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;
    }

    @Override
    public Equation copy() {
        Equation result = new DivEquation(this.owner);
        result.display = this.getDisplay(-1);
        // pass selected?

        // copy all the kiddos and set this as their parent
        for (int i = 0; i < this.size(); i++) {
            result.add(this.get(i).copy());
            result.get(i).parent = result;
        }
        return result;
    }

    protected float getScale(Equation e){
        if (indexOf(e)==1) {
            return 0.75f;
        }else {
            return 1;
        }
    }

    @Override
    public float measureWidth() {
        // TODO
        float totalWidth = 0;

        for (int i = 0; i < size(); i++) {
            totalWidth += get(i).measureWidth();
        }
        if (parenthesis()) {
            totalWidth += PARN_WIDTH_ADDITION;
        }
        return totalWidth;
    }

    @Override
    public void integrityCheck(){
        if (size() != 2){
            Log.e("ic", "this should be size 2");
        }
    }

                    // y is not centered is that a problem - this at a problem
                    // yes i think it is
                    @Override
                    public void privateDraw(Canvas canvas, float x, float y) {
                        lastPoint = new ArrayList<Point>();
                        float totalWidth = measureWidth();
                        float atX =x - (totalWidth / 2);
                        float atY =y;
                        Paint temp = getPaint();
                        if (parenthesis()) {
                            drawParentheses(canvas, x, y, temp);
                            atX += PARN_WIDTH_ADDITION / 2;
                        }
                        Rect out = new Rect();
                        textPaint.getTextBounds(display, 0, display.length(), out);
                        float h = out.height();


                        for (int i = 0; i < size(); i++) {
                            float currentWidth = get(i).measureWidth();

                            atX += (currentWidth / 2);

                            if (i==1){
                                // we want the bottom of 1 to be at 2/3 height of 0
                                // what's the math then
                                float baseLoc =  get(0).measureHeightLower() - (get(0).measureHeight()*2f/3f);
                                atY += baseLoc - get(i).measureHeightLower();
                            }

                            get(i).draw(canvas,atX, atY);
                            atX += (currentWidth / 2);
                            if (i == 1) {
                                Equation tempEq = get(i);
                                while (tempEq instanceof MinusEquation){
                    tempEq = tempEq.get(0);
                }

                if (tempEq instanceof NumConstEquation) {
                    Point point = new Point();
                    lastPoint.add(point);
                }
            }
        }
    }

    protected float measureHeightLower() {
        float result = get(0).measureHeightLower();
        if (parenthesis()) {
            result += PARN_HEIGHT_ADDITION/2f;
        }
        return result;
    }

    protected float measureHeightUpper() {
        float r0 = get(0).measureHeightUpper();


        float r1 =  -get(0).measureHeightLower() +(get(0).measureHeight()*2f/3f) + get(1).measureHeight();

        float result = Math.max(r0,r1);
        if (parenthesis()) {
            result += PARN_HEIGHT_ADDITION/2f;
        }
        return result;
    }

    @Override
    public float measureHeight() {
          return measureHeightLower() + measureHeightUpper();
    }
}
