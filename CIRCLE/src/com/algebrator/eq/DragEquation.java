package com.algebrator.eq;


import com.algebrator.eq.Equation.Op;

public class DragEquation {
    // this is the on we draw
	public Equation eq;
    // this is the one in the equation
	public Equation demo;
	public boolean add;
    public Equation oldDemo;
	
	public DragEquation(Equation eq) {
		super();
		this.eq = eq.copy();
		// is this a good idea?
		this.eq.parent = null;
        this.oldDemo = eq;
		this.demo = eq;
		// figure out if it is add
		//TODO what happens if it is a Equals equation?
		this.add = getAdd(eq);
	}

    private boolean getAdd(Equation equation) {
        while (equation.parent instanceof MinusEquation){
            equation = equation.parent;
        }
        return (equation.parent instanceof AddEquation || equation.parent instanceof EqualsEquation);
    }

    public Equation getAndUpdateDemo(Equation equation, boolean sameSide) {
        Equation toInsert;
        if (sameSide){
            if (demo instanceof MinusEquation){
                toInsert = demo.get(0);
            }else{
                toInsert = new MinusEquation(equation.owner);
                toInsert.add(demo);
            }
        }else{
            toInsert = demo;
        }
        toInsert.isDemo(true);
        float oldX = eq.x;
        float oldY = eq.y;
        eq = demo.copy();
        eq.x = oldX;
        eq.y = oldY;
        return toInsert;
    }
}
