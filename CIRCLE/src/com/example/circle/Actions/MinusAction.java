package com.example.circle.Actions;

import com.algebrator.eq.AddEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.EmilyView;

public class MinusAction extends Action {

	public MinusAction(EmilyView emilyView) {
		super(emilyView);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		// if you have an add equation selected add a new element
		if (emilyView.selected instanceof AddEquation) {
			Equation oldEq = emilyView.selected;
			PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
			newEq.negative = true;
			oldEq.add(newEq);
			newEq.setSelected(true);
		} else 
			
		// if what we have selected is a PlaceHolderEquation
		if (emilyView.selected instanceof PlaceholderEquation) {
			emilyView.selected.negative = ! emilyView.selected.negative;
		} else

		// if what you have selected is part of a add equation
		if (emilyView.selected.parent instanceof AddEquation) {
			Equation oldEq = emilyView.selected.parent;
			PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
			newEq.negative = true;
			oldEq.add(newEq);
			newEq.setSelected(true);
		}

		// otherwise create a new add equation
		else {
			Equation oldEq = emilyView.selected;
			
			AddEquation newEq = new AddEquation(emilyView);
			if (oldEq.parentheses){
				oldEq.parentheses = false;
				newEq.parentheses = true;
			}
			oldEq.replace(newEq);
			newEq.add(oldEq);

			PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
			rightAdd.negative = true;
			newEq.add(rightAdd);
			rightAdd.setSelected(true);
		}
	}

}
