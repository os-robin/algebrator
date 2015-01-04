package com.example.circle.Actions;

import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.EmilyView;

public class DecimalAction extends Action {
	
	public String dec;
	
	public DecimalAction(EmilyView emilyView, String dec){
		super(emilyView);
		this.dec = dec;
	}

	@Override
	public void act() {
	
		if (emilyView.selected instanceof PlaceholderEquation) {
			PlaceholderEquation oldEq = (PlaceholderEquation)emilyView.selected;
			NumConstEquation numEq = new NumConstEquation(0, emilyView);
            numEq.setDisplay(numEq.getDisplay(0)+".");
			oldEq.replace(numEq);
		}
		
		else if (emilyView.selected instanceof NumConstEquation && emilyView.selected.getDisplay(-1).contains(".") == false) {
            String toSet = emilyView.selected.getDisplay(0) + dec;
			emilyView.selected.setDisplay(toSet);
		}
		
	}

}

