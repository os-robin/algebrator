package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.algebrator.eq.WritingPraEquation;
import com.example.circle.EmilyView;

public class DivAction extends Action {

    public DivAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {

        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            boolean can = l != null;
            if (can && (l instanceof WritingLeafEquation)) {
                can = !((WritingLeafEquation) l).isOpLeft();
            }

            if (can && l instanceof WritingPraEquation) {
                if (((WritingPraEquation) l).left) {
                    can = false;
                } else {
                    // we need to select the hole ( .. )
                    can = false;
                    ((WritingPraEquation) l).selectBlock();
                }
            }
            if (can) {
                emilyView.selected.justRemove();
                Equation oldEq = l;
                DivEquation newEq = new DivEquation(emilyView);
                oldEq.replace(newEq);
                newEq.add(oldEq);
                newEq.add(emilyView.selected);
            }
        }
        if (!(emilyView.selected instanceof PlaceholderEquation)) {
            boolean can = countEquals(emilyView.selected) == 0;
            if (emilyView.selected instanceof WritingEquation) {
                if (emilyView.selected.get(0) instanceof WritingLeafEquation) {
                    can = can && !((WritingLeafEquation) emilyView.selected.get(0)).isOpRight();
                }
            }
            if (emilyView.selected instanceof WritingEquation) {
                if (emilyView.selected.get(emilyView.selected.size() - 1) instanceof WritingLeafEquation) {
                    can = can && !((WritingLeafEquation) emilyView.selected.get(emilyView.selected.size() - 1)).isOpLeft();
                }
            }

            if (can) {

                Equation oldEq = emilyView.selected;
                DivEquation newEq = new DivEquation(emilyView);
                if (oldEq.parent == null) {
                    Equation writeEq = new WritingEquation(emilyView);
                    writeEq.add(newEq);
                    oldEq.replace(writeEq);
                } else {
                    oldEq.replace(newEq);
                }
                newEq.add(oldEq);
                Equation placeHolder = new PlaceholderEquation(emilyView);
                newEq.add(placeHolder);
                placeHolder.setSelected(true);
            }
        }
    }

}
