package com.algebrator.eq;

import com.example.circle.EmilyView;

public class VarEquation extends LeafEquation {
		
	public VarEquation(String display, EmilyView ev) {
		super(ev);
		this.display = display;

	}
	
	@Override
	public Equation copy() {
		Equation result = new VarEquation(this.display, this.owner);

		result.parentheses = this.parentheses;

		return result;
	}


}