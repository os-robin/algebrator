package com.example.circle.Actions;

import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.example.circle.EmilyView;

/**
 * Created by Colin on 1/7/2015.
 */
public class EqualsAction extends Action {
    public EqualsAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            // we can't add it if there is nothing to the left
            boolean can = l != null;
            // we can't add if the last char was an op
            if (l instanceof WritingLeafEquation) {
                can = can && !((WritingLeafEquation) l).isOpLeft();
            }
            // if the root equation only hold one
            can = can && countEquals(emilyView.stupid) == 0;

            // we can't add if there is an unmatched (
            can = can && !hasMatch();
            // we can't add if we are not adding to the rootWriteEquation
            if (can) {
                can = can && l.parent.parent == null;
                // but we can move out
                if (emilyView.selected.right() == null) {
                    can = true;
                    while (canMoveRight()) {
                        tryMoveRight();
                    }
                }
            }

            // TODO we should moveright if that would help

            if (can) {
                Equation newEq = new WritingLeafEquation("=", emilyView);
                emilyView.insert(newEq);
            }
        }

        //TODO this probably want's an else
    }


}
