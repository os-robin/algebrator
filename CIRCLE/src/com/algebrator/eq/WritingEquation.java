package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

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



    public Equation convert() {
        Equation root = null;
        Equation currentToAdd = null;

        for (int i=0;i<size();i++){
            Equation at = get(i);
            if (at instanceof WritingLeafEquation){
                if(currentToAdd == null){
                    if (at.getDisplay(-1) .equals("+")) {
                        currentToAdd = new AddEquation(owner);
                    }else if (at.getDisplay(-1) .equals("*")){
                        currentToAdd = new MultiEquation(owner);
                    } else if (at.getDisplay(-1) .equals("=")) {
                        currentToAdd = new EqualsEquation(owner);
                    }
                    //TODO or xx or 2( or 2x or )x so many ways to mutliply

                    Equation before=null;
                    for (int j =i-1;j>=0;j--) {

                        if (get(j) instanceof  WritingLeafEquation && get(j).getDisplay(-1).equals("-")){
                            Equation neg = new MinusEquation(owner);
                            if (before ==null){
                                Log.e("ic", "this is waay no good");
                            }
                            neg.add(before);
                            before = neg;
                        }else {
                            before = get(j);
                        }
                    }
                    currentToAdd.add(before);
                    if (root == null){
                        root =currentToAdd;
                        if (currentToAdd instanceof EqualsEquation){
                            currentToAdd = null;
                        }
                    }else{
                        root.add(currentToAdd);
                    }
                }else {
                    Equation newEq = null;
                    if (at.getDisplay(-1).equals("+")) {
                        newEq = new AddEquation(owner);
                    } else if (at.getDisplay(-1).equals("*")) {
                        newEq = new MultiEquation(owner);
                    } else if (at.getDisplay(-1).equals("=")) {
                        newEq = new EqualsEquation(owner);
                    }
                    if (newEq != null) {
                        if (newEq.getClass().equals(currentToAdd.getClass())) {
                            //we don't worry about it
                        } else {
                            if (newEq instanceof MultiEquation) {
                                Equation moving = currentToAdd.get(currentToAdd.size() - 1);
                                currentToAdd.justRemove(moving);
                                if (currentToAdd.parent instanceof MultiEquation) {
                                    newEq = currentToAdd.parent;
                                } else {
                                    currentToAdd.add(newEq);
                                }
                                newEq.add(moving);
                                currentToAdd = newEq;
                            } else if (newEq instanceof AddEquation) {
                                if (currentToAdd.parent instanceof AddEquation) {
                                    newEq = currentToAdd.parent;
                                } else {
                                    newEq.add(currentToAdd);
                                    if (currentToAdd.equals(root)) {
                                        root = newEq;
                                    }
                                }
                                currentToAdd = newEq;
                            } else if (newEq instanceof EqualsEquation) {
                                Equation left = currentToAdd;
                                while (left.parent != null) {
                                    left = left.parent;
                                }
                                newEq.add(left);
                                root = newEq;
                                currentToAdd = null;
                            }
                        }
                    }
                }
            } else {
                if (currentToAdd != null) {
                    // convert it if something something and add it to the thing
                    if (at instanceof WritingEquation) {
                        currentToAdd.add(((WritingEquation) at).convert());
                    } else if (at instanceof DivEquation) {
                        // convert the top and the bottom
                        // and then add it
                        for (Equation e : at) {
                            e.replace(((WritingEquation) e).convert());
                        }
                        currentToAdd.add(at);
                    } else {
                        currentToAdd.add(at);
                    }
                }
            }
        }
    Log.i("conversion result",root.toString() );
        return root;
    }
}
