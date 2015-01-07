package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.WritingLeafEquation;
import com.example.circle.EmilyView;


/**
 * How <b>b</b>u<i>t</i>t<b><i>o</b></i>ns know what to do
 *
 * @author Emily
 */
public abstract class Action {
    public EmilyView emilyView;

    public Action(EmilyView emilyView) {
        this.emilyView = emilyView;
    }

    abstract public void act();

    protected boolean isOp(Equation l) {
        if (l instanceof WritingLeafEquation) {
            if (l.parent instanceof DivEquation) {
                return true;
            }
            String dis = l.getDisplay(-1);
            if (dis.equals("+") || dis.equals("-") || dis.equals("*")) {
                return true;
            }
        }
        return false;
    }

}
