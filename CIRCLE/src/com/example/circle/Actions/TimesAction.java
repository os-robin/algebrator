package com.example.circle.Actions;

import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.algebrator.eq.WritingPraEquation;
import com.example.circle.EmilyView;

public class TimesAction extends Action {

    public TimesAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            Equation l = emilyView.left();
            boolean can = true;
            if (l instanceof WritingLeafEquation) {
                can = !((WritingLeafEquation) l).isOpLeft();
            }
            if (l instanceof WritingPraEquation && ((WritingPraEquation) l).left){
                can = false;
            }
            if (l != null && can) {
                Equation newEq = new WritingLeafEquation("*", emilyView);
                emilyView.insert(newEq);
            }
        } else if (emilyView.selected != null) {

            Equation numEq = new WritingLeafEquation("*", emilyView);
            addToBlock(numEq);
        }

//		if (emilyView.selected != null) {
//            if (! (emilyView.selected instanceof WritingEquation) ){
//
//                    if (emilyView.selected.parent instanceof WritingEquation) {
//                        if (emilyView.selected instanceof PlaceholderEquation) {
//                            //int at = emilyView.selected.parent.indexOf(emilyView.selected);
//                            //emilyView.selected.parent.add(at, new WritingLeafEquation("*", emilyView));
//                        }else {
//                            emilyView.selected.parent.add(new WritingLeafEquation("*", emilyView));
//                            Equation newEq = new PlaceholderEquation(emilyView);
//                            emilyView.selected.parent.add(newEq);
//                            newEq.setSelected(true);
//                        }
//                    }else{
//                            Equation oldEq = emilyView.selected;
//                            Equation newEq = new WritingEquation(emilyView);
//                            emilyView.selected.replace(newEq);
//                            newEq.add(oldEq);
//                            newEq.add(new WritingLeafEquation("*", emilyView));
//                            Equation newPlaceholder = new PlaceholderEquation(emilyView);
//                            newEq.add(newPlaceholder);
//                            newPlaceholder.setSelected(true);
//                    }
//            }else {
//                //TODO rap in () and trow a + in after
//            }

//			if (emilyView.selected instanceof MultiEquation) {
//				Equation oldEq = emilyView.selected;
//				PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
//				oldEq.add(newEq);
//				newEq.setSelected(true);
//
//			} else
//
//			if (emilyView.selected.parent instanceof MultiEquation) {
//				Equation oldEq = emilyView.selected.parent;
//				PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
//				oldEq.add(newEq);
//				newEq.setSelected(true);
//			}
//
//			else {
//				Equation oldEq = emilyView.selected;
//				MultiEquation newEq = new MultiEquation(emilyView);
//				if (oldEq.parentheses) {
//					oldEq.parentheses = false;
//					newEq.parentheses = true;
//				}
//				oldEq.replace(newEq);
//				newEq.add(oldEq);
//
//				PlaceholderEquation rightAdd = new PlaceholderEquation(
//						emilyView);
//				newEq.add(rightAdd);
//				rightAdd.setSelected(true);
//			}
        //}
    }
}
