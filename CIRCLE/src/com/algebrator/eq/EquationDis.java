package com.algebrator.eq;

import android.util.Log;

import java.util.ArrayList;

public class EquationDis implements Comparable<EquationDis> {
	public float dis;
	public Equation equation;
	float x;
	float y;
    public Side side;
    public enum Side {left,right,top,bottom}


	/**
	 * @param equation
	 * @param equation
	 */



	public EquationDis(Equation equation, DragEquation dragging,Side side) {
		super();
        this.side = side;
        this.equation = equation;
		this.x = dragging.eq.x;
		this.y = dragging.eq.y;
        float topLeft = dis(x-dragging.eq.measureWidth()/2,y-dragging.eq.measureHeightUpper());
        float topRight = dis(x+dragging.eq.measureWidth()/2,y-dragging.eq.measureHeightUpper());
        float botLeft = dis(x-dragging.eq.measureWidth()/2,y+dragging.eq.measureHeightLower());
        float botRight = dis(x+dragging.eq.measureWidth()/2,y+dragging.eq.measureHeightLower());
        this.dis = Math.max(Math.max(topLeft,topRight),Math.max(botLeft,botRight));
//		this.dis = Math.max(
//                    Math.max(
//                            dis(equation.x+equation.measureWidth()/2f,equation.y+equation.measureHeight()/2f,x,y),
//                            dis(equation.x-equation.measureWidth()/2f,equation.y+equation.measureHeight()/2f,x,y)
//                ),Math.max(
//                            dis(equation.x+equation.measureWidth()/2f,equation.y-equation.measureHeight()/2f,x,y),
//                            dis(equation.x-equation.measureWidth()/2f,equation.y-equation.measureHeight()/2f,x,y)));

	}

    public EquationDis(Equation equation, float x, float y,Side side) {
        super();
        this.side = side;
        this.x = x;
        this.y = y;
        this.equation = equation;
        this.dis = dis(x,y);
//		this.dis = Math.max(
//                    Math.max(
//                            dis(equation.x+equation.measureWidth()/2f,equation.y+equation.measureHeight()/2f,x,y),
//                            dis(equation.x-equation.measureWidth()/2f,equation.y+equation.measureHeight()/2f,x,y)
//                ),Math.max(
//                            dis(equation.x+equation.measureWidth()/2f,equation.y-equation.measureHeight()/2f,x,y),
//                            dis(equation.x-equation.measureWidth()/2f,equation.y-equation.measureHeight()/2f,x,y)));

    }

    private float dis(float x, float y) {
        float dx = x - (equation.x + (side == Side.left?equation.measureWidth()/2f:0)- (side == Side.right?equation.measureWidth()/2f:0)) ;
        float dy = y - (equation.y + (side == Side.bottom?equation.measureHeightLower():0)- (side == Side.right?equation.measureHeightUpper():0));
        return  (float) Math.sqrt((dx * dx) + (dy * dy));
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

	public ArrayList<EquationDis> tryInsert(DragEquation dragging) {
        if (side == Side.left || side == Side.right) {
            return tryX(dragging);
        }else{
            return tryY(dragging);
		}
	}

	private ArrayList<EquationDis> tryX(DragEquation dragging) {
		if (dragging.ops.contains(Equation.Op.ADD)) {
			if (equation.x > x) {
                ArrayList<EquationDis> result = equation.tryOp(dragging,false,Equation.Op.ADD);
				if (result==null){
                    return  null;
                }
			} else {
                ArrayList<EquationDis> result =equation.tryOp(dragging,true,Equation.Op.ADD);
                if (result==null){
                    return  null;
                }
			}
		}
        if (dragging.ops.contains(Equation.Op.MULTI)){
			if (equation.x > x) {
                ArrayList<EquationDis> result =equation.tryOp(dragging,false,Equation.Op.MULTI);
                if (result==null){
                    return  null;
                }
			} else {
                ArrayList<EquationDis> result =equation.tryOp(dragging,true,Equation.Op.MULTI);
                if (result==null){
                    return  null;
                }
			}
		}
        if (dragging.ops.contains(Equation.Op.POWER)){
            ArrayList<EquationDis> result =equation.tryOp(dragging,false,Equation.Op.POWER);
            if (result==null){
                return  null;
            }
        }
        return new ArrayList<EquationDis>();
	}

	private ArrayList<EquationDis> tryY(DragEquation dragging) {
		if (dragging.ops.contains(Equation.Op.DIV)) {
			if (equation.x > x) {
                ArrayList<EquationDis> result =equation.tryOp(dragging,false,Equation.Op.DIV);
                if (result==null){
                    return  null;
                }
			} else {
                ArrayList<EquationDis> result =equation.tryOp(dragging,true,Equation.Op.DIV);
                if (result==null){
                    return  null;
                }
			}
		}
        if (dragging.ops.contains(Equation.Op.POWER)){
            ArrayList<EquationDis> result =equation.tryOp(dragging,false,Equation.Op.POWER);
            if (result==null){
                return  null;
            }
        }
        if (equation.parent instanceof EqualsEquation) {
            return new ArrayList<EquationDis>();
        }
        // we need the ArrayList of the parents stuff
        ArrayList<EquationDis> result = new  ArrayList<EquationDis>();
        if (equation.parent!= null) {
            result.add(new EquationDis(equation.parent, dragging.eq.x, dragging.eq.y, EquationDis.Side.left));
            result.add(new EquationDis(equation.parent, dragging.eq.x, dragging.eq.y, EquationDis.Side.right));
            if (!(equation instanceof DivEquation)) {
                result.add(new EquationDis(equation.parent, dragging.eq.x, dragging.eq.y, EquationDis.Side.top));
                result.add(new EquationDis(equation.parent, dragging.eq.x, dragging.eq.y, EquationDis.Side.bottom));
            }
        }
        return result;
	}
}
