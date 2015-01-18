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
 * Created by Colin on 1/10/2015.
 */
public class PowerEquation extends Operation implements BinaryEquation {

    public PowerEquation(SuperView owner) {
        super(owner);

        display = "^";
        myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
        myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;
    }

    @Override
    public Equation copy() {
        Equation result = new PowerEquation(this.owner);
        // pass selected?

        // copy all the kiddos and set this as their parent
        for (int i = 0; i < this.size(); i++) {
            result.add(this.get(i).copy());
            result.get(i).parent = result;
        }
        return result;
    }

    protected float getScale(Equation e) {
        if (indexOf(e) == 1) {
            return 0.75f;
        } else {
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
    public void integrityCheck() {
        if (size() != 2) {
            Log.e("ic", "this should be size 2");
        }
    }

    @Override
    public void tryOperator(ArrayList<Equation> eqs) {
        //TODO handle inbeddedness
        String db = "";
        for (Equation e : eqs) {
            db += e.toString() + ",";
        }
        Log.i("try op power", (eqs.size() == 0 ? "no eqs passed in" : db));


        Equation result = null;


        if (get(0) instanceof PowerEquation) {
            // we multi

            HashSet<MultiCountData> left = new HashSet<MultiCountData>();
            Operations.findEquation(get(0).get(1).copy(), left);
            HashSet<MultiCountData> right = new HashSet<MultiCountData>();
            Operations.findEquation(get(1).copy(), right);

            left = Operations.Multiply(left, right);

            if (left.size() == 1) {
                MultiCountData mine = ((MultiCountData) left.toArray()[0]);
                result = mine.getEquation(owner);
            } else if (left.size() > 1) {
                result = new AddEquation(owner);
                for (MultiCountData e : left) {
                    result.add(e.getEquation(owner));
                }
            }

            this.get(0).get(1).replace(result);

            this.replace(get(0));
        } else {
            // if it's and add split it up
            // if it's numb and number you can just do it
            // if it's numb on top
            boolean neg = false;

            Equation temp = get(1);
            while (temp instanceof MinusEquation) {
                temp = temp.get(0);
                neg = !neg;
            }


            if (temp instanceof NumConstEquation) {

                // if you have something like ((2^2)^0.5)
                double value = ((NumConstEquation) temp).getValue();


                if ((value == Math.floor(value)) && !Double.isInfinite(value)) {
                    // int
                    int val = (int) Math.floor(value);

                    HashSet<MultiCountData> left = new HashSet<MultiCountData>();
                    left.add(new MultiCountData(new NumConstEquation(1, owner)));
                    for (int i = 0; i < val; i++) {
                        HashSet<MultiCountData> right = new HashSet<MultiCountData>();
                        Operations.findEquation(get(0).copy(), right);
                        left = Operations.Multiply(left, right);
                    }
                    if (left.size() == 1) {
                        MultiCountData mine = ((MultiCountData) left.toArray()[0]);
                        result = mine.getEquation(owner);
                    } else if (left.size() > 1) {
                        result = new AddEquation(owner);
                        for (MultiCountData e : left) {
                            result.add(e.getEquation(owner));
                        }
                    }
                }else{
                    boolean innerNeg = false;

                    Equation leftTemp = get(0);
                    while (leftTemp instanceof MinusEquation) {
                        leftTemp = leftTemp.get(0);
                        innerNeg = !innerNeg;
                    }

                    if (leftTemp instanceof NumConstEquation) {

                        double leftValue = ((NumConstEquation) leftTemp).getValue()*(innerNeg?-1:1);

                        double resultValue = Math.pow(leftValue,value);

                        result = new NumConstEquation(resultValue,owner);
                    }


                }
                if (neg) {
                    Equation oldEq = result;
                    Equation newEq = new NumConstEquation(1, owner);
                    result = new DivEquation(owner);
                    result.add(newEq);
                    result.add(oldEq);
                }
                this.replace(result);
            }


            // for the bottom and the top
            // find all the equations on each side


            // multiply && combine like terms


        }
    }

    // y is not centered is that a problem - this at a problem
    // yes i think it is
    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        lastPoint = new ArrayList<Point>();
        float totalWidth = measureWidth();
        float atX = x - (totalWidth / 2);
        float atY = y;
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

            if (i == 1) {
                // we want the bottom of 1 to be at 2/3 height of 0
                // what's the math then
                float baseLoc = get(0).measureHeightLower() - (get(0).measureHeight() * 2f / 3f);
                atY += baseLoc - get(i).measureHeightLower();
            }

            get(i).draw(canvas, atX, atY);
            if (i == 1) {
                Equation tempEq = get(i);
                while (tempEq instanceof MinusEquation) {
                    tempEq = tempEq.get(0);
                }

                if (tempEq instanceof NumConstEquation) {
                    Point point = new Point();
                    point.x = (int) atX;
                    point.y = (int) atY;
                    lastPoint.add(point);
                }
            }
            atX += (currentWidth / 2);
        }
    }

    protected float measureHeightLower() {
        float result = get(0).measureHeightLower();
        if (parenthesis()) {
            result += PARN_HEIGHT_ADDITION / 2f;
        }
        return result;
    }

    protected float measureHeightUpper() {
        float r0 = get(0).measureHeightUpper();


        float r1 = -get(0).measureHeightLower() + (get(0).measureHeight() * 2f / 3f) + get(1).measureHeight();

        float result = Math.max(r0, r1);
        if (parenthesis()) {
            result += PARN_HEIGHT_ADDITION / 2f;
        }
        return result;
    }

    @Override
    public float measureHeight() {
        return measureHeightLower() + measureHeightUpper();
    }
}
