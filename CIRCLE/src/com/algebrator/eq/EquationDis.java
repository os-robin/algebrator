package com.algebrator.eq;

import android.util.Log;

public class EquationDis implements Comparable<EquationDis> {
	float dis;
	public LeafEquation equation;
	float x;
	float y;

	/**
	 * @param equation
	 * @param equation
	 */

	public EquationDis(LeafEquation equation, float x, float y) {
		super();
		this.x = x;
		this.y = y;
		float dx = x - equation.x;
		float dy = y - equation.y;
		float dis = (float) Math.sqrt((dx * dx) + (dy * dy));
		this.dis = dis;
		this.equation = equation;
	}

	@Override
	public int compareTo(EquationDis other) {
		float otherDis = other.dis;
		if (otherDis > dis) {
			return -1;
		} else if (otherDis < dis) {
			return 1;
		}
		return 0;
	}

	public boolean tryInsert(DragEquation dragging) {
		float dx = x - equation.x;
		// we only care about dy if the mouse is under us
		float dy = Math.max(y - equation.y,0);
		if (Math.abs(dy) < Math.abs(dx)) {
			if (tryY(dragging)) {
				return true;
			}
			if (tryX(dragging)) {
				return true;
			}
		} else {
			if (tryX(dragging)) {
				return true;
			}
			if (tryY(dragging)) {
				return true;
			}
		}
		return false;
	}

	private boolean tryX(DragEquation dragging) {
		if (dragging.add) {
			if (equation.x > x) {
				return equation.tryAddLeft(dragging);
			} else {
				return equation.tryAddRight(dragging);
			}
		}else{
			if (equation.x > x) {
				return equation.tryMultiLeft(dragging);
			} else {
				return equation.tryMultiRight(dragging);
			}
		}
	}

	private boolean tryY(DragEquation dragging) {
		if (dragging.add) {
			return false;
		}else{
			return equation.tryDiv(dragging);
		}
	}
}
