package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.example.circle.SuperView;

import java.util.ArrayList;

/**
 * Created by Colin on 1/7/2015.
 */
public class WritingPraEquation extends WritingLeafEquation {
    public boolean left;


    public WritingPraEquation(boolean left, SuperView emilyView) {
        super((left ? "(" : ")"), emilyView);
        this.left = left;
    }

    @Override
    public Equation copy() {
        Equation result = new WritingPraEquation(this.left, this.owner);
        return result;
    }

    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        lastPoint = new ArrayList<Point>();
        float totalWidth = measureWidth();
        float currentX = 0;
        Paint temp = getPaint();
        lastPoint.add(new Point((int) x, (int) y));
        drawParentheses(canvas, x, y, temp, left);
    }

    @Override
    public boolean illegal() {
        return getMatch()== null;
    }


    protected void drawParentheses(Canvas canvas, float x, float y, Paint temp, boolean left) {
        Paint ptemp = new Paint(temp);
        ptemp.setStrokeWidth(3);
        float uh = measureHeightUpper();
        float lh = measureHeightLower();
        //TODO sace 9 by dpi
        if (left) {
            //left side
            canvas.drawLine(x + 3, y - uh  + 3, x + 9, y - uh + 3, ptemp);
            canvas.drawLine(x + 3, y - uh  + 3, x + 3, y + lh - 3, ptemp);
            canvas.drawLine(x + 3, y + lh - 3, x + 9, y + lh - 3, ptemp);
        } else {
            //right side
            canvas.drawLine(x - 3, y - uh  + 3, x - 9, y - uh  + 3, ptemp);
            canvas.drawLine(x - 3, y - uh  + 3, x - 3, y + lh  - 3, ptemp);
            canvas.drawLine(x - 3, y + lh  - 3, x - 9, y + lh  - 3, ptemp);
        }
    }

    @Override
    public float measureHeightUpper(){
        return measureHeightHelper(true);
    }

    @Override
    public float measureHeightLower(){
        return measureHeightHelper(false);
    }

    public float measureHeightHelper(boolean upper) {
        float totalHeight = myHeight/2;

        // if left
        // move left through parent
        // match our height to the tallest thing left of us that is:
        // not a open
        boolean go = true;
        int depth = 1;
        Equation current = this;
        if (left) {
            current = current.right();
            while (go && depth != 0) {
                if (current != null) {
                    if (current instanceof WritingPraEquation) {
                        if (((WritingPraEquation) current).left) {
                            if (depth == 1) {
                                totalHeight = Math.max(totalHeight,(upper?current.measureHeightUpper():current.measureHeightLower()) + PARN_HEIGHT_ADDITION/2);
                            }
                            depth++;
                        } else {
                            depth--;
                            if (depth == 0) {
                                go = false;
                            }
                        }
                    } else {
                        if (depth == 1) {
                            totalHeight = Math.max(totalHeight, (upper?current.measureHeightUpper():current.measureHeightLower()) + PARN_HEIGHT_ADDITION/2);
                        }
                    }
                    current = current.right();
                } else {
                    go = false;
                }
            }
        } else {
            current = current.left();
            while (go && depth != 0) {
                if (current != null) {
                    if (current instanceof WritingPraEquation) {
                        if (!((WritingPraEquation) current).left) {
                            depth++;
                        } else {
                            depth--;
                            if (depth == 0) {
                                totalHeight = (upper?current.measureHeightUpper():current.measureHeightLower());
                                go = false;
                            }
                        }
                    }
                    current = current.left();
                } else {
                    go = false;
                    Log.e("", "this does not have a matching left para");
                }
            }
        }
        return totalHeight;
    }

    public Equation selectBlock() {
        // we need to deselect whatever is selected
        owner.removeSelected();

        Equation toSelect = popBlock();

        // and select the new equation
        toSelect.setSelected(true);

        return toSelect;
    }

    private Equation getMatch() {
        int depth = 1;
        Equation current = this;
        if (left) {
            current = current.right();
            while (depth != 0 && current != null) {
                if (current instanceof WritingPraEquation) {
                    if (((WritingPraEquation) current).left) {
                        depth++;
                    } else {
                        depth--;
                        if (depth == 0) {
                            return current;
                        }
                    }
                }
                current = current.right();
            }
        } else {
            current = current.left();
            while (depth != 0 && current != null) {
                if (current instanceof WritingPraEquation) {
                    if (!((WritingPraEquation) current).left) {
                        depth++;
                    } else {
                        depth--;
                        if (depth == 0) {
                            return current;
                        }
                    }
                }
                current = current.left();
            }
        }
        return null;
    }

    public Equation popBlock() {

        // we need to find it's other end
        Equation eq = getMatch();
        int min = Math.min(this.parent.indexOf(this), this.parent.indexOf(eq));
        int max = Math.max(this.parent.indexOf(this), this.parent.indexOf(eq));
        ArrayList<Equation> list = new ArrayList<Equation>();
        for (int i=min;i<max+1;i++){
            list.add(this.parent.get(i));
        }
        Equation toSelect = null;
        if (this.parent instanceof MultiEquation) {
            toSelect = new MultiEquation(owner);
        } else if (this.parent instanceof AddEquation) {
            toSelect = new AddEquation(owner);
        } else if (this.parent instanceof WritingEquation) {
            toSelect = new WritingEquation(owner);
        }
        if (toSelect == null){
            Log.e("","ToSelect should not be null");
        }

        Equation oldEq = this.parent;

        for (Equation e : list) {
            oldEq.justRemove(e);
            toSelect.add(e);
        }

        // add it back
        oldEq.add(min, toSelect);

        return toSelect;

    }
}
