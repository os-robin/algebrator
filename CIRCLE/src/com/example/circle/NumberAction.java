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
			NumConstEquation numEq = new NumConstEquation(num, emilyView);
			int index = emilyView.selected.parent.indexOf(emilyView.selected);
			emilyView.selected.parent.set(index, numEq);
			numEq.setSelected(true);
		}
		
		else if (emilyView.selected instanceof NumConstEquation) {
			emilyView.selected.display = emilyView.selected.display + num;
		}
		
	}

}
