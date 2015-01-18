package com.example.circle.Actions;

import com.algebrator.eq.BinaryEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.example.circle.EmilyView;

public class DecimalAction extends Action {

    public String dec;

    public DecimalAction(EmilyView emilyView, String dec) {
        super(emilyView);
        this.dec = dec;
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            if ((l != null) &&
                    (l instanceof NumConstEquation && l.getDisplay(-1).contains(".") == false) ){
                 if (
                    !(l.parent instanceof BinaryEquation && l.parent.indexOf(l) == 0)) {
                     String toSet = l.getDisplay(0) + dec;
                     l.setDisplay(toSet);
                 }else{
                     Equation oldEq = emilyView.selected;
                     Equation holder = new WritingEquation(emilyView);
                     Equation newEq = getEq();
                     oldEq.replace(holder);
                     holder.add(newEq);
                     holder.add(oldEq);
                     oldEq.setSelected(true);
                 }
            } else {
                emilyView.insert(getEq());
            }
        } else {
            if (emilyView.selected != null) {
                addToBlock(getEq());
            }

        }
    }

    private Equation getEq() {
        NumConstEquation numEq = new NumConstEquation(0, emilyView);
        numEq.setDisplay(numEq.getDisplay(0) + ".");
        return numEq;
    }


}

