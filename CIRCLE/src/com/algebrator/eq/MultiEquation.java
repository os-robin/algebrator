package com.algebrator.eq;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

import android.graphics.Canvas;

public class MultiEquation extends FlexEquation implements MultiDivSuperEquation {

	@Override
	public Equation copy() {
		Equation result = new MultiEquation(this.owner);
		result.display = this.display;
		result.parentheses = this.parentheses;
		// pass selected?

		// copy all the kiddos and set this as their parent
		for (int i = 0; i < this.size(); i++) {
			result.add(this.get(i).copy());
			result.get(i).parent = result;
		}
		return result;
	}
	
	/**
	 * returns true is the given equation could be "on top" if this MultiEquation was written as A/B
	 * @param equation - a child of this
	 * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
	 */
	@Override
	public boolean onTop(Equation equation){
		boolean currentTop= true;
		Equation current = equation;
		while (true){
			//TODO if current is an EqualsEquation Error out
			//TODO if we hit something that is not a div or multi error out
			if (current.parent.equals(this)){
				return currentTop;
			}
			if (current.parent instanceof DivEquation){
				currentTop = ((DivEquation)current.parent).onTop(current) == currentTop;
			}
			current = current.parent;
		}
	}

	public MultiEquation(SuperView owner) {
		super(owner);
		display = "*";

		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		// TODO Auto-generated constructor stub
	}
}
