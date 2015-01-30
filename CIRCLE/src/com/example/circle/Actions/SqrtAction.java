package com.example.circle.Actions;

import com.algebrator.eq.BinaryEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.algebrator.eq.WritingPraEquation;
import com.algebrator.eq.WritingSqrtEquation;
import com.example.circle.EmilyView;

/**
 * Created by Colin on 1/29/2015.
 */
public class SqrtAction  extends Action{
    public SqrtAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            Equation newEq = new WritingSqrtEquation(emilyView);
            if (l != null) {
                    if (!(l.parent instanceof BinaryEquation)) {
                        emilyView.insert(newEq);
                    } else {
                        Equation oldEq = emilyView.selected;
                        Equation holder = new WritingEquation(emilyView);
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    }
            } else {
                    emilyView.insert(newEq);
            }
        } else {
            if (emilyView.selected != null) {
                if (emilyView.selected instanceof WritingEquation) {
                    //add WritingSqrtEquation left of the block
                    emilyView.selected.add(0, new WritingSqrtEquation(emilyView));
                    // add ) right of the bock
                    emilyView.selected.add(new WritingPraEquation(false, emilyView));
                } else {

                }
            }
        }
    }
}
