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

    char[] timesUnicode = { '\u00D7'};
    String times = new String(timesUnicode);


    public WritingEquation(SuperView o) {
        super(o);
        display = "\"";
    }


    public boolean deepLegal() {
        return deepLegal(this);
    }

    private boolean deepLegal(Equation eq) {
        for (Equation e : eq) {
            if (e instanceof WritingEquation) {
                if (!((WritingEquation) e).deepLegal()) {
                    return false;
                }
            } else if (e instanceof WritingLeafEquation) {
                if (((WritingLeafEquation) e).illegal()) {
                    return false;
                }
            } else if (e instanceof DivEquation) {
                if (!deepLegal(e)) {
                    return false;
                }
            }
        }
        return true;
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
    protected float privateMeasureWidth() {
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
        //boolean openParen = false;
        boolean wasParen = false;
        int minus = 0;

        for (int i = 0; i < size(); i++) {
            Equation at = get(i);
            Log.i("root", (root == null ? "null" : root.toString()));
            Log.i("at", at.toString());
            if (currentToAdd == null) {
                Log.i("left", (left == null ? "null" : left.toString()));
                if (at.getDisplay(-1).equals("+")) {
                    currentToAdd = new AddEquation(owner);
                } else if (at.getDisplay(-1).equals(times)) {
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
                        boolean wasSqrt =at instanceof WritingSqrtEquation;

                        Equation temp = ((WritingPraEquation) at).popBlock();
                        temp.remove(0);
                        temp.remove(temp.size() - 1);
                        // we need to remove the first and last
                        left = convert(temp);
                        if (wasSqrt){
                            Equation oldEq = left;
                            left = new PowerEquation(owner);
                            left.add(oldEq);
                            left.add(new NumConstEquation(.5,owner));
                        }
                        wasParen = true;

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
                    } else {
                        root.add(currentToAdd);
                    }
                    if (left != null) {
                        currentToAdd.add(left);
                    }
                    if (currentToAdd instanceof EqualsEquation) {
                        currentToAdd = null;
                    }
                    left = null;
                }
            } else {
                if (at instanceof WritingLeafEquation) {
                    Equation newEq = null;
                    if (at.getDisplay(-1).equals("+")) {
                        newEq = new AddEquation(owner);
                    } else if (at.getDisplay(-1).equals(times)) {
                        newEq = new MultiEquation(owner);
                    } else if (at.getDisplay(-1).equals("=")) {
                        newEq = new EqualsEquation(owner);
                    } else if (at instanceof WritingPraEquation && ((WritingPraEquation) at).left) {
                        //openParen = true;
                    } else if (at.getDisplay(-1).equals("-")) {
                        if (!get(i - 1).getDisplay(-1).equals(times)) {
                            newEq = new AddEquation(owner);
                        }
                        minus++;
                    }
                    if (newEq != null) {
                        if (newEq.getClass().equals(currentToAdd.getClass())) {
                            //we don't worry about it
                        } else {
                            if (newEq instanceof MultiEquation) {
                                //Equation last = get(i - 1);
                                //(last instanceof WritingPraEquation && !((WritingPraEquation) last).left) ||
                                if (wasParen) {
                                    if (currentToAdd.parent instanceof MultiEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        if (currentToAdd.parent == null) {
                                            newEq.add(currentToAdd);
                                            root = newEq;
                                        } else {
                                            Equation oldEq = currentToAdd;
                                            oldEq.replace(newEq);
                                            newEq.add(oldEq);
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
                                //openParen = false;
                            } else if (newEq instanceof AddEquation) {
                                //if (openParen) {
                                //    Equation moving = currentToAdd.get(currentToAdd.size() - 1);
                                //    currentToAdd.justRemove(moving);
                                //    if (currentToAdd.parent instanceof AddEquation) {
                                //        newEq = currentToAdd.parent;
                                //   } else {
                                //        currentToAdd.add(newEq);
                                //    }
                                //    newEq.add(moving);
                                //    currentToAdd = newEq;
                                //} else {
                                    if (currentToAdd.parent instanceof AddEquation) {
                                        newEq = currentToAdd.parent;
                                    } else {
                                        if (currentToAdd.parent == null) {
                                            newEq.add(currentToAdd);
                                            root = newEq;
                                        } else {
                                            Equation oldEq = currentToAdd;
                                            oldEq.replace(newEq);
                                            newEq.add(oldEq);
                                        }
                                    }
                                    currentToAdd = newEq;
                                //}
                                //openParen = false;
                            } else if (newEq instanceof EqualsEquation) {
                                Equation before;
                                if (root != null) {
                                    before = root;
                                } else {
                                    before = left;
                                }
                                newEq.add(before);
                                root = newEq;
                                currentToAdd = null;
                                left = null;
                            }
                        }
                    } else {
                        if (at instanceof WritingPraEquation && ((WritingPraEquation) at).left) {
                            boolean wasSqrt = at instanceof WritingSqrtEquation;
                            Equation temp = ((WritingPraEquation) at).popBlock();
                            temp.remove(0);
                            temp.remove(temp.size() - 1);
                            // we need to remove the first and last
                            at = convert(temp);
                            if (wasSqrt){
                                Equation oldEq = at;
                                at = new PowerEquation(owner);
                                at.add(oldEq);
                                at.add(new NumConstEquation(.5,owner));
                            }
                            wasParen = true;

                            while (minus > 0) {
                                Equation neg = new MinusEquation(owner);
                                neg.add(at);
                                at = neg;
                                minus--;
                            }
                            currentToAdd.add(at);
                        }
                    }
                } else {

                    at = convert(at);

                    while (minus > 0) {
                        Equation neg = new MinusEquation(owner);
                        neg.add(at);
                        at = neg;
                        minus--;
                    }
                    currentToAdd.add(at);
                }
                if (!(at instanceof WritingPraEquation)) {
                    wasParen = false;
                }
            }
        }
        if (root == null) {
            Log.i("conversion result-left", left.toString());
            return left;
        } else {

            if (root.size() == 1) {
                // if have something of the form =---1
                root.add(left);
            }
            Log.i("conversion result", root.toString());
            return root;
        }
    }

    private Equation convert(Equation at) {

        if (at instanceof WritingEquation) {
            return ((WritingEquation) at).convert();
        } else if (at instanceof BinaryEquation) {
            // convert the top and the bottom
            // and then add it
            at = convertBinary((BinaryEquation) at);
            return at;
        } else {
            return at;
        }
    }

    private void addImpliedMultiplication() {
        // we interate over and handle xx or 2( or 2x or )x or x2 or 2(2/4) or (2/4)2
        for (int i = 0; i < size() - 1; i++) {
            Equation left = get(i);
            if (left instanceof BinaryEquation) {
                left = addImpliedMultiplicationDiv((BinaryEquation) left);
            }
            Equation right = get(i + 1);
            if (right instanceof BinaryEquation) {
                right = addImpliedMultiplicationDiv((BinaryEquation) right);
            }
            if ((left instanceof BinaryEquation || left instanceof NumConstEquation || left instanceof VarEquation || (left instanceof WritingPraEquation && !((WritingPraEquation) left).left)) &&
                    (right instanceof BinaryEquation || right instanceof NumConstEquation || right instanceof VarEquation || (right instanceof WritingPraEquation && ((WritingPraEquation) right).left))) {
                // we need to insert a *
                add(i + 1, new WritingLeafEquation(times, owner));
            }
        }
    }

    private Equation addImpliedMultiplicationDiv(BinaryEquation at) {
        for (Equation e : (Equation) at) {
            if (e instanceof WritingEquation) {
                ((WritingEquation) e).addImpliedMultiplication();
            } else if (e instanceof BinaryEquation) {
                e.replace(addImpliedMultiplicationDiv((BinaryEquation) e));
            }
        }
        return (Equation) at;
    }

    private Equation convertBinary(BinaryEquation at) {
        for (Equation e : (Equation) at) {
            if (e instanceof WritingEquation) {
                e.replace(((WritingEquation) e).convert());
            } else if (e instanceof BinaryEquation) {
                e.replace(convertBinary((BinaryEquation) e));
            }
        }
        return (Equation) at;
    }
}
