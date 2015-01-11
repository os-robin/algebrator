package com.algebrator.eq;

import android.util.Log;

public class EquationDis implements Comparable<EquationDis> {
	public float dis;
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
		this.dis = Math.max(
                    Math.max(
                            dis(equation.x+equation.measureWidth()/2f,equation.y+equation.measureHeight()/2f,x,y),
                            dis(equation.x-equation.measureWidth()/2f,equation.y+equation.measureHeight()/2f,x,y)
                ),Math.max(
                            dis(equation.x+equation.measureWidth()/2f,equation.y-equation.measureHeight()/2f,x,y),
                            dis(equation.x-equation.measureWidth()/2f,equation.y-equation.measureHeight()/2f,x,y)));
		this.equation = equation;
	}

    private float dis(float x, float y, float x1, float y1) {
        float dx = x - x1;
        float dy = y - y1;
      return (float) Math.sqrt((dx * dx) + (dy * dy));
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
				return equation.tryOp(dragging,false,Equation.Op.ADD);
			} else {
				return equation.tryOp(dragging,true,Equation.Op.ADD);
			}
		}else{
			if (equation.x > x) {
				return equation.tryOp(dragging,false,Equation.Op.MULTI);
			} else {
				return equation.tryOp(dragging,true,Equation.Op.MULTI);
			}
		}
	}

	private boolean tryY(DragEquation dragging) {
		if (dragging.add) {
			return false;
		}else{
			if (equation.x > x) {
				return equation.tryOp(dragging,false,Equation.Op.DIV);
			} else {
				return equation.tryOp(dragging,true,Equation.Op.DIV);
			}
		}
	}
}
