package com.example.circle.Actions;

import com.algebrator.eq.Equation;
import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
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
            if (l != null) {
                if (l instanceof NumConstEquation && l.getDisplay(-1).contains(".") == false) {
                    String toSet = l.getDisplay(0) + dec;
                    l.setDisplay(toSet);
                } else {
                    NumConstEquation numEq = new NumConstEquation(0, emilyView);
                    numEq.setDisplay(numEq.getDisplay(0) + ".");
                    emilyView.insert(numEq);
                }
            } else {
                NumConstEquation numEq = new NumConstEquation(0, emilyView);
                numEq.setDisplay(numEq.getDisplay(0) + ".");
                emilyView.insert(numEq);
            }
        }else{
            NumConstEquation numEq = new NumConstEquation(0, emilyView);
            numEq.setDisplay(numEq.getDisplay(0) + ".");
            addToBlock(numEq);

        }

    }


}

