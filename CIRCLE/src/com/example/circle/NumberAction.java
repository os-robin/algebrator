package com.example.circle;

import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;

public class NumberAction extends Action {
	
	public EmilyView emilyView;
	public String num;
	
	public NumberAction(EmilyView emilyView, String num){
		super();
		this.emilyView = emilyView;
		this.num = num;
	}

	@Override
	public void act() {
	
		if (emilyView.selected instanceof PlaceholderEquation) {
			PlaceholderEquation oldEq = (PlaceholderEquation) emilyView.selected;
			oldEq.replace(new NumConstEquation(num, emilyView));
		}
		
		else if (emilyView.selected instanceof NumConstEquation && ! emilyView.selected.getDisplay(-1).equals("0")) {
			emilyView.selected.setDisplay( emilyView.selected.getDisplay(-1) + num);
		}
		
		else if (emilyView.selected instanceof NumConstEquation && emilyView.selected.getDisplay(-1).equals("0")) {
			emilyView.selected.setDisplay(num);
		}
		
	}

}
