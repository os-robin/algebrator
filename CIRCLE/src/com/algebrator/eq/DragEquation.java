package com.algebrator.eq;

public class DragEquation {
	public Equation eq;
	public Equation demo;
	public boolean add;
	public int side;
	
	public DragEquation(Equation eq) {
		super();
		this.eq = eq.copy();
		// is this a good idea?
		this.eq.parent = null;
		this.demo = eq;
		this.side = eq.side();
		// figure out if it is add
		//TODO what happens if it is a Equals equation?
		this.add = (eq.parent instanceof AddEquation || eq.parent instanceof EqualsEquation);
	}
}
