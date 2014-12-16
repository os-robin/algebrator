package com.algebrator.eq;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

import android.graphics.Canvas;
import android.util.Log;

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
		String debug = current.hashCode() + ", ";
		while (true){
			
			if (current.equals(this)){
				return currentTop;
			}
			if (current.parent instanceof DivEquation){
				currentTop = ((DivEquation)current.parent).onTop(current) == currentTop;
			}
			current = current.parent;
			if (current == null){
				Log.i("bad",debug + " this is: " + this.hashCode());
			}
			debug += current.hashCode()+",";
		}
	}

	public MultiEquation(SuperView owner) {
		super(owner);
		display = "*";

		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
	}
}
