package com.algebrator.eq;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

import android.graphics.Canvas;

public abstract class FixEquation extends Equation {
	// TODO this should override add and remove? so that they can't  happen
	
	
	public FixEquation(SuperView owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isFlex() {
		return false;
	}
}
