package com.algebrator.eq;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class VarEquation extends LeafEquation {
		
	public VarEquation(String display, SuperView owner) {
		super(owner);
		this.display = display;

	}
	
	@Override
	public Equation copy() {
		Equation result = new VarEquation(this.display, this.owner);

		result.parentheses = this.parentheses;
		result.negative = this.negative;

		return result;
	}
	
	@Override
	public boolean same(Equation eq){
		if (!(eq instanceof LeafEquation))
			return false;
		LeafEquation e = (LeafEquation)eq;
		return display.equals(e.display);
	}
}