package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.PowerEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.algebrator.eq.WritingPraEquation;
import com.example.circle.EmilyView;

public class DivAction extends BinaryAction {

    public DivAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        Equation newEq = new DivEquation(emilyView);

        act(newEq);
    }

}
