package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.algebrator.eq.WritingPraEquation;
import com.example.circle.EmilyView;


/**
 * How <b>b</b>u<i>t</i>t<b><i>o</b></i>ns know what to do
 *
 * @author Emily
 */
public abstract class Action {
    public EmilyView emilyView;

    public Action(EmilyView emilyView) {
        this.emilyView = emilyView;
    }

    abstract public void act();



    protected boolean hasMatch() {
        int depth = 1;
        Equation current = emilyView.selected;
        current = current.left();
        while (true) {
            if (current != null) {
                if (current instanceof WritingPraEquation) {
                    if (!((WritingPraEquation) current).left) {
                        depth++;
                    } else {
                        depth--;
                        if (depth == 0) {
                            return true;
                        }
                    }
                }
                current = current.left();
            } else {
                return false;
            }
        }

    }


    protected void addToBlock(Equation numEq) {
        PlaceholderEquation phe = new PlaceholderEquation(emilyView);
        if (emilyView.selected.parent instanceof WritingEquation){
            // add to the parent
            int at = emilyView.selected.parent.indexOf(emilyView.selected);
            emilyView.selected.parent.add(at+1,numEq);
            emilyView.selected.parent.add(at+2,phe);
            phe.setSelected(true);
        }else if (emilyView.selected instanceof WritingEquation){
            // add to what is selected
            emilyView.selected.add(numEq);
            emilyView.selected.add(phe);
            phe.setSelected(true);
        }else{
            // replace selected with a new WritingEqution that contains selects
            Equation write = new WritingEquation(emilyView);
            Equation oldEq = emilyView.selected;
            emilyView.selected.replace(write);
            write.add(oldEq);
            write.add(numEq);
            write.add(phe);
            phe.setSelected(true);
        }
    }

    protected void tryMoveRight() {
        Equation current = getMoveRightCurrent();
        if (current instanceof DivEquation) {
            Equation oldEq = emilyView.selected;
            oldEq.remove();
            int at = current.parent.indexOf(current);
            current.parent.add(at + 1, oldEq);
        }
    }


    private Equation getMoveRightCurrent() {
        Equation current = emilyView.selected;
        boolean done = false;
        while (current.parent != null && !done) {
            if (current instanceof DivEquation) {
                done = true;
            } else {
                current = current.parent;
            }
        }
        return current;
    }

    protected boolean canMoveRight() {
        return getMoveRightCurrent() instanceof DivEquation;
    }

    protected int countEquals(Equation stupid) {
        int count = 0;
        for (Equation e : stupid) {
            if (e instanceof WritingLeafEquation && e.getDisplay(-1).equals("=")) {
                count++;
            }
        }
        return count;
    }

}
