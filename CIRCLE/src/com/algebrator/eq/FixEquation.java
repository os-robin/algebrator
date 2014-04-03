package com.algebrator.eq;

import com.example.circle.EmilyView;

import android.graphics.Canvas;

public abstract class FixEquation extends Equation {

	public FixEquation(EmilyView ev) {
		super(ev);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isFlex() {
		return false;
	}
	public boolean canInstertAt(int pos, Equation e){
		return false;
	}
}
