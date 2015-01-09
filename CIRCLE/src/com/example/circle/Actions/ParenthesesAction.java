package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.algebrator.eq.WritingPraEquation;
import com.example.circle.EmilyView;

public class ParenthesesAction extends Action {

    private boolean left;

    public ParenthesesAction(EmilyView emilyView, boolean left) {
        super(emilyView);
        this.left = left;
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            Equation newEq = new WritingPraEquation(left, emilyView);
            if (l != null) {
                if (left) {
                    if (!(l.parent instanceof DivEquation)) {
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
                    boolean can = true;
                    boolean op = false;
                    if (l instanceof WritingLeafEquation){
                        op = ((WritingLeafEquation) l).isOpLeft();
                    }
                    can &= !op;
                    if (!hasMatch()) {
                        can = false;
                        tryMoveRight();
                    }

                    if (can) {
                        // if we are right things are a little more complex
                        // we have to find our other half
                        Equation leftAt = emilyView.left();
                        while ((leftAt != null) && !(leftAt instanceof WritingPraEquation && ((WritingPraEquation) leftAt).left)) {
                            leftAt = leftAt.left();
                        }
                        if (leftAt != null) {
                            Equation rightAt = emilyView.selected;
                            while (rightAt != null && !(rightAt.parent.equals(leftAt.parent))) {
                                rightAt = rightAt.right();
                            }
                            if (rightAt == null) {
                                // we add at the right end of leftAt.parent
                                emilyView.insertAt(leftAt.parent, leftAt.parent.size(), newEq);
                            } else {
                                // we add left of rightAt
                                emilyView.insertAt(leftAt.parent, rightAt.parent.indexOf(rightAt), newEq);
                            }
                        }
                    }
                }
            } else {
                if (left) {
                    emilyView.insert(newEq);
                }
            }

        }
    }


}
