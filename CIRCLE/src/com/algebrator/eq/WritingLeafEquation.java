package com.algebrator.eq;

import com.example.circle.SuperView;

/**
 * Created by Colin on 1/6/2015.
 */
public class WritingLeafEquation extends LeafEquation {
    public WritingLeafEquation(String display, SuperView emilyView) {
        super(emilyView);
        this.display = display;
    }

    @Override
    public Equation copy() {
        Equation result = new WritingLeafEquation(this.display, this.owner);


        return result;
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
