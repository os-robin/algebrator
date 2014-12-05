package com.example.circle.Actions;

import com.algebrator.eq.DivEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.MultiDivSuperEquation;
import com.algebrator.eq.MultiEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.EmilyView;

public class DivAction extends Action {

	public DivAction(EmilyView emilyView) {
		super(emilyView);
	}

	@Override
	public void act() {

		Equation oldEq = emilyView.selected;
		DivEquation newEq = new DivEquation(emilyView);
		if (oldEq.parentheses) {
			oldEq.parentheses = false;
			newEq.parentheses = true;
		}
		oldEq.replace(newEq);
		newEq.add(oldEq);

		PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
		newEq.add(rightAdd);
		rightAdd.setSelected(true);

	}

}
