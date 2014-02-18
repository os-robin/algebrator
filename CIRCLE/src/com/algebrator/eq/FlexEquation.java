package com.algebrator.eq;

public abstract class FlexEquation extends Equation {
	Equation empty;
	
	public Equation remove(int i){
		Equation removed = super.remove(i);
		// if this is now empty replace it with the proper equation
		if (this.size() ==1){
			this.parent.set(this.parent.indexOf(this),this.get(0));
		}
		return removed;
	}
	public boolean remove(Equation equ){
		boolean result = super.remove(equ);
		// if this is now empty replace it with the proper equation
		if (this.size() ==1){
			this.parent.set(this.parent.indexOf(this),this.get(0));
		}
		return result;
	}
	@Override
	public boolean isFlex() {
		return true;
	}
}
