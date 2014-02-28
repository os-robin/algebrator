package com.algebrator.eq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import android.graphics.Canvas;

public class MultiEquation extends FlexEquation {
	/**
	 * returns true is the given equation could be "on top" if this MultiEquation was written as A/B
	 * @param equation - a child of this
	 * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
	 */
	public boolean isTop(Equation equation){
		boolean currentTop= true;
		Equation current = equation;
		while (true){
			//TODO if current is an EqualsEquation Error out
			//TODO if we hit something that is not a div or multi error out
			if (current.parent.equals(this)){
				return currentTop;
			}
			if (current.parent instanceof DivEquation){
				currentTop = ((DivEquation)current.parent).isTop(current) == currentTop;
			}
			current = current.parent;
		}
	}

	/**
	 * check to see if all the generations between an equation and this are divEquations or MultiEquations
	 * @param equation
	 * @return
	 */
	
	public boolean DivMultiContain(Equation equation){
		Equation current = equation;
		while (true){
			if (current.parent.equals(this)){
				return true;
			}else if (current.parent instanceof DivEquation || current.parent instanceof MultiEquation){
				current = current.parent;
			}else{
				return false;
			}
		}
	}

	@Override
	public float measureWidth() {
		return horizMeasureWidth();
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		horizDraw(canvas,x,y);
		
	}

	@Override
	public float measureHeight() {
		return horizMeasureHeight();
	}
}
