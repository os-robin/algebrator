package com.example.circle.Actions;

import com.algebrator.eq.BinaryEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.MonaryEquation;
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
        if (emilyView.selected.parent instanceof WritingEquation) {
            // add to the parent
            int at = emilyView.selected.parent.indexOf(emilyView.selected);
            emilyView.selected.parent.add(at + 1, numEq);
            emilyView.selected.parent.add(at + 2, phe);
            phe.setSelected(true);
        } else if (emilyView.selected instanceof WritingEquation) {
            // add to what is selected
            emilyView.selected.add(numEq);
            emilyView.selected.add(phe);
            phe.setSelected(true);
        } else {
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
        tryMove(false);
    }

    void tryMoveLeft() {
        tryMove(true);
    }

    void tryMove(boolean left) {
        Equation current = getMoveCurrent(left);
        if (current != null){
            Equation oldEq = emilyView.selected;
            oldEq.remove();
            int at = current.parent.indexOf(current);
            current.parent.add(at +(left?0:1), oldEq);
        }else {
            Equation next = (left?emilyView.selected.left():emilyView.selected.right());
            if (next != null) {
                Equation oldEq = emilyView.selected;

                while (next.size() != 0) {
                    next = next.get((left?next.size() - 1:0));
                }
                if (next.parent.equals(emilyView.selected.parent)) {
                    int at = next.parent.indexOf(next);
                    oldEq.justRemove();
                    // at does not need to be adjusted since we remove the old equation
                    next.parent.add(at, oldEq);
                } else {
                    if (next.parent instanceof BinaryEquation || next instanceof MonaryEquation) {
                        Equation oldNext = next;
                        Equation newEq = new WritingEquation(emilyView);
                        next.replace(newEq);
                        if (left){
                            newEq.add(oldNext);
                            oldEq.remove();
                            newEq.add(oldEq);
                        }else{
                            oldEq.remove();
                            newEq.add(oldEq);
                            newEq.add(oldNext);

                        }
                    } else {
                        int at = next.parent.indexOf(next) + (left?1:0);
                        oldEq.remove();
                        next.parent.add(at, oldEq);
                    }
                }
            }
        }
    }


    private Equation getMoveCurrent(boolean left) {
        Equation current = emilyView.selected;
        while (current.parent != null && current.parent.indexOf(current) == (left?0:current.parent.size()-1)) {
            if (current instanceof BinaryEquation) {
                return current;
            } else {
                current = current.parent;
            }
        }
        return null;
    }

    protected boolean canMoveRight() {
        return canMove(false);
    }

    private boolean canMove(boolean left){
        return getMoveCurrent(left) instanceof BinaryEquation;
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
