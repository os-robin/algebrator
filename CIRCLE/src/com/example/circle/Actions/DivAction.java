package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.EmilyView;

public class DivAction extends Action {

    public DivAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {

        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            if (l != null && !isOp(l)) {
                emilyView.selected.justRemove();
                Equation oldEq = l;
                DivEquation newEq = new DivEquation(emilyView);
                oldEq.replace(newEq);
                newEq.add(oldEq);
                newEq.add(emilyView.selected);
            } else {

            }
        } else {
            Equation oldEq = emilyView.selected;
            DivEquation newEq = new DivEquation(emilyView);
            oldEq.replace(newEq);
            newEq.add(oldEq);
            Equation placeHolder = new PlaceholderEquation(emilyView);
            newEq.add(placeHolder);
            placeHolder.setSelected(true);
        }
    }

}
