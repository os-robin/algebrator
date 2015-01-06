package com.example.circle.Actions;

import com.algebrator.eq.AddEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.MultiEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.EmilyView;

public class PlusAction extends Action {


	public PlusAction(EmilyView emilyView) {
		super(emilyView);
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
		}else if (emilyView.selected instanceof PlaceholderEquation){
            Equation oldEq = emilyView.selected;
            AddEquation newEq = new AddEquation(emilyView);
            newEq.parentheses =true;
            oldEq.replace(newEq);
            newEq.add(oldEq);
            PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
            newEq.add(rightAdd);
            oldEq.setSelected(true);
        }
		
		// otherwise create a new add equation
		else if (emilyView.selected.parent instanceof MultiEquation){
            Equation oldEq = emilyView.selected.parent;
            AddEquation newEq = new AddEquation(emilyView);
            oldEq.replace(newEq);
            newEq.add(oldEq);
            PlaceholderEquation rightAdd = new PlaceholderEquation(emilyView);
            newEq.add(rightAdd);
            rightAdd.setSelected(true);
        }else{
			Equation oldEq = emilyView.selected;
			AddEquation newEq = new AddEquation(emilyView);
			if (oldEq.parentheses){
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

}
