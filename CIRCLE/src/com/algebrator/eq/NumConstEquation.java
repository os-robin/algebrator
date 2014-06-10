package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.BigDaddy;
import com.example.circle.EmilyView;

public class NumConstEquation extends LeafEquation {
	private double value;
	
	public NumConstEquation(String display, EmilyView ev) {
		super(ev);
		this.display = display;

	}
	
	@Override
	public Equation copy() {
		Equation result = new NumConstEquation(this.display, this.owner);
		result.parenthesis = this.parenthesis;
		return result;
	}


}
