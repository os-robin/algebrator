package com.example.circle.Actions;

import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.VarEquation;
import com.example.circle.EmilyView;

public class VarAction extends Action {
	

	public String var;
	
	public VarAction(EmilyView emilyView, String var) {
		super(emilyView);
	}

	@Override
	public void act() {
		
		if (emilyView.selected instanceof PlaceholderEquation) {
			((PlaceholderEquation) emilyView.selected).replace(new VarEquation(var, emilyView));;
		}
		
	}
	

}
