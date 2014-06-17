package com.example.circle;

import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;

public class DecimalAction extends Action {
	
	public EmilyView emilyView;
	public String dec;
	
	public DecimalAction(EmilyView emilyView, String dec){
		super();
		this.emilyView = emilyView;
		this.dec = dec;
	}

	@Override
	public void act() {
	
		if (emilyView.selected instanceof PlaceholderEquation) {
			PlaceholderEquation oldEq = (PlaceholderEquation)emilyView.selected;
			NumConstEquation numEq = new NumConstEquation("0.", emilyView);
			oldEq.replace(numEq);
		}
		
		else if (emilyView.selected instanceof NumConstEquation && emilyView.selected.getDisplay(-1).contains(".") == false) {
			emilyView.selected.setDisplay(emilyView.selected.getDisplay(-1) + dec);
		}
		
	}

}

