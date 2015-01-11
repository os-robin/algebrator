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

    public WritingEquation(SuperView o) {
        super(o);
        display = "\"";
    }

    @Override
    public Equation copy() {
        Equation result = new WritingEquation(owner);
        for (Equation e : this) {
            result.add(e.copy());
        }
        return result;
    }

    /**
     * x,y is the center of the equation to be drawn
     */
    @Override
    protected void privateDraw(Canvas canvas, float x, float y) {
        lastPoint = new ArrayList<Point>();
        float totalWidth = measureWidth();
        float currentX = 0;
        Paint temp = getPaint();
        if (parenthesis()) {
            drawParentheses(canvas, x, y, temp);
            currentX += PARN_WIDTH_ADDITION / 2;
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
    public float measureWidth() {
        float totalWidth = 0;

        for (int i = 0; i < size(); i++) {
            totalWidth += get(i).measureWidth();
        }

        if (parenthesis()) {
            totalWidth += PARN_WIDTH_ADDITION;
        }

        return totalWidth;
    }


    public Equation convert() {

        owner.removeSelected();

        //TODO flatten?

        addImpliedMultiplication();
        Log.i("after doing implied multiplication", toString());

        Equation root = null;
        Equation currentToAdd = null;
        Equation left = null;
        boolean openParen = false;
        int minus = 0;

        for (int i = 0; i < size(); i++) {
            Equation at = get(i);
            if (currentToAdd == null) {
                Log.i("left", (left == null ? "null" : left.toString()));
                Log.i("at", at.toString());
                if (at.getDisplay(-1).equals("+")) {
                    currentToAdd = new AddEquation(owner);
                } else if (at.getDisplay(-1).equals("*")) {
                    currentToAdd = new MultiEquation(owner);
                } else if (at.getDisplay(-1).equals("=")) {
                    currentToAdd = new EqualsEquation(owner);
                } else if (at.getDisplay(-1).equals("-") && (i == 0 || (get(i - 1) instanceof WritingLeafEquation && get(i - 1).getDisplay(-1).equals("=")))) {
                    minus++;
                } else if (at.getDisplay(-1).equals("-")) {
                    currentToAdd = new AddEquation(owner);
                    minus++;
                } else {
                    if (at instanceof WritingPraEquation && ((WritingPraEquation) at).left) {
                        Equation temp = ((WritingPraEquation) at).popBlock();
                        temp.remove(0);
                        temp.remove(temp.size() - 1);
                        // we need to remove the first and last
                        left = convert(temp);

                    } else {
                        left = convert(at);
                    }

                    //TODO this minus could go inside but we are not going to worry about that now
                    while (minus > 0) {
                        Equation neg = new MinusEquation(owner);
                        neg.add(left);
                        left = neg;
                        minus--;
                    }
                    if (left instanceof AddEquation || left instanceof MultiEquation) {
                        currentToAdd = left;
                        left = null;
                    }
                }
                if (currentToAdd != null) {
                    if (root == null) {
                        root = currentToAdd;
                    }else{
                        root.add(currentToAdd);
                    }
                    if (left != null) {
                        currentToAdd.add(left);
                    }
                    if (currentToAdd instanceof EqualsEquation){
                        currentToAdd =null;
                    }
                    left=null;
                }
            } else {
                if (at instanceof WritingLeafEquation) {
                    Equation newEq = null;
                    if (at.getDisplay(-1).equals("+")) {
                        newEq = new AddEquation(owner);
                    } else if (at.getDisplay(-1).equals("*")) {
                        newEq = new MultiEquation(owner);
                    } else if (at.getDisplay(-1).equals("=")) {
                        newEq = new EqualsEquation(owner);
                    } else if (at instanceof WritingPraEquation && ((WritingPraEquation) at).left) {
                        openParen = true;
                    } else if (at.getDisplay(-1).equals("-")) {
                        if (!get(i - 1).getDisplay(-1).equals("*")) {
                            newEq = new AddEquation(owner);
                        }
                        minus++;
                    }
                    if (newEq != null) {
                        if (newEq.getClass().equals(currentToAdd.getClass())) {
                            //we don't worry about it
                        } else {
                            if (newEq instanceof MultiEquation) {
                                Equation last = get(i - 1);
                                if (last instanceof WritingPraEquation && !((WritingPraEquation) last).left) {
                                    if (currentToAdd.parent instanceof MultiEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        newEq.add(currentToAdd);
                                        if (currentToAdd.equals(root)) {
                                            root = newEq;
                                        }
                                    }
                                    currentToAdd = newEq;
                                } else {
                                    Equation moving = currentToAdd.get(currentToAdd.size() - 1);
                                    currentToAdd.justRemove(moving);
                                    if (currentToAdd.parent instanceof MultiEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        currentToAdd.add(newEq);
                                    }
                                    newEq.add(moving);
                                    currentToAdd = newEq;
                                }
                                openParen = false;
                            } else if (newEq instanceof AddEquation) {
                                if (openParen) {
                                    Equation moving = currentToAdd.get(currentToAdd.size() - 1);
                                    currentToAdd.justRemove(moving);
                                    if (currentToAdd.parent instanceof AddEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        currentToAdd.add(newEq);
                                    }
                                    newEq.add(moving);
                                    currentToAdd = newEq;
                                } else {
                                    if (currentToAdd.parent instanceof AddEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        newEq.add(currentToAdd);
                                        if (currentToAdd.equals(root)) {
                                            root = newEq;
                                        }
                                    }
                                    currentToAdd = newEq;
                                }
                                openParen = false;
                            } else if (newEq instanceof EqualsEquation) {
                                Equation before;
                                if (currentToAdd != null) {
                                    before = currentToAdd;
                                    while (before.parent != null) {
                                        before = before.parent;
                                    }
                                } else {
                                    before=left;
                                }
                                newEq.add(before);
                                root = newEq;
                                currentToAdd = null;
                                left = null;
                            }
                        }
                    }

                } else {
                    if (currentToAdd != null) {
                        // convert it if something something and add it to the thing
                        at = convert(at);
                        while (minus > 0) {
                            Equation neg = new MinusEquation(owner);
                            neg.add(at);
                            at = neg;
                            minus--;
                        }
                        currentToAdd.add(at);
                    }
                }
            }
        }
        if (root.size() == 1) {
            // if have something of the form =---1
            root.add(left);
        }
        Log.i("conversion result", root.toString());
        if (root != null) {
            return root;
        } else {
            return left;
        }
    }

    private Equation convert(Equation at) {

        if (at instanceof WritingEquation) {
            return ((WritingEquation) at).convert();
        } else if (at instanceof DivEquation) {
            // convert the top and the bottom
            // and then add it
            at = convertDiv((DivEquation) at);
            return at;
        } else {
            return at;
        }
    }

    private void addImpliedMultiplication() {
        // we interate over and handle xx or 2( or 2x or )x or x2 or 2(2/4) or (2/4)2
        for (int i = 0; i < size() - 1; i++) {
            Equation left = get(i);
            if (left instanceof DivEquation) {
                left = addImpliedMultiplicationDiv((DivEquation) left);
            }
            Equation right = get(i + 1);
            if (right instanceof DivEquation) {
                right = addImpliedMultiplicationDiv((DivEquation) right);
            }
            if ((left instanceof DivEquation || left instanceof NumConstEquation || left instanceof VarEquation || (left instanceof WritingPraEquation && !((WritingPraEquation) left).left)) &&
                    (right instanceof DivEquation || right instanceof NumConstEquation || right instanceof VarEquation || (right instanceof WritingPraEquation && ((WritingPraEquation) right).left))) {
                // we need to insert a *
                add(i + 1, new WritingLeafEquation("*", owner));
            }
        }
    }

    private Equation addImpliedMultiplicationDiv(DivEquation at) {
        for (Equation e : at) {
            if (e instanceof WritingEquation) {
                ((WritingEquation) e).addImpliedMultiplication();
            } else if (e instanceof DivEquation) {
                e.replace(addImpliedMultiplicationDiv((DivEquation) e));
            }
        }
        return at;
    }

    private Equation convertDiv(DivEquation at) {
        for (Equation e : at) {
            if (e instanceof WritingEquation) {
                e.replace(((WritingEquation) e).convert());
            } else if (e instanceof DivEquation) {
                e.replace(convertDiv((DivEquation) e));
            }
        }
        return at;
    }
}
