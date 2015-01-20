package com.example.circle.Actions;

import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingLeafEquation;
import com.algebrator.eq.WritingPraEquation;
import com.example.circle.EmilyView;

public class PlusAction extends Action {


    public PlusAction(EmilyView emilyView) {
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
                Equation newEq = new WritingLeafEquation("+", emilyView);
                emilyView.insert(newEq);
            }
        } else if (emilyView.selected != null) {

            Equation numEq = new WritingLeafEquation("+", emilyView);
            addToBlock(numEq);
        }


//        if (! (emilyView.selected instanceof WritingEquation) ){
//            if (emilyView.selected.parent instanceof WritingEquation) {
//                if (emilyView.selected instanceof PlaceholderEquation) {
//                    //int at = emilyView.selected.parent.indexOf(emilyView.selected);
//                    //emilyView.selected.parent.add(at, new WritingLeafEquation("+", emilyView));
//                }else {
//                    emilyView.selected.parent.add(new WritingLeafEquation("+", emilyView));
//                    Equation newEq = new PlaceholderEquation(emilyView);
//                    emilyView.selected.parent.add(newEq);
//                    newEq.setSelected(true);
//                }
//            }else{
//                Equation oldEq = emilyView.selected;
//                Equation newEq = new WritingEquation(emilyView);
//                emilyView.selected.replace(newEq);
//                newEq.add(oldEq);
//                newEq.add(new WritingLeafEquation("+", emilyView));
//                Equation newPlaceholder = new PlaceholderEquation(emilyView);
//                newEq.add(newPlaceholder);
//                newPlaceholder.setSelected(true);
//            }
//        }else {
//            //TODO rap in () and trow a + in after
//        }
//		// if you have an add equation selected add a new element
//		if (emilyView.selected instanceof AddEquation) {
//			Equation oldEq = emilyView.selected;
//			PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
//			oldEq.add(newEq);
//			newEq.setSelected(true);
//
//		}else
//
//		// if what you have selected is part of a add equation
//		if (emilyView.selected.parent instanceof AddEquation){
//				Equation oldEq = emilyView.selected.parent;
//				PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
//				oldEq.add(newEq);
//				newEq.setSelected(true);
//		}else if (emilyView.selected instanceof PlaceholderEquation){
//            Equation oldEq = emilyView.selected;
//            AddEquation newEq = new AddEquation(emilyView);
//            newEq.parentheses =true;
//            oldEq.replace(newEq);
//            newEq.add(oldEq);
//            PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
//            newEq.add(rightAdd);
//            oldEq.setSelected(true);
//        }
//
//		// otherwise create a new add equation
//		else if (emilyView.selected.parent instanceof MultiEquation){
//            Equation oldEq = emilyView.selected.parent;
//            AddEquation newEq = new AddEquation(emilyView);
//            oldEq.replace(newEq);
//            newEq.add(oldEq);
//            PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
//            newEq.add(rightAdd);
//            rightAdd.setSelected(true);
//        }else{
//			Equation oldEq = emilyView.selected;
//			AddEquation newEq = new AddEquation(emilyView);
//			if (oldEq.parentheses){
//				oldEq.parentheses = false;
//				newEq.parentheses = true;
//			}
//			oldEq.replace(newEq);
//			newEq.add(oldEq);
//
//			PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
//			newEq.add(rightAdd);
//			rightAdd.setSelected(true);
//		}
    }

}
