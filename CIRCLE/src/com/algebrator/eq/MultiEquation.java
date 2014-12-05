package com.algebrator.eq;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.example.circle.EmilyView;

import android.graphics.Canvas;

public class MultiEquation extends MultiDivSuperEquation {

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

	public MultiEquation(EmilyView ev) {
		super(ev);
		display = "*";
		// TODO Auto-generated constructor stub
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

	@Override
	public EquationLoc closestPossible(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInstertAt(int pos, Equation eq) {
		return canMulti(eq);
//		// we can't put things in themselves
//		if (eq.deepContains(this)) {
//			return false;
//		}
//		// if the other is a div multi element of this
//		if (DivMultiContain(eq)) {
//			if (onTop(eq)) {
//				return true;
//			}
//		}
//		// if this and eq are both div multi elements of something
//		// and they are both on the same side of that something
//		Equation lcc = lowestCommonContainer(eq);
//		if (lcc instanceof MultiEquation) {
//			if (((MultiEquation) lcc).DivMultiContain(this)
//					&& ((MultiEquation) lcc).DivMultiContain(this)) {
//				if (((MultiEquation) lcc).onTop(this) == ((MultiEquation) lcc)
//						.onTop(this)) {
//					return true;
//				}
//			}
//		}
//		// this this and other are on different sides
//		if (lcc instanceof EqualsEquation) {
//			// figure out what side this is on
//			int mySide = ((EqualsEquation) lcc).side(this);
//			// if both size have div or multi as top node
//			if (lcc.get(mySide) instanceof MultiDivSuperEquation
//					&& lcc.get(EqualsEquation.otherSide(mySide)) instanceof MultiDivSuperEquation) {
//				// if this and eq are both div multi contained by thier sides
//				// top
//				if (((MultiDivSuperEquation) lcc.get(mySide))
//						.DivMultiContain(this)
//						&& ((MultiDivSuperEquation) lcc.get(mySide))
//								.DivMultiContain(this)) {
//					// if we have different on topness
//					if (((MultiDivSuperEquation) lcc.get(mySide)).onTop(this) != ((MultiDivSuperEquation) lcc
//							.get(EqualsEquation.otherSide(mySide))).onTop(eq)) {
//						return true;
//					}
//				}
//			}
//		}
//		// TODO other cases?
//		return false;
	}
}
