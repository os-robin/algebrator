package com.algebrator.eq;

public class EquationDis implements Comparable{
	float dis;
	Equation Equation;
	/**
	 * @param equation
	 * @param equation
	 */
	public EquationDis(Equation equation, float dis) {
		super();
		this.dis = dis;
		Equation = equation;
	}
	@Override
	public int compareTo(Object other) {
		float otherDis = ((EquationDis)other).dis;
		if (otherDis > dis){
			return -1;
		}else if(otherDis < dis){
			return 1;
		}
		return 0;
	}
	
	

}
