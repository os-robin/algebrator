package com.algebrator.eq;

import android.graphics.Color;
import android.graphics.Paint;

import com.example.circle.SuperView;

import java.util.ArrayList;

/**
 * Created by Colin on 1/6/2015.
 */
public class WritingLeafEquation extends LeafEquation implements LegallityCheck {
    public WritingLeafEquation(String display, SuperView emilyView) {
        super(emilyView);
        this.display = display;
    }

    @Override
    public Equation copy() {
        Equation result = new WritingLeafEquation(this.display, this.owner);


        return result;
    }

    @Override
    protected Paint getPaint() {
        // check if we are legal
        if (illegal()){
            Paint temp = new Paint(textPaint);
            temp.setColor(Color.RED);
            return temp;
        }
        return super.getPaint();
    }

    public boolean illegal() {
        Equation left = null;
        Equation right = null;

        if (parent != null) {
            int at = parent.indexOf(this);
            if (at != 0){
                left = parent.get(at-1);
            }
            if (at != parent.size()-1){
                right = parent.get(at+1);
            }
            ArrayList<String> ops = new ArrayList<String>();
            ops.add("+");ops.add("*");ops.add("=");

            if (ops.contains(getDisplay(-1))) {
                // if it is directly contained by a div
                if (parent!= null && parent instanceof DivEquation){
                    return true;
                }

                for (Equation e:new Equation[]{left,right}) {
                    // if we have nothing next to an op
                    if (e == null) {
                        return true;
                    }
                    // if we have two ops next to each other
                    if (ops.contains(e.getDisplay(-1))) {
                        return true;
                    }
                }
                // if you have +)
                if (left instanceof WritingPraEquation && ((WritingPraEquation) left).left){
                    return true;
                }
                // or (+
                if (right instanceof WritingPraEquation && !((WritingPraEquation) right).left){
                    return true;
                }
            }

            // if we have nothing left of a minus
            if (this.getDisplay(-1).equals("-")){
                if (right == null){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * looking form the left
     *
     * @return
     */
    public boolean isOpLeft() {
        if (parent instanceof DivEquation && parent.indexOf(this) == 0) {
            return true;
        }
        String dis = getDisplay(-1);
        if (dis.equals("+") || dis.equals("-") || dis.equals("*") || dis.equals("=")) {
            return true;
        }
        return false;
    }

    /**
     * looking form the left
     *
     * @return
     */
    public boolean isOpRight() {
        if (parent instanceof DivEquation && parent.indexOf(this) == 1) {
            return true;
        }
        String dis = getDisplay(-1);
        if (dis.equals("+") || dis.equals("-") || dis.equals("*") || dis.equals("=")) {
            return true;
        }
        return false;
    }

}
