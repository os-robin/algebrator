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
        if (isSqrt()) {
            return sqrtMeasureWidth();
        } else {
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
    }

    private float sqrtMeasureWidth() {
        return get(0).measureWidth() + width_addition;
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

        // this is really dangerous
        if (isSqrt() && this.get(1) instanceof DivEquation) {
            Equation oldEq = this.get(1);
            Equation newEq = new NumConstEquation(.5, owner);
            if (oldEq.selected){
                newEq.setSelected(true);
            }
            oldEq.replace(newEq);
        }

        boolean wasEvenRoot = isEven();
        boolean wasEven = get(1) instanceof NumConstEquation && ((NumConstEquation) get(1)).getValue() %2 ==0;

        // if it is a power equation
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

            if (result instanceof  NumConstEquation && ((NumConstEquation) result).getValue()==1){
                get(0).replace(get(0).get(0));
            }else {
                get(0).get(1).replace(result);
            }

            if (wasEvenRoot){
                result = new PlusMinusEquation(owner);
                result.add(get(0));
                replace(result);
            }else {
                replace(get(0));
            }
        } else if (get(0) instanceof DivEquation){
            result = new DivEquation(owner);
            Equation top = new PowerEquation(owner);
            top.add(this.get(0).get(0));
            top.add(this.get(1).copy());
            result.add(top);
            Equation bot = new PowerEquation(owner);
            top.add(this.get(0).get(1));
            top.add(this.get(1).copy());
            result.add(bot);
            replace(result);
        }else{
            // if it's an add split it up
            // if it's numb and number you can just do it
            // if it's numb on top
            boolean neg = false;

            Equation temp = get(1);
            while (temp instanceof MinusEquation) {
                temp = temp.get(0);
                neg = !neg;
            }

            if (neg) {
                Equation oldEq = this;
                Equation newEq = new NumConstEquation(1, owner);
                result = new DivEquation(owner);
                result.add(newEq);
                result.add(oldEq);
            } else {

                // if the right is a number
                if (temp instanceof NumConstEquation) {

                    // if you have something like ((2^2)^0.5)
                    double value = ((NumConstEquation) temp).getValue();

                    // if it is an int
                    if ((value == Math.floor(value)) && !Double.isInfinite(value)) {
                        int val = (int) Math.floor(value);

                        // if left is a var write x^3 -> x*x*x
                        if (val > 1 && get(0).reallyInstanceOf(VarEquation.class)) {
                            result = new MultiEquation(owner);
                            for (int i = 0; i < val; i++) {
                                result.add(get(0).copy());
                            }
                        } else {
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

                            if (wasEven && result instanceof PlusMinusEquation){
                                result = result.get(0);
                            }
                        }
                    } else {
                        boolean innerNeg = false;
                        boolean plusMinus = false;

                        Equation leftTemp = get(0);
                        while (leftTemp instanceof MinusEquation || leftTemp instanceof PlusMinusEquation) {
                            if (leftTemp instanceof  MinusEquation) {
                                innerNeg = !innerNeg;
                            }else if (leftTemp instanceof  PlusMinusEquation){
                                plusMinus = true;
                            }
                            leftTemp = leftTemp.get(0);
                        }

                        if (leftTemp instanceof NumConstEquation && (!innerNeg || plusMinus)) {

                            double leftValue = ((NumConstEquation) leftTemp).getValue();

                            double resultValue = Math.pow(leftValue, value);

                            result = new NumConstEquation(resultValue, owner);
                        }
                    }
                }
            }
            if (result != null) {
                if (wasEvenRoot){
                    Equation oldEq = result;
                    result = new PlusMinusEquation(owner);
                    result.add(oldEq);
                }
                replace(result);
            }
        }
    }

    // is used to tell if we need to +/- cases A^(1/(O*2))
    private boolean isEven() {
        if (get(1) instanceof NumConstEquation && 1/((NumConstEquation) get(1)).getValue() %2 ==0) {
            return true;
        }
        // there are other cases but we do not worry about them
        return false;

    }

    private boolean isSqrt() {
        if (get(1) instanceof NumConstEquation && ((NumConstEquation) get(1)).getValue() == .5) {
            return true;
        }
        if (get(1) instanceof DivEquation && get(1).get(0) instanceof NumConstEquation && ((NumConstEquation) get(1).get(0)).getValue() == 1 && get(1).get(1) instanceof NumConstEquation && ((NumConstEquation) get(1).get(1)).getValue() == 2) {
            return true;
        }
        // there are other cases but we do not worry about them
        return false;
    }

    // y is not centered is that a problem - this at a problem
    // yes i think it is
    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        Paint temp = getPaint();
        lastPoint = new ArrayList<Point>();
        if (isSqrt()) {
            sqrtDraw(canvas, x, y, temp);
        } else {
            float totalWidth = measureWidth();
            float atX = x - (totalWidth / 2);
            float atY = y;

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
    }


    public static float height_addition = 20;
    public static float width_addition = 60;

    public static void sqrtSignDraw(Canvas canvas, float atX, float y, Paint p ,Equation e){
        sqrtSignDraw(canvas, atX, y, p, e.measureHeightLower(), e.measureHeightUpper(), e.x + e.measureWidth() / 2);
    }

    public static void sqrtSignDraw(Canvas canvas, float atX, float y, Paint p ,float lower, float upper,float end){
        if (canvas != null) {
            // TODO scale by dpi
            p.setStrokeWidth(3);
            canvas.drawLine(atX, y, atX + width_addition / 3f, y, p);
            atX += width_addition / 3f;
            canvas.drawLine(atX, y, atX + width_addition / 3f, y + lower, p);
            atX += width_addition / 3f;
            canvas.drawLine(atX, y + lower, atX + width_addition / 3f, y - upper, p);
            atX += width_addition / 3f;
            canvas.drawLine(atX, y - upper, end, y - upper, p);
        }
    }


    private void sqrtDraw(Canvas canvas, float x, float y, Paint p) {
        // let's start by drawing the sqrt sign
        float atX = x - measureWidth() / 2;
            sqrtSignDraw(canvas, atX, y, p, this);

        // now let's draw the content
        get(0).draw(canvas, x + width_addition / 2, y);

        // now we need to put the last point
        Point point = new Point();
        point.x = (int) (x - measureWidth() / 2 + width_addition / 6);
        point.y = (int) (y - (measureHeightUpper() / 2));
        //canvas.drawLine(point.x + 3, point.y, point.x - 3, point.y, p);
        lastPoint.add(point);

        //update the location of thing
        get(1).x = point.x;
        get(1).y = point.y;
    }

    public float measureHeightLower() {
        if (isSqrt()) {
            return sqrtMeasureHeightLower();
        } else {
            float result = get(0).measureHeightLower();
            if (parenthesis()) {
                result += PARN_HEIGHT_ADDITION / 2f;
            }
            return result;
        }
    }

    private float sqrtMeasureHeightLower() {
        return get(0).measureHeightLower();
    }

    public float measureHeightUpper() {
        if (isSqrt()) {
            return sqrtMeasureHeightUpper();
        } else {

            float r0 = get(0).measureHeightUpper();


            float r1 = -get(0).measureHeightLower() + (get(0).measureHeight() * 2f / 3f) + get(1).measureHeight();

            float result = Math.max(r0, r1);
            if (parenthesis()) {
                result += PARN_HEIGHT_ADDITION / 2f;
            }
            return result;
        }
    }

    private float sqrtMeasureHeightUpper() {
        return get(0).measureHeightUpper() + height_addition;
    }

    @Override
    public float measureHeight() {
        return measureHeightLower() + measureHeightUpper();
    }
}
