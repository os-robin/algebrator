package com.algebrator.eq;

import com.example.circle.EmilyView;

import android.graphics.Canvas;

public abstract class MultiDivSuperEquation extends FlexEquation {

	public MultiDivSuperEquation(EmilyView ev) {
		super(ev);
	}
	
	/**
	 * check to see if all the generations between an equation and this are divEquations or MultiEquations
	 * equations are considered to contain themselves
	 * @param equation
	 * @return
	 */
	public boolean DivMultiContain(Equation equation){
		Equation current = equation;
		if (equation.equals(this)){
			return true;
		}
		while (true){
			if (current.parent.equals(this)){
				return true;
			}else if (current.parent instanceof MultiDivSuperEquation){
				current = current.parent;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * returns true is the given equation could be "on top" if this MultiEquation was written as A/B
	 * @param equation - a child of this
	 * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
	 */
	public abstract boolean onTop(Equation equation);

}
