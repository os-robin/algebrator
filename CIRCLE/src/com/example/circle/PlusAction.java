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
		// TODO Auto-generated method stub
		Equation oldEq = emilyView.selected;
		AddEquation newEq = new AddEquation(emilyView);
		oldEq.replace(newEq);
		newEq.add(oldEq);
		
		PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
		newEq.add(rightAdd);
		rightAdd.setSelected(true);
	}

}
