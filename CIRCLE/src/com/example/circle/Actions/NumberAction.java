package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.example.circle.EmilyView;

public class NumberAction extends Action {

    public String num;

    public NumberAction(EmilyView emilyView, String num) {
        super(emilyView);
        this.num = num;
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            Equation r = emilyView.selected.right();
            if (l != null) {
                if (!(l.parent instanceof DivEquation)) {
                    if (!(l instanceof NumConstEquation)) {
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                        emilyView.insert(newEq);
                    } else if (l instanceof NumConstEquation && !l.getDisplay(-1).equals("0")) {
                        l.setDisplay(l.getDisplay(0) + num);
                    } else if (l instanceof NumConstEquation && l.getDisplay(-1).equals("0")) {
                        l.setDisplay(num);
                    }
                } else {
                    if (r instanceof NumConstEquation) {
                        if (!r.getDisplay(-1).equals("0")) {
                            r.setDisplay(num + r.getDisplay(0));
                        } else {
                            r.setDisplay(num);
                        }
                    } else {
                        Equation oldEq = emilyView.selected;
                        Equation holder = new WritingEquation(emilyView);
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    }
                }
            } else {
                if (r instanceof NumConstEquation) {
                        if (!r.getDisplay(-1).equals("0")) {
                            r.setDisplay(num + r.getDisplay(0));
                        } else {
                            r.setDisplay(num);
                        }
                } else {
                    Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                    emilyView.insert(newEq);
                }
            }

        }
    }

}
