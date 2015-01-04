package com.algebrator.eq;

public class DragEquation {
    // this is the on we draw
	public Equation eq;
    // this is the one in the equation
	public Equation demo;
	public boolean add;
	
	public DragEquation(Equation eq) {
		super();
		this.eq = eq.copy();
		// is this a good idea?
		this.eq.parent = null;
		this.demo = eq;
		// figure out if it is add
		//TODO what happens if it is a Equals equation?
		this.add = (eq.parent instanceof AddEquation || eq.parent instanceof EqualsEquation);
	}
}
