package com.example.circle;

import com.algebrator.eq.AddEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;

public class PlusAction extends Action {
	EmilyView emilyView;

	public PlusAction(EmilyView emilyView) {
		this.emilyView = emilyView;
	}

	@Override
	public void act() {
		// if you have an add equation selected add a new element
		if (emilyView.selected instanceof AddEquation) {
			Equation oldEq = emilyView.selected;
			PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
			oldEq.add(newEq);
			newEq.setSelected(true);
			
		}else 
		
		// if what you have selected is part of a add equation
		if (emilyView.selected.parent instanceof AddEquation){
				Equation oldEq = emilyView.selected.parent;
				PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
				oldEq.add(newEq);
				newEq.setSelected(true);
		}
		
		// otherwise create a new add equation
		else{
			Equation oldEq = emilyView.selected;
			AddEquation newEq = new AddEquation(emilyView);
			oldEq.replace(newEq);
			newEq.add(oldEq);
		
			PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
			newEq.add(rightAdd);
			rightAdd.setSelected(true);
		}
	}

}
