package com.example.circle.Actions;

import com.algebrator.eq.Equation;
import com.algebrator.eq.MultiDivSuperEquation;
import com.algebrator.eq.MultiEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.EmilyView;

public class TimesAction extends Action {

	public TimesAction(EmilyView emilyView) {
		super(emilyView);
	}

	@Override
	public void act() {
		if (emilyView.selected != null) {
			if (emilyView.selected instanceof MultiEquation) {
				Equation oldEq = emilyView.selected;
				PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
				oldEq.add(newEq);
				newEq.setSelected(true);

			} else

			if (emilyView.selected.parent instanceof MultiEquation) {
				Equation oldEq = emilyView.selected.parent;
				PlaceholderEquation newEq = new PlaceholderEquation(emilyView);
				oldEq.add(newEq);
				newEq.setSelected(true);
			}

			else {
				Equation oldEq = emilyView.selected;
				MultiEquation newEq = new MultiEquation(emilyView);
				if (oldEq.parentheses) {
					oldEq.parentheses = false;
					newEq.parentheses = true;
				}
				oldEq.replace(newEq);
				newEq.add(oldEq);

				PlaceholderEquation rightAdd = new PlaceholderEquation(
						emilyView);
				newEq.add(rightAdd);
				rightAdd.setSelected(true);
			}
		}
	}
}
