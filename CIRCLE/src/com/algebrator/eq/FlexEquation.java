package com.algebrator.eq;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public abstract class FlexEquation extends Equation {
	public FlexEquation(SuperView owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isFlex() {
		return true;
	}
}
