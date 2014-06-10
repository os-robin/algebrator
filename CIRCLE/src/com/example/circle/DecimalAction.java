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
			NumConstEquation numEq = new NumConstEquation("0.", emilyView);
			int index = emilyView.selected.parent.indexOf(emilyView.selected);
			emilyView.selected.parent.set(index, numEq);
			numEq.setSelected(true);
		}
		
		else if (emilyView.selected instanceof NumConstEquation && emilyView.selected.display.contains(".") == false) {
			emilyView.selected.display = emilyView.selected.display + dec;
		}
		
	}

}

