package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.example.circle.EmilyView;

public class MinusAction extends Action {

    public MinusAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            if (l != null) {
                if (!(l.parent instanceof DivEquation)) {
                    if (l instanceof WritingLeafEquation && l.getDisplay(-1) == "+") {
                        l.justRemove();
                        ;
                    }
                    Equation newEq = new WritingLeafEquation("-", emilyView);
                    emilyView.insert(newEq);
                } else {
                    Equation oldEq = emilyView.selected;
                    Equation holder = new WritingEquation(emilyView);
                    Equation newEq = new WritingLeafEquation("-", emilyView);
                    oldEq.replace(holder);
                    holder.add(newEq);
                    holder.add(oldEq);
                    oldEq.setSelected(true);
                }
            } else {
                if (l instanceof WritingLeafEquation && l.getDisplay(-1) == "+") {
                    l.justRemove();
                    ;
                }
                Equation newEq = new WritingLeafEquation("-", emilyView);
                emilyView.insert(newEq);
            }

        }

//		    if (emilyView.selected instanceof PlaceholderEquation) {
//            Equation old = emilyView.selected;
//            Equation toAdd = new MinusEquation(emilyView);
//            old.replace(toAdd);
//            toAdd.add(old);
//            old.setSelected(true);
//		} else if(emilyView.selected.parent instanceof AddEquation){
//            Equation add = emilyView.selected.parent;
//            Equation neg = new MinusEquation(emilyView);
//            Equation place = new PlaceholderEquation(emilyView);
//            neg.add(place);
//            add.add(neg);
//            place.setSelected(true);
//        }else{
//            Equation old = emilyView.selected;
//            Equation add = new AddEquation(emilyView);
//            old.replace(add);
//            add.add(old);
//            Equation neg = new MinusEquation(emilyView);
//            Equation place = new PlaceholderEquation(emilyView);
//            neg.add(place);
//            add.add(neg);
//            place.setSelected(true);
//        }
    }

}

