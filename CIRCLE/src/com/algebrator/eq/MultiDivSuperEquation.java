package com.algebrator.eq;

import com.example.circle.EmilyView;

import android.graphics.Canvas;

public abstract class MultiDivSuperEquation extends FlexEquation {

	public MultiDivSuperEquation(EmilyView ev) {
		super(ev);
	}
	
	//TODO is this different than what is in Equation?
//	@Override
//	public boolean canDiv(Equation eq) {
//		
//		// if the element is an element of this but not on top
//		if (DivMultiContain(eq)){
//			if (!this.onTop(eq)){
//				return true;
//			}
//		}
//		// if these this and eq are both the div multi child of a super equation
//		// this contains the above case 
//		Equation lcc = lowestCommonContainer(eq);
//		if (lcc instanceof MultiDivSuperEquation){
//			MultiDivSuperEquation lccmd = (MultiDivSuperEquation)lcc;
//			if (lccmd.DivMultiContain(this)
//					&& lccmd.DivMultiContain(eq)){
//				if (lccmd.onTop(eq)!= lccmd.onTop(this)){
//					return true;
//				}
//			}
//		}
//		// if eq is on the other side
//		if (lcc instanceof EqualsEquation){
//			EqualsEquation lccee = (EqualsEquation) lcc;
//			// if both top are div multi equation
//			if (lccee.get(lccee.side(this)) instanceof MultiDivSuperEquation){
//				MultiDivSuperEquation mySide =(MultiDivSuperEquation)lccee.get(lccee.side(this));
//				if (mySide.DivMultiContain(this)){
//					if (lccee.get(lccee.side(eq)) instanceof MultiDivSuperEquation){
//						MultiDivSuperEquation eqSide =(MultiDivSuperEquation)lccee.get(lccee.side(eq));
//						if (eqSide.DivMultiContain(eq)){
//							if (eqSide.onTop(eq)== mySide.onTop(this)){
//								return true;
//							}
//						}
//					}
//				}	
//			}
//		}
//		return false;
//	}

	
	/**
	 * returns true is the given equation could be "on top" if this MultiEquation was written as A/B
	 * @param equation - a child of this
	 * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
	 */
	public abstract boolean onTop(Equation equation);

}
