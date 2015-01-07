package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.EmilyView;

public class DeleteAction extends Action {

    public String num;

    public DeleteAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            if (l != null) {
                if (l.parent instanceof DivEquation) {
                    l.parent.replace(l);
                    int pos = l.parent.indexOf(l);
                    l.parent.add(pos + 1, emilyView.selected);
                } else if (l instanceof NumConstEquation) {
                    if (((NumConstEquation) l).getDisplay(-1).length() != 0) {
                        String toSet = (String) ((NumConstEquation) l).getDisplay(-1)
                                .subSequence(0,
                                        ((NumConstEquation) l).getDisplay(-1)
                                                .length() - 1);
                        if (toSet.length() != 0 && toSet.charAt(0) == '-') {
                            toSet = toSet.substring(1, toSet.length());
                        }
                        ((NumConstEquation) l).setDisplay(toSet);
                    }
                    if (((NumConstEquation) l).getDisplay(0).length() == 0) {
                        l.remove();
                    }
                } else {
                    l.remove();
                }
            }
        } else {
            // if they have a stack of stuff selected kill it all and replace it wiht a new Placeholder
            Equation newEq = new PlaceholderEquation(emilyView);
            emilyView.selected.replace(newEq);
            newEq.setSelected(true);
        }
        //else if (emilyView.selected instanceof EqualsEquation){
        //	emilyView.selected.remove();
        //}
    }

}
